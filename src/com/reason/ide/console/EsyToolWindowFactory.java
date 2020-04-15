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
import com.reason.ide.facet.EsyFacet;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EsyToolWindowFactory extends ORToolWindowFactory {

    public static final String IDENTIFIER = "Esy:";

    public static final Icon ICON = Icons.ESY_TOOL;

    public static final String TITLE = "Process";

    public static final String STRIPE_TITLE = "Esy";

    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow window) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);

        EsyConsole console = new EsyConsole(project);
        panel.setContent(console.getComponent());

        ActionToolbar toolbar = createToolbar(console);
        panel.setToolbar(toolbar.getComponent());

        Content content = ContentFactory.SERVICE.getInstance()
                .createContent(panel, "", true);

        window.getContentManager().addContent(content);

        Disposer.register(project, console);
    }

    @Override
    FacetTypeId<?> getAssociatedFacet() {
        return EsyFacet.ID;
    }

    @Override
    Icon getIcon() {
        return ICON;
    }

    @Override
    String getTitle() {
        return TITLE;
    }

    @Override
    String getStripeTitle() {
        return STRIPE_TITLE;
    }

    @NotNull
    private ActionToolbar createToolbar(@NotNull EsyConsole console) {
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new ScrollToTheEndToolbarAction(console.getEditor()));
        group.add(new Action.ClearLogAction(console));
        group.add(new EsyAction.Install());
        group.add(new EsyAction.Build());
        group.add(new EsyAction.Shell());

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("left", group, false);
        toolbar.setTargetComponent(console.getComponent());
        return toolbar;
    }
}
