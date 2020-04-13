package com.reason.ide.console;

public interface CliType {

  public static enum Bs implements CliType {
    MAKE,
    CLEAN_MAKE
  }

  public static enum Dune implements CliType {
    BUILD,
    CLEAN,
    INSTALL
  }

  public enum Esy implements CliType {
    INSTALL,
    BUILD,
    SHELL
  }
}
