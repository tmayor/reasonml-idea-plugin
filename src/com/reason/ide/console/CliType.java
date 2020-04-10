package com.reason.ide.console;

public interface CliType {

  public static enum BuckleScript implements CliType {
    MAKE,
    CLEAN,
    CLEAN_MAKE,
    STANDARD
  }

  public static enum Dune implements CliType {
    MAKE,
    CLEAN,
    CLEAN_MAKE,
    STANDARD
  }

  public enum Esy implements CliType {
    INSTALL,
    BUILD,
    SHELL
  }
}
