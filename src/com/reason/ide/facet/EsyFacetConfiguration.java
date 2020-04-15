package com.reason.ide.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.reason.ide.ORModuleManager;
import org.jetbrains.annotations.NotNull;

@State(name = "EsyFacetConfiguration", storages = {@Storage("ocaml-dune.xml")})
public class EsyFacetConfiguration implements FacetConfiguration, PersistentStateComponent<EsyFacetConfiguration> {

    public boolean isEsyEnabled = true;

    @NotNull
    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        if (editorContext.isNewFacet()) {
            isEsyEnabled = ORModuleManager.isEsyModule(editorContext.getModule());
        }
        return new FacetEditorTab[]{new EsyFacetEditor(editorContext, this)};
    }

    @Override
    public EsyFacetConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull EsyFacetConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
