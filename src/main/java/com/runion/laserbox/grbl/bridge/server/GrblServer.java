package com.runion.laserbox.grbl.bridge.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class GrblServer {
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;

  private final GrblChannelInitializer channelInitializer;

  public GrblServer(GrblChannelInitializer channelInitializer) {
    this.channelInitializer = channelInitializer;
  }

  @PostConstruct
  void start() {
    System.out.println("Starting Server");
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();

    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .childHandler(channelInitializer);
    b.bind(NetworkConstants.INBOUND_PORT);
  }

  @PreDestroy
  void stop() {
    System.out.println("Stopping Server");

    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
    }

    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
  }

}
