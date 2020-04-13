package com.reason.ide.facet;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import com.reason.Platform;
import com.reason.ide.files.EsySandboxFileType;
import org.jetbrains.annotations.NotNull;

public class EsyFrameworkDetector extends FacetBasedFrameworkDetector<EsyFacet, EsyFacetConfiguration> {

    protected EsyFrameworkDetector() {
        super(DuneFacet.ID_NAME);
    }

    @NotNull
    @Override
    public FacetType<EsyFacet, EsyFacetConfiguration> getFacetType() {
        return EsyFacet.getFacetType();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return EsySandboxFileType.INSTANCE;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
       return FileContentPattern.fileContent().withName(Platform.ESY_PROJECT_IDENTIFIER_FILE);
    }
}
