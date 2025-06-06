package com.r2m.projectionsapi.infrastructure.api;

import java.util.Collections;
import java.util.List;

public class ProjectionsResponse {

  private final List<Double> metrics;

  private final String name;

  public ProjectionsResponse( String name) {
    this.metrics = Collections.nCopies(10, Math.random()*1000);
    this.name = name;
  }

  public List<Double> getMetrics() {
    return metrics;
  }

  public String getName() {
    return name;
  }
}
