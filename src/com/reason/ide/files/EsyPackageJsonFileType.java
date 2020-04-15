package com.reason.ide.files;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EsyPackageJsonFileType implements FileType {

    public static final FileType INSTANCE = new EsyPackageJsonFileType();

    public static String getDefaultFilename() {
        return "package.json";
    }

    private EsyPackageJsonFileType() {}

    @NotNull
    @Override
    public String getName() {
        return "package";
    }

    @Nls
    @NotNull
    @Override
    public String getDescription() {
        return "Esy package.json.";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "json";
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
