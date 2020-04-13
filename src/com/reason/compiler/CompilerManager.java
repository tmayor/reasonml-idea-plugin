package com.reason.compiler;

import com.intellij.facet.FacetManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.bs.Bucklescript;
import com.reason.dune.DuneCompiler;
import com.reason.ide.ORNotification;
import com.reason.ide.facet.DuneFacet;
import org.jetbrains.annotations.NotNull;

import static com.intellij.notification.NotificationListener.URL_OPENING_LISTENER;
import static com.intellij.notification.NotificationType.ERROR;

@Service
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
    public Compiler getCompiler(VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);
        return CompilerProvider.getInstance(module);
    }
}
