package com.runion.laserbox.grbl.bridge.api;

public interface GrblCommandProcessor {
  String process(GrblCommand command) throws GrblException;
}
