package com.reason.ide.debug;

import org.jetbrains.annotations.NotNull;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;

public class OClModuleBasedConfiguration extends RunConfigurationModule {
    public OClModuleBasedConfiguration(@NotNull Project project) {
        super(project);
    }
}
