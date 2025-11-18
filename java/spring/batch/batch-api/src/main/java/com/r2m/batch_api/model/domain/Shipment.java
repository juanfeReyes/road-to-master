package com.r2m.batch_api.model.domain;

import lombok.Builder;

@Builder
public class Shipment  {
    private String source;
    private String destination;
}
