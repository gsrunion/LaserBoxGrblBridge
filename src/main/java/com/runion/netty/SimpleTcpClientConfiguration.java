package com.runion.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public abstract class SimpleTcpClientConfiguration extends ChannelInitializer<Channel> {
  protected abstract void initChannel(Channel channel);
  public abstract int port();
  public abstract String host();
}
