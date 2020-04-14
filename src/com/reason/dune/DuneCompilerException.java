package com.reason.dune;

class DuneCompilerException extends RuntimeException {

  public DuneCompilerException(String message) {
    super(message);
  }

  public DuneCompilerException(String message, Throwable cause) {
    super(message, cause);
  }
}
