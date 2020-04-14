package com.reason.compiler;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.ide.console.CliType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Compiler {

    @FunctionalInterface
    interface ProcessTerminated {
        void run();
    }

    @Nullable
    ConsoleView getConsoleView();

    @Nullable
    VirtualFile findContentRoot();

    void refresh(@NotNull VirtualFile configFile);

    /* runs whenever the file is modified. should run `build`, `make`, or similar */
    void run(@NotNull VirtualFile file, @Nullable ProcessTerminated onProcessTerminated);

    /* runs the compiler with a command specified by `cliType` */
    void run(@NotNull VirtualFile file, @NotNull CliType cliType, @Nullable ProcessTerminated onProcessTerminated);

}
