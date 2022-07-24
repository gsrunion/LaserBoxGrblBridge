package com.runion.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleTcpServer {
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;

  private final SimpleTcpServerConfiguration configuration;

  public SimpleTcpServer(SimpleTcpServerConfiguration configuration) {
    this.configuration = configuration;
  }

  public void start() {
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();

    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .childHandler(configuration);

    b.bind(configuration.port());
  }

  public void stop() {
    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
    }

    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
  }
}
