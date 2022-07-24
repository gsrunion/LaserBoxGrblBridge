package com.runion.laserbox.grbl.bridge.http.client;

import org.springframework.web.util.UriBuilder;

import java.net.URI;

class LaserBoxUrlBuilder {
  private static final String PATH = "/cnc/cmd";

  public static URI buildUri(UriBuilder builder, String gcode) {
    return builder
      .path(PATH)
      .queryParam("cmd", gcode)
      .build();
  }
}
