package com.runion.laserbox.grbl.bridge.handlers;

import java.util.List;

public record Command(String command, List<String> params) {
}
