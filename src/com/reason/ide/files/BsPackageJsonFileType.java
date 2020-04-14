package com.reason.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BsPackageJsonFileType implements FileType {

    public static final FileType INSTANCE = new BsPackageJsonFileType();

    private BsPackageJsonFileType() {}

    @Nls
    @NotNull
    @Override
    public String getName() {
        return ".bsconfig";
    }

    @Nls
    @NotNull
    @Override
    public String getDescription() {
        return "BuckleScript configuration file.";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return ".bsconfig";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.BUCKLESCRIPT_TOOL;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return null;
    }
}
