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
import com.reason.Compiler;
import com.reason.*;
import com.reason.hints.InsightManager;
import com.reason.ide.console.CliType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DuneCompiler implements Compiler {

    private static final Log LOG = Log.create("dune.compiler");

    @NotNull
    private final Project m_project;

    public static Compiler getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, DuneCompiler.class);
    }

    DuneCompiler(@NotNull Project project) {
        m_project = project;
        LOG.info("Created dune compiler instance.");
    }

    @Nullable
    @Override
    public VirtualFile findContentRoot(@NotNull Project project) {
        return Platform.findORDuneContentRoot(project);
    }

    @Override
    public void refresh(@NotNull VirtualFile configFile) {
        // Nothing to do
    }

    @Override
    public void run(@NotNull VirtualFile file, @NotNull CliType cliType, @Nullable Compiler.ProcessTerminated onProcessTerminated) {
        if (!(cliType instanceof CliType.Dune)) {
            throw new IllegalArgumentException("Invalid cliType for dune compiler.");
        }
        CliType.Dune duneCliType = (CliType.Dune) cliType;
        if (duneCliType == CliType.Dune.CLEAN_MAKE) {
            run(file, CliType.Dune.CLEAN, () ->
                run(file, CliType.Dune.MAKE, onProcessTerminated));
        } else {
            CompilerProcess process = DuneProcess.getInstance(m_project);
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
                    ServiceManager.getService(m_project, InsightManager.class).downloadRincewindIfNeeded(file);
                } else {
                    process.terminate();
                }
            }
        }
    }

    // copied
    @Nullable
    private ConsoleView getConsoleView() {
        ConsoleView console = null;
        ToolWindow window = ToolWindowManager.getInstance(m_project).getToolWindow("Bucklescript");
        Content windowContent = window.getContentManager().getContent(0);
        if (windowContent != null) {
            SimpleToolWindowPanel component = (SimpleToolWindowPanel) windowContent.getComponent();
            JComponent panelComponent = component.getComponent();
            if (panelComponent != null) {
                console = (ConsoleView) panelComponent.getComponent(0);
            }
        }
        return console;
    }
}
