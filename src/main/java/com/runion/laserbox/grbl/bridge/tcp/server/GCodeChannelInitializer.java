package com.runion.laserbox.grbl.bridge.tcp.server;

import com.runion.laserbox.grbl.bridge.api.GrblProxy;
import com.runion.netty.SimpleTcpServerConfiguration;
import com.runion.netty.handlers.LineFeedAppender;
import io.netty.channel.Channel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

/**
 * Sets up Netty pipeline for receiving, parsing, and forwarding gcode responses on
 * port 23
 */
@Component
public class GCodeChannelInitializer extends SimpleTcpServerConfiguration {
  private final GrblProxy laserBoxHttpClient;

  public GCodeChannelInitializer(GrblProxy laserBoxHttpClient) {
    this.laserBoxHttpClient = laserBoxHttpClient;
  }

  @Override
  protected void initChannel(Channel channel) {
    channel.pipeline().addLast(
      new LineBasedFrameDecoder(256), // Splits input on newlines
      new StringDecoder(), // ByteBuf to String
      new StringEncoder(), // String to ByteBuf
      new LineFeedAppender(), // Append newline
      new StringLoggingHandler(), // Logging
      new GcodeCommandTokenizer(), // Split at G, M, and newline
      new GcodeForwarder(laserBoxHttpClient) // Forward on to LaserBox
    );
  }

  @Override
  public int port() {
    return 23; // LightBurn expects to connect to port 23 of the GRBL implementation
  }
}
