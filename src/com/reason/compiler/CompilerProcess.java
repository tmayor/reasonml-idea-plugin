package com.reason.compiler;

import com.intellij.execution.process.ProcessHandler;
import com.reason.ide.console.CliType;

public interface CompilerProcess {

    boolean start();

    void startNotify();

    ProcessHandler recreate(CliType cliType, Compiler.ProcessTerminated onProcessTerminated);

    void terminate();

}
