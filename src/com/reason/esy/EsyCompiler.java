package com.reason.esy;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.reason.Log;
import com.reason.Platform;
import com.reason.compiler.Compiler;
import com.reason.compiler.ProcessFinishedListener;
import com.reason.ide.console.CliType;
import com.reason.ide.console.EsyToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EsyCompiler implements Compiler {

    private static final Log LOG = Log.create("esy.compiler");

    @NotNull
    private final Project project;

    public static Compiler getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, EsyCompiler.class);
    }

    EsyCompiler(@NotNull Project project) {
        this.project = project;
        LOG.info("Created esy compiler instance.");
    }

    @Nullable
    @Override
    public VirtualFile findContentRoot() {
        return Platform.findOREsyContentRoot(project);
    }

    @Override
    public void refresh(@NotNull VirtualFile configFile) {
        // Nothing to do
    }

    @Override
    public void run(@NotNull VirtualFile file, @Nullable Compiler.ProcessTerminated onProcessTerminated) {
        run(file, CliType.Esy.BUILD, onProcessTerminated);
    }

    @Override
    public void run(@NotNull VirtualFile file, @NotNull CliType cliType, @Nullable ProcessTerminated onProcessTerminated) {
        if (!(cliType instanceof CliType.Esy)) {
            throw new IllegalArgumentException("Invalid cliType for esy compiler.");
        }
        EsyProcess process = EsyProcess.getInstance(project);
        ProcessHandler processHandler = process.recreate(cliType, onProcessTerminated);
        if (processHandler != null) {
            processHandler.addProcessListener(new ProcessFinishedListener());
            ConsoleView console = getConsoleView();
            console.attachToProcess(processHandler);
            process.startNotify();
        }
    }

    @Override
    public ConsoleView getConsoleView() {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(EsyToolWindowFactory.IDENTIFIER);
        Content windowContent = toolWindow.getContentManager().getContent(0);
        if (windowContent == null) {
            throw new EsyCompilerException("Unable to retrieve content from tool window.");
        }
        SimpleToolWindowPanel component = (SimpleToolWindowPanel) windowContent.getComponent();
        JComponent panelComponent = component.getComponent();
        if (panelComponent == null) {
            throw new EsyCompilerException("Unable to retrieve component from panel.");
        }
        return (ConsoleView) panelComponent.getComponent(0);
    }
}
