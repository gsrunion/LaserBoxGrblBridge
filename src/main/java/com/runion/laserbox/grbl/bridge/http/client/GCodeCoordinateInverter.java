package com.runion.laserbox.grbl.bridge.http.client;

import java.util.Set;

import static java.lang.String.format;

class GCodeCoordinateInverter {
  private static final Set<String> coordinateParams = Set.of("X", "Y", "Z");

  public static String invert(String gcode) {
    for(String p: coordinateParams) {
      gcode = gcode.replace(p, format("%s-", p));
    }
    gcode = gcode.replaceAll("--", "");
    gcode = gcode.replaceAll("-0", "0");

    System.out.printf(" COORDS %s ->", gcode);
    return gcode;
  }
}
