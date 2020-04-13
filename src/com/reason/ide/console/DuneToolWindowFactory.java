package com.reason.ide.console;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.actions.ScrollToTheEndToolbarAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class DuneToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow duneWindow) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);

        DuneConsole console = new DuneConsole(project);
        panel.setContent(console.getComponent());

        ActionToolbar toolbar = createToolbar(console);
        panel.setToolbar(toolbar.getComponent());

        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "", true);

        duneWindow.getContentManager().addContent(content);

        Disposer.register(project, console);
    }

    @NotNull
    private ActionToolbar createToolbar(@NotNull DuneConsole console) {
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new ScrollToTheEndToolbarAction(console.getEditor()));
        group.add(new Action.ClearLogAction(console));
        group.add(new DuneAction.Install());
        group.add(new DuneAction.Build());
        group.add(new DuneAction.Clean());

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("left", group, false);
        toolbar.setTargetComponent(console.getComponent());

        return toolbar;
    }
}
