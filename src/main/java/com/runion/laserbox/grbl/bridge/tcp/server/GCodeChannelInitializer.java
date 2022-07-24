package com.runion.laserbox.grbl.bridge.tcp.server;

import com.runion.laserbox.grbl.bridge.api.LaserBoxGCodeClient;
import com.runion.netty.SimpleTcpServerConfiguration;
import com.runion.netty.handlers.LineFeedAppender;
import io.netty.channel.Channel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

@Component
public class GCodeChannelInitializer extends SimpleTcpServerConfiguration {
  private final LaserBoxGCodeClient laserBoxHttpClient;

  public GCodeChannelInitializer(LaserBoxGCodeClient laserBoxHttpClient) {
    this.laserBoxHttpClient = laserBoxHttpClient;
  }

  @Override
  protected void initChannel(Channel channel) {
    channel.pipeline().addLast(
      new LoggingHandler(LogLevel.ERROR), // Logging all traffic
      new LineBasedFrameDecoder(256),
      new StringDecoder(), // ByteBuf to String
      new StringEncoder(), // String to ByteBuf
      new GcodeCommandTokenizer(), // Split at G, M, and newline
      new LineFeedAppender(), // Append newline
      new GcodeForwarder(laserBoxHttpClient) // Forward on to LaserBox
    );
  }

  @Override
  public int port() {
    return 23;
  }
}
