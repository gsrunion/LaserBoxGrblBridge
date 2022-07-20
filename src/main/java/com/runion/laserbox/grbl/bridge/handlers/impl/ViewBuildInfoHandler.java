package com.runion.laserbox.grbl.bridge.handlers.impl;

import com.runion.laserbox.grbl.bridge.handlers.Command;
import com.runion.laserbox.grbl.bridge.handlers.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class ViewBuildInfoHandler implements CommandHandler {
  @Override
  public boolean handlesCommand(Command command) {
    return "$I".equals(command.command());
  }

  @Override
  public String handleCommand(Command command) {
    return "ok";
  }
}
