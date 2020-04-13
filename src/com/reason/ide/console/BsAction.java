package com.reason.ide.console;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class BsAction {

    public static class Make extends CompilerAction {

        public Make() {
            super("Make", "Make", AllIcons.Actions.Compile);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
            CompilerAction.doAction(event, CliType.Bs.MAKE, this);
        }
    }

    public static class CleanMake extends CompilerAction {

        public CleanMake() {
            super("Clean and make world", "Clean and make world", AllIcons.General.Web);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
            CompilerAction.doAction(event, CliType.Bs.CLEAN_MAKE, this);
        }
    }
}
