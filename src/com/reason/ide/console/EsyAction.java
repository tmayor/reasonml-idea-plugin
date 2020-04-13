package com.reason.ide.console;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class EsyAction {

    public static class Install extends CompilerAction {

        public Install() {
            super("Install", "Install Dependencies", AllIcons.Actions.Install);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            CompilerAction.doAction(e, CliType.Esy.INSTALL, this);
        }
    }

    public static class Build extends CompilerAction {

        public Build() {
            super("Build", "Build Project", AllIcons.Actions.Compile);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            CompilerAction.doAction(e, CliType.Esy.BUILD, this);
        }
    }

    public static class Shell extends CompilerAction {

        public Shell() {
            super("Shell", "Launch Esy Shell", AllIcons.Actions.Execute);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            CompilerAction.doAction(e, CliType.Esy.SHELL, this);
        }
    }

    private EsyAction() {}
}
