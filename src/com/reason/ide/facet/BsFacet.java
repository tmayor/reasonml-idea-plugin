package com.reason.ide.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;

public class BsFacet extends Facet<BsFacetConfiguration> {

    public static final String ID_NAME = "bs";

    public static final FacetTypeId<BsFacet> ID = new FacetTypeId<>(ID_NAME);

    BsFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name,
            @NotNull BsFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    @NotNull
    static FacetType<BsFacet, BsFacetConfiguration> getFacetType() {
        return FacetTypeRegistry.getInstance().findFacetType(ID);
    }

    @NotNull
    @Override
    public String toString() {
        return getModule().getName();
    }
}
