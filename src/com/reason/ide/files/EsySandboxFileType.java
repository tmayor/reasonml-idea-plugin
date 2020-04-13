package com.reason.ide.files;

import com.intellij.openapi.fileTypes.DirectoryFileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EsySandboxFileType implements DirectoryFileType {

    public static final EsySandboxFileType INSTANCE = new EsySandboxFileType();

    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Esy Sandbox";
    }

    @Nls
    @NotNull
    @Override
    public String getDescription() {
        return "Esy sandbox directory";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.ESY;
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
