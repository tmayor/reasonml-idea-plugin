package com.reason.ide.console;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

class DuneConsole extends ConsoleViewImpl {

    DuneConsole(@NotNull Project project) {
        super(project, true);
    }
}
