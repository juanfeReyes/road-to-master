package com.r2m.cloud.sqs_producer.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Product {

    private UUID id;

    private String name;

    private Double cost;

    private Double weight;
}
