package com.r2m.portofolioapi.infrastructure.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/portfolio")
public class GetPortfolioController {

  private final Map<String, List<PortfolioResponse>> portfolios = Map.of(
          "1", List.of(new PortfolioResponse("Real state")),
          "2", List.of(new PortfolioResponse("Banking"), new PortfolioResponse("Oil")),
          "3", List.of(new PortfolioResponse("Gold"), new PortfolioResponse("Fashion"), new PortfolioResponse("Industry"))
  );

  @GetMapping("/user/{userId}")
  public List<PortfolioResponse> getPortfoliosByUser(@PathVariable String userId) throws InterruptedException {
    Thread.sleep(800);
    return portfolios.getOrDefault(userId, null);
  }
}
