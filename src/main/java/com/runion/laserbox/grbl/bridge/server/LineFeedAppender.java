package com.runion.laserbox.grbl.bridge.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static java.lang.String.format;

class LineFeedAppender extends MessageToMessageEncoder<String> {
  @Override
  protected void encode(ChannelHandlerContext ctx, String in, List<Object> out) {
    out.add(format("%s\n", in));
  }
}
