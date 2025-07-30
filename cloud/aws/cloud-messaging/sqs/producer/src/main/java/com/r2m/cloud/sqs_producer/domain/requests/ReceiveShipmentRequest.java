package com.r2m.cloud.sqs_producer.domain.requests;

import com.r2m.cloud.sqs_producer.domain.Shipment;
import lombok.Data;

@Data
public class ReceiveShipmentRequest {

    private Shipment shipment;

    private String currentLocation;
}
