package com.reason.ide.facet;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import com.reason.esy.EsyPackageJson;
import com.reason.ide.files.EsyPackageJsonFileType;
import org.jetbrains.annotations.NotNull;

public class EsyFrameworkDetector extends FacetBasedFrameworkDetector<EsyFacet, EsyFacetConfiguration> {

    protected EsyFrameworkDetector() {
        super(EsyFacet.ID_NAME);
    }

    @NotNull
    @Override
    public FacetType<EsyFacet, EsyFacetConfiguration> getFacetType() {
        return EsyFacet.getFacetType();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return EsyPackageJsonFileType.INSTANCE;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return EsyPackageJson.createFilePattern();
    }
}
