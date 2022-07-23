package com.runion.laserbox.grbl.bridge.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

@Component
public class GrblChannelInitializer extends ChannelInitializer<Channel> {
  private final GrblServerHandler handler;

  public GrblChannelInitializer(GrblServerHandler handler) {
    this.handler = handler;
  }

  @Override
  protected void initChannel(Channel channel) {
    channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO)); // logging
    channel.pipeline().addLast(new LineBasedFrameDecoder(256)); // break input up by lines
    channel.pipeline().addLast(new StringDecoder()); // convert inbound ByteBuf to Strings
    channel.pipeline().addLast(new StringEncoder()); // convert outbound Strings to ByteBuf
    channel.pipeline().addLast(new LineFeedAppender()); // adds newline to end of output strings
    channel.pipeline().addLast(new CommandTokenizer()); // parses inbound into GrblCommand
    channel.pipeline().addLast(handler);
  }
}
