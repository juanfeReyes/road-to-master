package com.r2m.apicomposer.infrastructure.services.projection;

import com.r2m.apicomposer.infrastructure.services.portfolio.PortfolioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProjectionService {

  private final RestTemplate restTemplate;

  @Value("${services.projection}")
  private String projectionUrl;

  @Autowired
  public ProjectionService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Async
  public CompletableFuture<List<ProjectionsResponse>> getProjectionByUserId(String userId){
    return CompletableFuture.supplyAsync(() -> restTemplate.exchange(
            projectionUrl+"/user/"+userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProjectionsResponse>>(){}).getBody());
  }
}
