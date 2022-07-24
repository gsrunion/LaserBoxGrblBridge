package com.runion.laserbox.grbl.bridge.tcp.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.runion.laserbox.grbl.bridge.api.GrblProxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrontendTest {
  private String input = "473231204735340a4739300a4731202058302059302046363030302053300a4739300a4d320a";
  private byte bytes[] = ByteBufUtil.decodeHexDump(input);
  private ByteBuf buffer = Unpooled.wrappedBuffer(bytes);

  @Mock
  private GrblProxy client;

  @Captor
  private ArgumentCaptor<List<String>> inputCaptor;

  @InjectMocks
  private GCodeChannelInitializer initializer;

  private EmbeddedChannel embeddedChannel = new EmbeddedChannel(initializer);

  @BeforeEach
  public void setup() {
    embeddedChannel = new EmbeddedChannel(initializer);
  }

  @Test
  public void pipeline_tokenizes_input() throws JsonProcessingException {
    when(client.sendRequest(anyList())).thenReturn("ok");

    embeddedChannel.writeInbound(buffer);

    verify(client, times(5)).sendRequest(inputCaptor.capture());
    List<List<String>> values = inputCaptor.getAllValues();
    assertEquals("G21", values.get(0).get(0));
    assertEquals("G54", values.get(0).get(1));
    assertEquals("G90", values.get(1).get(0));
    assertEquals("G1 X0 Y0 F6000 S0", values.get(2).get(0));
    assertEquals("G90", values.get(3).get(0));
    assertEquals("M2", values.get(4).get(0));
  }
}
