package com.r2m.apicomposer.infrastructure.services.user;

import com.r2m.apicomposer.infrastructure.services.projection.ProjectionsResponse;
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
public class UserService {

  private final RestTemplate restTemplate;

  @Value("${services.user}")
  private String userApiUrl;

  @Autowired
  public UserService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Async
  public CompletableFuture<UserResponse> getUser(String userId){
    return CompletableFuture.supplyAsync(() -> restTemplate.exchange(
            userApiUrl+"/"+userId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<UserResponse>(){}).getBody());
  }
}
