package com.runion.laserbox.grbl.bridge.server;

import com.runion.laserbox.grbl.bridge.api.GrblCommandProcessor;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.mock;

class GrblServerTest {
  private GrblCommandProcessor mockCommandProcessor;
  private GrblServer server;

  @BeforeEach
  public void setup() {
    mockCommandProcessor = mock(GrblCommandProcessor.class);
    GrblServerHandler serverHandler = new GrblServerHandler(mockCommandProcessor);
    GrblChannelInitializer channelInitializer = new GrblChannelInitializer(serverHandler);
    server = new GrblServer(channelInitializer);
    server.start();
  }

  @AfterEach
  public void tearDown() {
    server.stop();
  }

  @Test
  public void standupAndTearDownCleanUp() {

  }
}
