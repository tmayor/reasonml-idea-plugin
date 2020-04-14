package com.reason.ide.facet;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import com.reason.ide.files.BsPackageJsonFileType;
import org.jetbrains.annotations.NotNull;

public class BsFrameworkDetector extends FacetBasedFrameworkDetector<BsFacet, BsFacetConfiguration> {

    protected BsFrameworkDetector() {
        super(BsFacet.ID_NAME);
    }

    @NotNull
    @Override
    public FacetType<BsFacet, BsFacetConfiguration> getFacetType() {
        return BsFacet.getFacetType();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return BsPackageJsonFileType.INSTANCE;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent().withName("");
    }
}
