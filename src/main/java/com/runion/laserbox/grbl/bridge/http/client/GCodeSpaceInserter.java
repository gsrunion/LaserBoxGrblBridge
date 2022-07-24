package com.runion.laserbox.grbl.bridge.http.client;

import java.util.Set;

/**
 * Not strictly needed but inserts spaces in between gcode parameters that are smushed together.
 * Example G0 X1Y2Z3 -> G0 X1 Y2 Z3
 */
class GCodeSpaceInserter {
  private static final Set<String> params = Set.of("X", "Y", "Z", "F", "P", "S");

  public static String insert(String gcode) {
    for(String param: params) {
      gcode = gcode.replaceAll(param, " " + param);
    }
    gcode = gcode.replaceAll("  ", " ");
    gcode = gcode.replaceAll("  ", " ");

    return gcode;
  }
}
