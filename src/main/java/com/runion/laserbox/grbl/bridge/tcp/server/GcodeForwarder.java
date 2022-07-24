package com.runion.laserbox.grbl.bridge.tcp.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.runion.laserbox.grbl.bridge.api.GrblProxy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * Takes the parsed list of gcode commands and hands them off to whichever
 * LaserBoxGCodeClient is instantiated
 */
class GcodeForwarder extends SimpleChannelInboundHandler<List<String>> {
  private final GrblProxy client;

  GcodeForwarder(GrblProxy client) {
    this.client = client;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, List<String> s) throws JsonProcessingException {
    ctx.writeAndFlush(client.sendRequest(s));
  }
}
