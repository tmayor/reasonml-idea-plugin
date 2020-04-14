package com.reason.compiler;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.ide.console.CliType;
import org.jetbrains.annotations.NotNull;

public class CompilerManager {

    @NotNull
    private final Project project;

    public static CompilerManager getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, CompilerManager.class);
    }

    CompilerManager(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public Compiler getCompiler(CliType cliType) {
        return CompilerProvider.getInstance(project, cliType);
    }

    @NotNull
    public Compiler getCompiler(VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);
        return getCompiler(module);
    }

    @NotNull
    public Compiler getCompiler(Module module) {
        return CompilerProvider.getInstance(module);
    }
}
