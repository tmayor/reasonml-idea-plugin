package com.reason.esy;

import com.google.common.collect.ImmutableSet;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.process.*;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.Log;
import com.reason.ORNotification;
import com.reason.compiler.Compiler.ProcessTerminated;
import com.reason.compiler.CompilerProcess;
import com.reason.ide.ORModuleManager;
import com.reason.ide.console.CliType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.intellij.notification.NotificationType.ERROR;
import static com.reason.esy.EsyProcessException.esyNotFoundException;

public class EsyProcess implements CompilerProcess {

  public static final String ESY_EXECUTABLE_NAME = "esy";
  public static final String WINDOWS_EXECUTABLE_SUFFIX =  ".exe";

  @Nls
  private static final Runnable SHOW_ESY_NOT_FOUND_NOTIFICATION =
      () -> Notifications.Bus.notify(new ORNotification("Esy Missing", "Unable to find esy executable in system PATH.", ERROR));

  @Nls
  private static final Runnable SHOW_ESY_PROJECT_NOT_FOUND_NOTIFICATION =
          () -> Notifications.Bus.notify(new ORNotification("Esy Project Not Found", "Unable to find esy project. " +
                  "Have you run esy yet?", ERROR));

  @Nls
  private static final Consumer<Exception> SHOW_EXEC_EXCEPTION_NOTIFICATION =
      (e) -> Notifications.Bus.notify(new ORNotification("Esy Exception", "Failed to execute esy command.\n" + e.getMessage(), ERROR));

  private static final Log LOG = Log.create("esy");

  public static class Command {
    public static final String ESY = "";
    public static final String INSTALL = "install";
    public static final String BUILD = "build";
    public static final String SHELL = "shell";
  }

  public static class EnvironmentVariable {
    public static final String CAML_LD_LIBRARY_PATH = "CAML_LD_LIBRARY_PATH";
    public static final String DUNE_STORE_ORIG_SOURCE_DIR = "DUNE_STORE_ORIG_SOURCE_DIR";
    public static final String ESY__ROOT_PACKAGE_CONFIG_PATH = "ESY__ROOT_PACKAGE_CONFIG_PATH";
    public static final String DUNE_BUILD_DIR = "DUNE_BUILD_DIR";
    public static final String MAN_PATH = "MAN_PATH";
    public static final String OCAMLFIND_DESTDIR = "OCAMLFIND_DESTDIR";
    public static final String OCAMLFIND_LDCONF = "OCAMLFIND_LDCONF";
    public static final String OCAMLLIB = "OCAMLLIB";
    public static final String OCAMLPATH = "OCAMLPATH";
    public static final String PATH = "PATH";
    public static final Set<String> ALL = ImmutableSet.of(
        CAML_LD_LIBRARY_PATH,
        DUNE_STORE_ORIG_SOURCE_DIR,
        ESY__ROOT_PACKAGE_CONFIG_PATH,
        CAML_LD_LIBRARY_PATH,
        DUNE_BUILD_DIR,
        MAN_PATH,
        OCAMLFIND_DESTDIR,
        OCAMLFIND_LDCONF,
        OCAMLLIB,
        OCAMLPATH,
        PATH
    );
  }

  private final Path workingDir;

  private final Path esyExecutable;

  private final boolean redirectErrors;

  private final AtomicBoolean started;

  @Nullable
  private KillableColoredProcessHandler processHandler;

  public static EsyProcess getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, EsyProcess.class);
  }

  private EsyProcess(@NotNull Project project) {
    FileSystem fileSystem = FileSystems.getDefault();
    Optional<VirtualFile> esyContentRoot = findEsyContentRoot(project);
    // @TODO better defaulting for esy root
    String esyRootAsString = esyContentRoot.map(VirtualFile::getPath).orElse("");
    this.workingDir = fileSystem.getPath(esyRootAsString);
    this.esyExecutable = findEsyExecutableInPath();
    this.redirectErrors = true;
    this.started = new AtomicBoolean(false);
  }

  public static Path findEsyExecutableInPath() {
    return findExecutableInPath(ESY_EXECUTABLE_NAME, System.getenv("PATH"))
            .orElseThrow(() -> {
              SHOW_ESY_NOT_FOUND_NOTIFICATION.run();
              return esyNotFoundException();
            });
  }

  public boolean isStarted() {
    return started.get();
  }

  @Override
  public boolean start() {
    if (isStarted()) {
      LOG.warn("Esy process already started.");
    }
    return started.compareAndSet(false, true);
  }

  @Override
  public void terminate() {
    if (!isStarted()) {
      LOG.warn("Esy process already terminated.");
      return;
    }
    started.set(false);
  }

  @Override
  public void startNotify() {
   if (processHandler != null && !processHandler.isStartNotified()) {
      try {
        processHandler.startNotify();
      } catch (Exception e) {
        LOG.error("Exception when calling 'startNotify'.", e);
      }
    }
  }

  @Nullable
  @Override
  public ProcessHandler recreate(@NotNull CliType cliType, @Nullable ProcessTerminated onProcessTerminated) {
    killIt();
    GeneralCommandLine cli = newCommandLine((CliType.Esy) cliType);
    ProcessHandler processHandler;
    try {
      processHandler = new KillableColoredProcessHandler(cli);
    } catch (ExecutionException e) {
      SHOW_EXEC_EXCEPTION_NOTIFICATION.accept(e);
      return null;
    }

    if (onProcessTerminated != null) {
      processHandler.addProcessListener(processTerminatedListener.apply(onProcessTerminated));
    }

    return processHandler;
  }

  private void killIt() {
    if (processHandler == null
        || processHandler.isProcessTerminating()
        || processHandler.isProcessTerminated()) {
      return;
    }
    processHandler.killProcess();
    processHandler = null;
  }

  private GeneralCommandLine newCommandLine(CliType.Esy cliType) {
    GeneralCommandLine commandLine;
    if (SystemInfo.isWindows) {
      commandLine = new GeneralCommandLine("cmd.exe");
      commandLine.addParameter("/c");
    } else {
      commandLine = new GeneralCommandLine("sh");
      commandLine.addParameter("-c");
    }
    commandLine.setWorkDirectory(workingDir.toFile());
    commandLine.setRedirectErrorStream(redirectErrors);
    commandLine.addParameter(esyExecutable + " " + getCommand(cliType)); // 'esy + command' must be a single parameter
    return commandLine;
  }

  private String getCommand(CliType.Esy cliType) {
    switch (cliType) {
      case INSTALL:
        return Command.INSTALL;
      case BUILD:
        return Command.BUILD;
      case SHELL:
        return Command.SHELL;
      default:
        return Command.ESY;
    }
  }

  private static Function<ProcessTerminated, ProcessListener> processTerminatedListener =
          (onProcessTerminated) -> new ProcessAdapter() {
    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      onProcessTerminated.run();
    }
  };

  private static Optional<Path> findExecutableInPath(String filename, String shellPath) {
    if (SystemInfo.isWindows) {
      filename += WINDOWS_EXECUTABLE_SUFFIX;
    }
    File exeFile = PathEnvironmentVariableUtil.findInPath(filename, shellPath, null);
    return exeFile == null ? Optional.empty() : Optional.of(exeFile.toPath());
  }

  private static Optional<VirtualFile> findEsyContentRoot(@NotNull Project project) {
    Optional<VirtualFile> esyContentRoot = ORModuleManager.findFirstEsyContentRoot(project);
    if (!esyContentRoot.isPresent()) {
      SHOW_ESY_NOT_FOUND_NOTIFICATION.run();
      return Optional.empty();
    }
    return esyContentRoot;
  }
}
