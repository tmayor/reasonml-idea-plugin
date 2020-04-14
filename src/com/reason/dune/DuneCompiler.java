package com.reason.dune;

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
import com.reason.compiler.CompilerProcess;
import com.reason.compiler.ProcessFinishedListener;
import com.reason.hints.InsightManager;
import com.reason.ide.console.CliType;
import com.reason.ide.console.DuneToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DuneCompiler implements Compiler {

    private static final Log LOG = Log.create("dune.compiler");

    @NotNull
    private final Project project;

    public static Compiler getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, DuneCompiler.class);
    }

    DuneCompiler(@NotNull Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public VirtualFile findContentRoot() {
        return Platform.findORDuneContentRoot(project);
    }

    @Override
    public void refresh(@NotNull VirtualFile configFile) {
        // Nothing to do
    }

    @Override
    public void run(@NotNull VirtualFile file, @Nullable Compiler.ProcessTerminated onProcessTerminated) {
        run(file, CliType.Dune.BUILD, onProcessTerminated);
    }

    @Override
    public void run(@NotNull VirtualFile file, @NotNull CliType cliType, @Nullable Compiler.ProcessTerminated onProcessTerminated) {
        CompilerProcess process = DuneProcess.getInstance(project);
        if (process.start()) {
            ProcessHandler duneHandler = process.recreate(cliType, onProcessTerminated);
            if (duneHandler != null) {
                ConsoleView console = getConsoleView();
                if (console != null) {
                    long start = System.currentTimeMillis();
                    console.attachToProcess(duneHandler);
                    duneHandler.addProcessListener(new ProcessFinishedListener(start));
                }
                process.startNotify();
                ServiceManager.getService(project, InsightManager.class).downloadRincewindIfNeeded(file);
            } else {
                process.terminate();
            }
        }
    }

    @Nullable
    @Override
    public ConsoleView getConsoleView() {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(DuneToolWindowFactory.IDENTIFIER);
        Content windowContent = toolWindow.getContentManager().getContent(0);
        if (windowContent == null) {
            throw new DuneCompilerException("Unable to retrieve content from tool window.");
        }
        SimpleToolWindowPanel component = (SimpleToolWindowPanel) windowContent.getComponent();
        JComponent panelComponent = component.getComponent();
        if (panelComponent == null) {
            throw new DuneCompilerException("Unable to retrieve component from panel.");
        }
        return (ConsoleView) panelComponent.getComponent(0);
    }
}
