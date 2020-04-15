package com.reason.ide.facet;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class EsyFacetEditor extends FacetEditorTab {

    private final EsyFacetConfiguration configuration;
    private final FacetEditorContext editorContext;

    private JPanel panel;
    private JCheckBox esyEnabledCheck;

    EsyFacetEditor(@NotNull FacetEditorContext editorContext, @NotNull EsyFacetConfiguration configuration) {
        this.editorContext = editorContext;
        this.configuration = configuration;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Esy";
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        esyEnabledCheck.setSelected(configuration.isEsyEnabled);
        return panel;
    }

    @Override
    public boolean isModified() {
        return configuration.isEsyEnabled != esyEnabledCheck.isSelected();
    }

    @Override
    public void apply() throws ConfigurationException {
        super.apply();
        configuration.isEsyEnabled = esyEnabledCheck.isSelected();
    }
}
