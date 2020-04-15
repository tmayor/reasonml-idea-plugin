package com.reason.esy;

import com.intellij.framework.detection.FileContentPattern;
import com.intellij.json.JsonFileType;
import com.intellij.json.psi.JsonFile;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonValue;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.FileContentImpl;
import com.reason.ide.facet.EsyFrameworkDetector;
import com.reason.ide.files.EsyPackageJsonFileType;
import org.jetbrains.annotations.NotNull;

public class EsyPackageJson {

    public static final FileType FILE_TYPE = EsyPackageJsonFileType.INSTANCE;

    // @TODO test
    public static boolean isEsyPackageJson(@NotNull VirtualFile virtualFile) {
        FileContent fileContent = FileContentImpl.createByFile(virtualFile);
        return createFilePattern().accepts(fileContent);
    }

    // @TODO test
    public static ElementPattern<FileContent> createFilePattern() {
        return FileContentPattern.fileContent()
                .withName(EsyPackageJsonFileType.INSTANCE.getName())
                .with(new PatternCondition<FileContent>(EsyFrameworkDetector.class.getName()) {
                    @Override
                    public boolean accepts(@NotNull FileContent fileContent, ProcessingContext context) {
                        PsiFile psiFile = fileContent.getPsiFile();
                        FileType fileType = psiFile.getFileType();
                        if (!(fileType instanceof JsonFileType)) {
                            return false;
                        }
                        JsonFile jsonFile = (JsonFile) psiFile;
                        JsonValue topLevelValue = jsonFile.getTopLevelValue();
                        if (!(topLevelValue instanceof JsonObject)) {
                            return false;
                        }
                        JsonObject topLevelObject = (JsonObject) topLevelValue;
                        return topLevelObject.findProperty("esy") != null;
                    }
                });
    }

    private EsyPackageJson() {}
}
