package com.runion.laserbox.grbl.bridge.http.client;

import java.util.Set;

import static java.lang.String.format;

/**
 * LaserBox deals in negative coordinates with the origin being the top left, everything to the right and down
 * from that are negative. LightBurn deals with positive values from this point. This class flips the sign on
 * any X|Y|Z gcode parameters that are non-zero.
 *
 * Example G0 X1Y2Z3 -> G0 X-1 Y-2 Z-3
 */
class GCodeCoordinateInverter {
  private static final Set<String> coordinateParams = Set.of("X", "Y", "Z");

  public static String invert(String gcode) {
    for(String p: coordinateParams) {
      gcode = gcode.replace(p, format("%s-", p));
    }
    gcode = gcode.replaceAll("--", "");
    gcode = gcode.replaceAll("-0", "0");

    return gcode;
  }
}
