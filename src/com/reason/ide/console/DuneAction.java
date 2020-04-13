package com.reason.ide.console;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DuneAction {

    public static class Build extends CompilerAction {

        public Build() {
            super("Build", "Build", AllIcons.Actions.Compile);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
            CompilerAction.doAction(event, CliType.Dune.BUILD, this);
        }
    }

    public static class Clean extends CompilerAction {

        public Clean() {
            super("Clean", "Clean", AllIcons.Actions.Compile);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
            CompilerAction.doAction(event, CliType.Dune.CLEAN, this);
        }
    }

    public static class Install extends CompilerAction {

        public Install() {
            super("Install", "Install", AllIcons.Actions.Compile);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
            CompilerAction.doAction(event, CliType.Dune.INSTALL, this);
        }
    }

    private DuneAction() {}
}
