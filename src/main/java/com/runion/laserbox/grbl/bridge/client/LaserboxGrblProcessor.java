package com.runion.laserbox.grbl.bridge.client;

import com.runion.laserbox.grbl.bridge.api.GrblCommand;
import com.runion.laserbox.grbl.bridge.api.GrblCommandProcessor;
import com.runion.laserbox.grbl.bridge.api.GrblException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LaserboxGrblProcessor implements GrblCommandProcessor {
  @PostConstruct
  @Override
  public String process(GrblCommand command) throws GrblException {
    switch(command.command()) {
      case "G0", "G00" -> handleRapid(command);
      case "G1" -> handleLinearCut(command);
//      M34 S0
//      M19 S1
//      M18 S0
//      M104 S0
//      M34 S0
//      M32 P10
//      M33 P10
//      G4 P0.1
//      M34 S0
//      M6 P1
    }
  }

  private void handleLaserPower(GrblCommand command) {
    //M4 S0 laser off
    //M4 S500 laser at 50%
    //M4 S0 laser off
    //M4 S0 laser off
    // M4 S500 laser at 50%
  }

  private void handleLinearCut(GrblCommand command) {
// G1 F1000
//    G1 X-28.153 Y-43.991
//    G1 X-51.603 Y-43.991
//    G1 X-51.603 Y-22.541
//    G1 X-28.153 Y-22.541
//    G1 F240 -> 40 mm/s
  }

  private void handleRapid(GrblCommand command) {
//    G0 Z-16.00
//    G0 F10000
//    G4 P0.1
//    G0 F8000
//    G0 X-28.153 Y-22.541
//    G0 X-28.153 Y-22.541
//    G0
//    G0 F10000
  }
}
