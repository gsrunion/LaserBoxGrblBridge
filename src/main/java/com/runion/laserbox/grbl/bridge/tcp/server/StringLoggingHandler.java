package com.runion.laserbox.grbl.bridge.tcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class StringLoggingHandler extends MessageToMessageCodec<String, String> {
  @Override
  protected void encode(ChannelHandlerContext ctx, String in, List<Object> out) throws Exception {
    System.out.printf("Grbl Response: %s\n", in);
    out.add(in);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, String in, List<Object> out) throws Exception {
    System.out.printf("Grbl Request: %s\n", in);
    out.add(in);
  }
}
