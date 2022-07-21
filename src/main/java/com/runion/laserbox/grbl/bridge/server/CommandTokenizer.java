package com.runion.laserbox.grbl.bridge.server;

import com.runion.laserbox.grbl.bridge.api.GrblCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class CommandTokenizer extends MessageToMessageDecoder<String> {
  @Override
  protected void decode(ChannelHandlerContext ctx, String in, List out) throws Exception {
    split(in, "((?=[GM$]))").forEach(command -> {
      List<String> tokens = split(command, " ");
      out.add(new GrblCommand(tokens.remove(0), tokens));
    });
  }

  private List<String> split(String in, String regex) {
    return Arrays.stream(in.split(regex)).collect(Collectors.toList());
  }
}
