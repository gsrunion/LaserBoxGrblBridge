package com.runion.laserbox.grbl.bridge.tcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Splits incoming gcode line into a list of individual gcode commands
class GcodeCommandTokenizer extends MessageToMessageDecoder<String> {
  @Override
  protected void decode(ChannelHandlerContext ctx, String in, List<Object> out) {
    List<String> commands = new ArrayList<>();
    for(String token: in.split("((?=[GM$]))")) {
      commands.add(token.replace("  ", " ").trim());
    }
    out.add(commands);
  }
}
