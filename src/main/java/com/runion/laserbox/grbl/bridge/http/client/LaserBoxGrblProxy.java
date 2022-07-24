package com.runion.laserbox.grbl.bridge.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runion.laserbox.grbl.bridge.api.GrblProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Takes a list of gcode commands and forwards them on to the laserbox. As multiple gcode commands can be provided
 * in a single line of input, and the LaserBox appears to only be able handle one command at time, when multiple commands
 * show up at once it aggregates the LaserBoxes responses. If any of the responses are not "ok" this aborts and sends
 * no more of the commands. If all the responses are "ok", the status returned to the caller is "ok".
 */
@Component
public class LaserBoxGrblProxy implements GrblProxy {
  private final ObjectMapper objectMapper;
  private final WebClient webClient;
  private final String url;

  public LaserBoxGrblProxy(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.url = "http://201.234.3.1:8080";

    webClient = WebClient
      .builder()
      .baseUrl(url)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
      .build();
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
    if (gcode.startsWith("$")) {
      return "ok";
    }

    AtomicReference<String> gcodeRef = new AtomicReference<>(GCodeCoordinateInverter.invert(GCodeSpaceInserter.insert(gcode)));

    System.out.printf("HTTP Response %s\n", gcodeRef.get());
    String response = webClient.get()
      .uri(builder -> LaserBoxGCodeUrlBuilder.buildUri(builder, gcodeRef.get()))
      .retrieve()
      .bodyToMono(String.class)
      .block();

    String result = objectMapper.readValue(response, LaserBoxResponse.class).result();
    System.out.printf("HTTP Response %s\n", gcodeRef.get());

    return result;
  }

}
