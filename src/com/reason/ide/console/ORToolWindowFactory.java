package com.reason.ide.console;

import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

abstract class ORToolWindowFactory implements ToolWindowFactory, DumbAware {

  public abstract void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow window);

  abstract FacetTypeId<?> getAssociatedFacet();

  @Override
  public boolean shouldBeAvailable(@NotNull Project project) {
    ModuleManager moduleManager = ModuleManager.getInstance(project);
    return Stream.of(moduleManager.getModules())
        .map(FacetManager::getInstance)
        .map(facetManager -> facetManager.getFacetByType(getAssociatedFacet()))
        .anyMatch(Objects::nonNull);
  }
}

