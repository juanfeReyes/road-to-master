package com.r2m.projectionsapi.infrastructure.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projections")
public class GetProjectionsController {

  private final Map<String, List<ProjectionsResponse>> projections = Map.of(
          "1", List.of(new ProjectionsResponse("Real state")),
          "2", List.of(new ProjectionsResponse("Banking"), new ProjectionsResponse("Oil")),
          "3", List.of(new ProjectionsResponse("Gold"), new ProjectionsResponse("Fashion"), new ProjectionsResponse("Industry"))
  );

  @GetMapping("/user/{userId}")
  public List<ProjectionsResponse> getProjectionsByUser(@PathVariable String userId) throws InterruptedException {
    Thread.sleep(2000);
    return projections.getOrDefault(userId, null);
  }
}
