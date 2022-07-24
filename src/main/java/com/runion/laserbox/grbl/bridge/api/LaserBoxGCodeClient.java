package com.runion.laserbox.grbl.bridge.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface LaserBoxGCodeClient {
  String sendRequest(List<String> gcodes) throws JsonProcessingException;
}
