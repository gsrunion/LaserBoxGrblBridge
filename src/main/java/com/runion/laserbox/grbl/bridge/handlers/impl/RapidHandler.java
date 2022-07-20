package com.runion.laserbox.grbl.bridge.handlers.impl;

import com.runion.laserbox.grbl.bridge.handlers.Command;
import com.runion.laserbox.grbl.bridge.handlers.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class RapidHandler implements CommandHandler {
  @Override
  public boolean handlesCommand(Command command) {
    return switch (command.command()) {
      case "G0", "G00" -> true;
      default -> false;
    };
  }

  @Override
  public String handleCommand(Command command) {
    return "ok";
  }
}
