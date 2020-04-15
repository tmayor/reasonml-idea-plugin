package com.reason.ide;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.reason.Joiner;
import com.reason.Log;
import com.reason.StringUtil;
import com.reason.bs.Bucklescript;
import com.reason.ide.files.BsConfigJsonFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

public class FileManager {

    private static final Log LOG = Log.create("file");

    private FileManager() {}

    @Nullable
    public static PsiFile findCmtFileFromSource(@NotNull Project project, @NotNull String filenameWithoutExtension) {
        if (!DumbService.isDumb(project)) {
            GlobalSearchScope scope = GlobalSearchScope.allScope(project);
            String filename = filenameWithoutExtension + ".cmt";

            PsiFile[] cmtFiles = FilenameIndex.getFilesByName(project, filename, scope);
            if (cmtFiles.length == 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("File module for " + filename + " is NOT FOUND, files found: [" + Joiner.join(", ", cmtFiles) + "]");
                }
                return null;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Found cmt " + filename + " (" + cmtFiles[0].getVirtualFile().getPath() + ")");
            }

            return cmtFiles[0];
        } else {
            LOG.info("Cant find cmt while reindexing");
        }

        return null;
    }

    @NotNull
    public static String toRelativeSourceName(@NotNull Project project, @NotNull VirtualFile sourceFile, @NotNull Path relativePath) {
        String sourcePath = relativePath.toString();
        String namespace = ServiceManager.getService(project, Bucklescript.class).getNamespace(sourceFile);
        if (!namespace.isEmpty()) {
            sourcePath = sourcePath.replace("-" + StringUtil.toFirstUpper(namespace), "");
        }
        int dotPos = sourcePath.lastIndexOf(".");
        return 0 <= dotPos ? sourcePath.substring(0, dotPos) + ".re" : sourcePath;
    }

    @Nullable
    public static VirtualFile toSource(@NotNull Project project, @NotNull VirtualFile cmxFile, @NotNull Path relativeCmi) {
        String relativeSource = separatorsToUnix(toRelativeSourceName(project, cmxFile, relativeCmi));
        VirtualFile contentRoot = ORModuleManager.findFirstBsContentRoot(project).get();
        VirtualFile sourceFile = contentRoot == null ? null : contentRoot.findFileByRelativePath(relativeSource);

        if (sourceFile == null && contentRoot != null) {
            relativeSource = relativeSource.replace(".re", ".ml");
            sourceFile = contentRoot.findFileByRelativePath(relativeSource);
        }

        return sourceFile;
    }

    @Nullable
    private static Path pathFromSource(@NotNull Project project, @NotNull VirtualFile baseRoot, @NotNull Path relativeBuildPath,
                                       @NotNull VirtualFile sourceFile, boolean useCmt) {
        Path baseRootPath = FileSystems.getDefault().getPath(baseRoot.getPath());
        Path relativePath;
        try {
            relativePath = baseRootPath.relativize(new File(sourceFile.getPath()).toPath());
        } catch (IllegalArgumentException e) {
            // Path can't be relative
            return null;
        }

        Path relativeParent = relativePath.getParent();
        if (relativeParent != null) {
            relativeBuildPath = relativeBuildPath.resolve(relativeParent);
        }

        String namespace = ServiceManager.getService(project, Bucklescript.class).getNamespace(sourceFile);
        return relativeBuildPath.resolve(sourceFile.getNameWithoutExtension() + (namespace.isEmpty() ? "" : "-" + namespace) + (useCmt ? ".cmt" : ".cmi"));
    }

    @Nullable
    public static VirtualFile fromSource(@NotNull Project project, @NotNull VirtualFile baseRoot, @NotNull Path relativeBuildPath,
                                         @NotNull VirtualFile sourceFile, boolean useCmt) {
        Path path = pathFromSource(project, baseRoot, relativeBuildPath, sourceFile, useCmt);
        if (path == null) {
            return null;
        }
        String relativeCmiPath = separatorsToUnix(path.toString());
        return baseRoot.findFileByRelativePath(relativeCmiPath);
    }

    @NotNull
    private static String separatorsToUnix(@NotNull String path) {
        return path.replace('\\', '/');
    }

    // Special finder that iterate through parents until a bsConfig.json is found.
    // This is always needed, we can't use module itself

    @Nullable
    public static VirtualFile findAncestorBsconfig(@NotNull Project project, @NotNull VirtualFile sourceFile) {
        VirtualFile contentRoot = ORModuleManager.findFirstBsContentRoot(project).get();
        if (sourceFile.equals(contentRoot)) {
            return sourceFile;
        }

        VirtualFile parent = sourceFile.getParent();
        if (parent == null) {
            return sourceFile;
        }

        VirtualFile child = parent.findChild(BsConfigJsonFileType.getDefaultFilename());
        while (child == null) {
            VirtualFile grandParent = parent.getParent();
            if (grandParent == null) {
                break;
            }

            parent = grandParent;
            child = parent.findChild(BsConfigJsonFileType.getDefaultFilename());
            if (parent.equals(contentRoot)) {
                break;
            }
        }

        return child;
    }

    public static VirtualFile findAncestorContentRoot(Project project, VirtualFile file) {
        VirtualFile bsConfig = findAncestorBsconfig(project, file);
        return bsConfig == null ? null : bsConfig.getParent();
    }

    @NotNull
    public static String removeProjectDir(@NotNull Project project, @NotNull String path) {
        try {
            Optional<VirtualFile> baseRoot = ORModuleManager.findFirstBsContentRoot(project);
            if (!baseRoot.isPresent()) {
                return path;
            }
            Path basePath = FileSystems.getDefault().getPath(baseRoot.get().getPath());
            Path relativePath = basePath.relativize(new File(path).toPath());
            return relativePath.toString();
        } catch (IllegalArgumentException e) {
            return path;
        }
    }

    @Nullable
    public static VirtualFile findFileByRelativePath(@NotNull Project project, @NotNull String path) {
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            VirtualFile moduleFile = module.getModuleFile();
            VirtualFile baseDir = moduleFile == null ? null : moduleFile.getParent();
            VirtualFile file = baseDir == null ? null : baseDir.findFileByRelativePath(path);
            if (file != null) {
                return file;
            }
        }
        return null;
    }
}
