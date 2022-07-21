package com.runion.laserbox.grbl.bridge.server;

import com.runion.laserbox.grbl.bridge.api.GrblCommand;
import com.runion.laserbox.grbl.bridge.api.GrblCommandProcessor;
import com.runion.laserbox.grbl.bridge.api.GrblException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static java.lang.String.format;

@Component
public class GrblServer {
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;

  private final GrblCommandProcessor commandProcessor;

  public GrblServer(GrblCommandProcessor commandProcessor) {
    this.commandProcessor = commandProcessor;
  }

  @PostConstruct
  private void start() {
    System.out.println("Starting Server");
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();

    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel channel) {
          channel.pipeline().addLast(new LineBasedFrameDecoder(256));
          channel.pipeline().addLast(new StringDecoder());
          channel.pipeline().addLast(new StringEncoder());
          channel.pipeline().addLast(new LineFeedAppender());
          channel.pipeline().addLast(new CommandTokenizer());
          channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
          channel.pipeline().addLast(new SimpleChannelInboundHandler<GrblCommand>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, GrblCommand command) {
              try {
                ctx.writeAndFlush(commandProcessor.process(command));
              } catch (GrblException e) {
                ctx.writeAndFlush(format("error:%s", e.getGrblErrorCode()));
              }
            }
          });
        }
      });

    b.bind(23);
  }

  @PreDestroy
  private void stop() {
    System.out.println("Stopping Server");

    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
    }

    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
  }
}
