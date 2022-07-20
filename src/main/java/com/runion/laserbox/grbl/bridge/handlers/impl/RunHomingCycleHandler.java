package com.runion.laserbox.grbl.bridge.handlers.impl;

import com.runion.laserbox.grbl.bridge.handlers.Command;
import com.runion.laserbox.grbl.bridge.handlers.CommandHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunHomingCycleHandler implements CommandHandler {
  @Override
  public boolean handlesCommand(Command command) {
    return "$H".equals(command.command());
  }

  @Override
  public String handleCommand(Command command) {
    return "ok";
  }
}
