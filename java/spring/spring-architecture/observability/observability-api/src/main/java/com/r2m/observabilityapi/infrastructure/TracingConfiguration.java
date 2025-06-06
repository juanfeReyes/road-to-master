package com.r2m.observabilityapi.infrastructure;

import io.micrometer.observation.ObservationRegistry;

import io.micrometer.observation.aop.ObservedAspect;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfiguration {

  @Bean
  public OtlpGrpcSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
    return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
  }

  @Bean
  ObservationRegistry observationRegistry() {
    return ObservationRegistry.create();
  }

  @Bean
  public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
    return new ObservedAspect(observationRegistry);
  }

}
