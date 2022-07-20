package com.runion.laserbox.grbl.bridge;

import com.runion.laserbox.grbl.bridge.handlers.CommandHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

import static java.lang.String.format;

@Component
public class Server {
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;

  private final List<CommandHandler> commandHandlers;

  public Server(List<CommandHandler> commandHandlers) {
    this.commandHandlers = commandHandlers;
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
          //channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
          channel.pipeline().addLast(new LineBasedFrameDecoder(256));
          channel.pipeline().addLast(new StringDecoder());
          channel.pipeline().addLast(new StringEncoder());
          channel.pipeline().addLast(new MessageToMessageEncoder<String>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, String in, List<Object> out) {
             out.add(format("%s\n", in));
            }
          });

          channel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String in) {
              System.out.println(in);

              Parser.parseCommands(in).forEach(command -> {
                commandHandlers
                  .stream()
                  .filter(ch -> ch.handlesCommand(command))
                  .findFirst()
                  .ifPresentOrElse(commandHandler -> {
                    ctx.writeAndFlush(commandHandler.handleCommand(command));
                  }, () -> {
                    System.out.printf("Unrecognized Command: %s\n", command);
                    ctx.writeAndFlush("ok");
                  });
              });
            }
          });
        }
      });

    b.bind(23);
  }

  @PreDestroy
  private void stop() {
    System.out.println("Stopping Server");

    if(bossGroup != null) {
      bossGroup.shutdownGracefully();
    }

    if(workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
  }

}
