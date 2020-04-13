package com.reason.ide.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.module.Module;
import com.reason.Log;
import com.reason.esy.EsyProcess;
import org.jetbrains.annotations.NotNull;

public class EsyFacet extends Facet<EsyFacetConfiguration> {

    public static final String ID_NAME = "esy";

    public static final FacetTypeId<EsyFacet> ID = new FacetTypeId<>(ID_NAME);

    private static final Log LOG = Log.create("facet.esy");

    EsyFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name,
            @NotNull EsyFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    @NotNull
    static FacetType<EsyFacet, EsyFacetConfiguration> getFacetType() {
        return FacetTypeRegistry.getInstance().findFacetType(ID);
    }

    @NotNull
    @Override
    public String toString() {
        return getModule().getName();
    }

    public boolean isSetupValid() {
        try {
            return EsyProcess.findEsyExecutableInPath() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
