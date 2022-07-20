package com.runion.laserbox.grbl.bridge.handlers.impl;

import com.runion.laserbox.grbl.bridge.handlers.Command;
import com.runion.laserbox.grbl.bridge.handlers.CommandHandler;

public class XYPlaneSelectedHandler implements CommandHandler {
  @Override
  public boolean handlesCommand(Command command) {
    return "G17".equals(command.command());
  }

  @Override
  public String handleCommand(Command command) {
    return "ok";
  }
}
