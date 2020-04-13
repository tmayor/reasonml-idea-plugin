package com.reason.ide.console;

import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.actions.ScrollToTheEndToolbarAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.reason.Icons;
import com.reason.ide.facet.BsFacet;
import org.jetbrains.annotations.NotNull;

public class BsToolWindowFactory extends ORToolWindowFactory {

    public static final String IDENTIFIER = "bs-tool-window";

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow window) {
        window.setIcon(Icons.BUCKLESCRIPT_TOOL);
        window.setStripeTitle("BuckleScript");
        window.setTitle("Process");

        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);

        BsConsole console = new BsConsole(project);
        panel.setContent(console.getComponent());

        ActionToolbar toolbar = createToolbar(console);
        panel.setToolbar(toolbar.getComponent());

        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "", true);

        window.getContentManager().addContent(content);

        Disposer.register(project, console);
    }

    @Override
    FacetTypeId<?> getAssociatedFacet() {
        return BsFacet.ID;
    }

    @NotNull
    private ActionToolbar createToolbar(@NotNull BsConsole console) {
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new ScrollToTheEndToolbarAction(console.getEditor()));
        group.add(new Action.ClearLogAction(console));
        group.add(new BsAction.Make());
        group.add(new BsAction.CleanMake());

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("left", group, false);
        toolbar.setTargetComponent(console.getComponent());
        return toolbar;
    }
}
