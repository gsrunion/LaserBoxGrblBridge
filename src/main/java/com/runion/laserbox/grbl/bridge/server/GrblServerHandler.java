package com.runion.laserbox.grbl.bridge.server;

import com.runion.laserbox.grbl.bridge.api.GrblCommand;
import com.runion.laserbox.grbl.bridge.api.GrblCommandProcessor;
import com.runion.laserbox.grbl.bridge.api.GrblException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
class GrblServerHandler extends SimpleChannelInboundHandler<GrblCommand> {
  private final GrblCommandProcessor commandProcessor;

  GrblServerHandler(GrblCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, GrblCommand command) {
    try {
      ctx.writeAndFlush(commandProcessor.process(command));
    } catch (GrblException e) {
      ctx.writeAndFlush(format("error:%s", e.getGrblErrorCode()));
    }
  }
}
