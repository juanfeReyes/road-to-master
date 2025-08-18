package com.r2m.cloud.sqs_producer.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
public class Shipment {

    private UUID id;

    private String originAddress;

    private String destinationAddress;

    private List<Product> products;

    private Map<String, LocalDateTime> trackLabels;
}
