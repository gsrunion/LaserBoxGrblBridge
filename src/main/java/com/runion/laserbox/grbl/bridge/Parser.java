package com.runion.laserbox.grbl.bridge;

import com.runion.laserbox.grbl.bridge.handlers.Command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
  public static List<Command> parseCommands(String string) {
    List<String> commands = Arrays.stream(string.split("((?=[GM$]))")).collect(Collectors.toList());
    return commands.stream().map(command -> {
      List<String> tokens = Arrays.stream(command.split(" ")).collect(Collectors.toList());
      String head = tokens.remove(0);
      return new Command(head, tokens);
    }).collect(Collectors.toList());
  }
}
