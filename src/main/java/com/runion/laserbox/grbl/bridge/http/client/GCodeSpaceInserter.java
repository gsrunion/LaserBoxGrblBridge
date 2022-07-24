package com.runion.laserbox.grbl.bridge.http.client;

import java.util.Set;

class GCodeSpaceInserter {
  private static final Set<String> params = Set.of("X", "Y", "Z", "F", "P", "S");

  public static String insert(String gcode) {
    for(String param: params) {
      gcode = gcode.replaceAll(param, " " + param);
    }
    gcode = gcode.replaceAll("  ", " ");
    gcode = gcode.replaceAll("  ", " ");

    System.out.printf(" SPACES %s ->", gcode);
    return gcode;
  }
}
