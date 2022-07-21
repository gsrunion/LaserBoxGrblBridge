package com.runion.laserbox.grbl.bridge.api;

import java.util.List;

public record GrblCommand(String command, List<String> params) {
}
