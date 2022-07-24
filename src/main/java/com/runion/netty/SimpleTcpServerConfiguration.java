package com.runion.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public abstract class SimpleTcpServerConfiguration extends ChannelInitializer<Channel> {
  protected abstract void initChannel(Channel channel);
  public abstract int port();
}
