package com.runion.laserbox.grbl.bridge.tcp.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.runion.laserbox.grbl.bridge.api.LaserBoxGCodeClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

class GcodeForwarder extends SimpleChannelInboundHandler<List<String>> {
  private final LaserBoxGCodeClient client;

  GcodeForwarder(LaserBoxGCodeClient client) {
    this.client = client;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, List<String> s) throws JsonProcessingException {
    ctx.writeAndFlush(client.sendRequest(s));
  }
}
