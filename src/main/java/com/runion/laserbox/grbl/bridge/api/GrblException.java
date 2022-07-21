package com.runion.laserbox.grbl.bridge.api;

public class GrblException extends Exception {
  private final String grblErrorCode;

  public GrblException(String grblErrorCode, String message) {
    super(message);
    this.grblErrorCode = grblErrorCode;
  }

  public String getGrblErrorCode() {
    return grblErrorCode;
  }
}
