package com.reason.ide.facet;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class BsFacetEditor extends FacetEditorTab {

    private final BsFacetConfiguration configuration;
    private final FacetEditorContext editorContext;

    private JPanel panel;

    BsFacetEditor(@NotNull FacetEditorContext editorContext, @NotNull BsFacetConfiguration configuration) {
        this.editorContext = editorContext;
        this.configuration = configuration;
    }

    @NotNull
    @Nls
    @Override
    public String getDisplayName() {
        return "BuckleScript";
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        super.apply();
    }
}
