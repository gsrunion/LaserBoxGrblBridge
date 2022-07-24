package com.runion.laserbox.grbl.bridge.http.client;

import org.springframework.web.util.UriBuilder;

import java.net.URI;

/**
 * Builds url for forwarding gcode to the laserbox
 *
 * Example G0 X1 Y1 Z1 -> /cnc/cmd?cmd=G0%20X1%20Y1%20Z1
 */
class LaserBoxGCodeUrlBuilder {
  public static URI buildUri(UriBuilder builder, String gcode) {
    return builder
      .path("/cnc/cmd")
      .queryParam("cmd", gcode)
      .build();
  }
}
