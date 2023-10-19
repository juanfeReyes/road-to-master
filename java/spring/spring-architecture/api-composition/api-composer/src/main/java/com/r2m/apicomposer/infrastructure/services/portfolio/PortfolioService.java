package com.r2m.apicomposer.infrastructure.services.portfolio;

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
public class PortfolioService {

  private final RestTemplate restTemplate;

  @Value("${services.portfolio}")
  private String portfolioApiUrl;

  @Autowired
  public PortfolioService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Async
  public CompletableFuture<List<PortfolioResponse>> getPortfolioByUserId(String userId){

    return CompletableFuture.supplyAsync(() -> restTemplate.exchange(
            portfolioApiUrl+"/user/"+userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<PortfolioResponse>>(){}).getBody());
  }
}
