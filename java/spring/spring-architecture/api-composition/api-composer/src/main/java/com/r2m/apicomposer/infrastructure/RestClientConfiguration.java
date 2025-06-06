package com.r2m.apicomposer.infrastructure;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder, SslBundles sslBundles){
    return builder
            .setSslBundle(sslBundles.getBundle("web-server")).build();
  }
}
