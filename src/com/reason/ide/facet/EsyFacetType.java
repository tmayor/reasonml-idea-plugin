package com.reason.ide.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.reason.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EsyFacetType extends FacetType<EsyFacet, EsyFacetConfiguration> {

    public EsyFacetType() {
        super(EsyFacet.ID, EsyFacet.ID_NAME, "Esy");
    }

    @NotNull
    @Override
    public EsyFacetConfiguration createDefaultConfiguration() {
        return new EsyFacetConfiguration();
    }

    @Nullable
    @Override
    public EsyFacet createFacet(@NotNull Module module, @NotNull String name,
            @NotNull EsyFacetConfiguration configuration, @Nullable Facet underlyingFacet) {
        return new EsyFacet(this, module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return true;
    }

    @Override
    public Icon getIcon() {
        return Icons.ESY;
    }
}
