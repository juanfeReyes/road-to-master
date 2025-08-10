package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterAlreadyProcessedShipmentStrategy filterAlreadyProcessedShipmentStrategy(){
        return new FilterAlreadyProcessedShipmentStrategy();
    }
}
