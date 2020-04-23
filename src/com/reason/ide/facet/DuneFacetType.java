package com.reason.ide.facet;

import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import icons.ORIcons;

public class DuneFacetType extends FacetType<DuneFacet, DuneFacetConfiguration> {

    public DuneFacetType() {
        super(DuneFacet.ID, DuneFacet.ID_NAME, "Dune/Esy");
    }

    @NotNull
    @Override
    public DuneFacetConfiguration createDefaultConfiguration() {
        return new DuneFacetConfiguration();
    }

    @Nullable
    @Override
    public DuneFacet createFacet(@NotNull Module module, @NotNull String name, @NotNull DuneFacetConfiguration configuration, @Nullable Facet underlyingFacet) {
        return new DuneFacet(this, module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return true;
    }

    @Override
    public Icon getIcon() {
        return ORIcons.DUNE;
    }
}
