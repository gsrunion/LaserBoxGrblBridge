package com.runion.laserbox.grbl.bridge.handlers;

import java.util.List;

public interface CommandHandler {
  boolean handlesCommand(Command command);
  String handleCommand(Command command);
}
