package com.runion.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

// expected to be used for end to end tests later.
public class SimpleTcpClient {
  private final SimpleTcpClientConfiguration configuration;
  private EventLoopGroup group;

  public SimpleTcpClient(SimpleTcpClientConfiguration configuration) {
    this.configuration = configuration;
  }

  void start() {
    group = new NioEventLoopGroup();

    Bootstrap b = new Bootstrap();
    b.group(group)
      .channel(NioSocketChannel.class)
      .option(ChannelOption.TCP_NODELAY, true)
      .handler(configuration);

    b.connect(configuration.host(), configuration.port());
  }

  void stop() {
    group.shutdownGracefully();
  }
}
