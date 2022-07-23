package com.runion.laserbox.grbl.bridge.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.LinkedList;
import java.util.Queue;

public final class TestTcpClient implements AutoCloseable {
  private final EventLoopGroup group = new NioEventLoopGroup();
  private final Queue<String> responseQueue = new LinkedList<>();

  public TestTcpClient(String ip, int port) {
    Bootstrap b = new Bootstrap();
    b.group(group)
      .channel(NioSocketChannel.class)
      .option(ChannelOption.TCP_NODELAY, true)
      .handler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
          channel.p
        }
      });
    ChannelFuture f = b.connect(ip, port);
  }

  @Override
  public void close() {
    group.shutdownGracefully();
  }
}
