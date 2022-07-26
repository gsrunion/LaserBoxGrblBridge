package com.runion.laserbox.grbl.bridge.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BackendTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final MockResponse successResponse = new MockResponse()
    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
    .setBody("{\"result\": \"ok\"}");

  private final MockResponse failResponse = new MockResponse()
    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
    .setBody("{\"result\": \"error\"}");

  private LaserBoxGrblProxy sut;
  private MockWebServer server;

  @BeforeEach
  public void setup() throws IOException {
    server = new MockWebServer();
    server.start();
    sut = new LaserBoxGrblProxy(objectMapper).setUrl(server.url("").toString());
  }

  @AfterEach
  public void teardown() throws IOException {
    server.close();
  }

  @Test
  public void sendRequest_withSingleCommand_makesSingleHttpCall() throws JsonProcessingException, InterruptedException {
    server.enqueue(successResponse);

    sut.sendRequest(List.of("G0"));

    RecordedRequest request = server.takeRequest();
    assertEquals(1, server.getRequestCount());
    assertEquals("GET", request.getMethod());
    assertEquals("/cnc/cmd?cmd=G0", request.getPath());
  }

  @Test
  public void sendRequest_withSingleCommand_returnsResultFromHttpResponse() throws JsonProcessingException, InterruptedException {
    server.enqueue(failResponse);

    assertEquals("error", sut.sendRequest(List.of("G0")));
  }

  @Test
  public void sendRequest_withTwoCommandsThatSucceeds_makesTwoHttpCalls() throws JsonProcessingException, InterruptedException {
    server.enqueue(successResponse);
    server.enqueue(successResponse);

    sut.sendRequest(List.of("G0", "M1"));

    assertEquals(2, server.getRequestCount());

    RecordedRequest request = server.takeRequest();
    assertEquals("GET", request.getMethod());
    assertEquals("/cnc/cmd?cmd=G0", request.getPath());

    request = server.takeRequest();
    assertEquals("GET", request.getMethod());
    assertEquals("/cnc/cmd?cmd=M1", request.getPath());
  }

  @Test
  public void sendRequest_withTwoCommandsThatSucceeds_returnsOk() throws JsonProcessingException, InterruptedException {
    server.enqueue(successResponse);
    server.enqueue(successResponse);

    assertEquals("ok", sut.sendRequest(List.of("G0", "M1")));
  }

  @Test
  public void sendRequest_abortsOnFirstNonOkResponse() throws JsonProcessingException, InterruptedException {
    server.enqueue(failResponse);
    server.enqueue(successResponse);

    sut.sendRequest(List.of("G0", "M1"));

    assertEquals(1, server.getRequestCount());

    RecordedRequest request = server.takeRequest();
    assertEquals("GET", request.getMethod());
    assertEquals("/cnc/cmd?cmd=G0", request.getPath());
  }

  @Test
  public void sendRequest_returnsResponseFromFirstFailedHttpReponse() throws JsonProcessingException, InterruptedException {
    server.enqueue(failResponse);
    server.enqueue(successResponse);

    assertEquals("error", sut.sendRequest(List.of("G0", "M1")));
  }

  @Test
  public void sendRequest_withCoordinateParams_thatArePositive_addsNegative() throws JsonProcessingException, InterruptedException {
    server.enqueue(successResponse);

    sut.sendRequest(List.of("G0 X1.111 Y2.222 Z3.333"));

    RecordedRequest request = server.takeRequest();
    assertEquals(1, server.getRequestCount());
    assertEquals("GET", request.getMethod());
    assertEquals("/cnc/cmd?cmd=G0%20X-1.111%20Y-2.222%20Z-3.333", request.getPath());
  }
}
