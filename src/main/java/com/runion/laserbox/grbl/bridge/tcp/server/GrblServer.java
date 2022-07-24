package com.runion.laserbox.grbl.bridge.tcp.server;

import com.runion.netty.SimpleTcpServer;
import com.runion.netty.SimpleTcpServerConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class GrblServer extends SimpleTcpServer {
  public GrblServer(SimpleTcpServerConfiguration configuration) {
    super(configuration);
  }

  @PostConstruct
  @Override
  public void start() {
    super.start();
  }

  @PreDestroy
  @Override
  public void stop() {
    super.stop();
  }
}
