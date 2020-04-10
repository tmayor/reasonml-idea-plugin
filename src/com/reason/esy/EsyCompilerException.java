package com.reason.esy;

class EsyCompilerException extends RuntimeException {

  public EsyCompilerException(String message) {
    super(message);
  }

  public EsyCompilerException(String message, Throwable cause) {
    super(message, cause);
  }
}
