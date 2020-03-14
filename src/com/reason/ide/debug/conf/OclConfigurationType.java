package com.reason.ide.debug.conf;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.SimpleConfigurationType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyValue;
import com.reason.Icons;
import org.jetbrains.annotations.NotNull;

public class OclConfigurationType extends SimpleConfigurationType implements DumbAware {
    protected OclConfigurationType() {
        super("ocaml.build_tool", "Ocaml application", null, NotNullLazyValue.createValue(() -> {
            return Icons.OCL_FILE_MODULE;
        }));
    }

    @NotNull
    @Override
    public String getTag() {
        return "ocaml";
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new OclRunConfiguration(project, this, "ocaml");
    }
}
