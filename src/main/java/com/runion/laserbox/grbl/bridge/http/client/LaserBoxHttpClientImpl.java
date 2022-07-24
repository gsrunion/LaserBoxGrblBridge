package com.runion.laserbox.grbl.bridge.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runion.laserbox.grbl.bridge.api.LaserBoxGCodeClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LaserBoxHttpClientImpl implements LaserBoxGCodeClient {
  private final ObjectMapper objectMapper;
  private final WebClient webClient;
  private final String url;

  public LaserBoxHttpClientImpl(ObjectMapper objectMapper, String url) {
    this.objectMapper = objectMapper;
    this.url = url;

    webClient = WebClient
      .builder()
      .baseUrl(url)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
      .build();
  }

  public LaserBoxHttpClientImpl(ObjectMapper objectMapper) {
    this(objectMapper, "201.234.3.1:8080");
  }

  private static record LaserBoxResponse(String result) {
  }

  @Override
  public String sendRequest(List<String> gcodes) throws JsonProcessingException {
    for (String gcode : gcodes) {
      String result = sendRequest(gcode);
      if (!"ok".equals(result)) {
        return result;
      }
    }
    return "ok";
  }

  private String sendRequest(String gcode) throws JsonProcessingException {
    System.out.printf("START %s ->", gcode);
    if (gcode.startsWith("$")) {
      System.out.printf(" ok END\n");
      return "ok";
    }

    AtomicReference<String> gcodeRef = new AtomicReference<>(GCodeCoordinateInverter.invert(GCodeSpaceInserter.insert(gcode)));

    String response = webClient.get()
      .uri(builder -> LaserBoxUrlBuilder.buildUri(builder, gcodeRef.get()))
      .retrieve()
      .bodyToMono(String.class)
      .block();

    String result = objectMapper.readValue(response, LaserBoxResponse.class).result();

    System.out.printf(" %s END\n", result);

    return result;
  }
}
