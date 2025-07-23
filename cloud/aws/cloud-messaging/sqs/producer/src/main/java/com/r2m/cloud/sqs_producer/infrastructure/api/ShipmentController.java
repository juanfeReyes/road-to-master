package com.r2m.cloud.sqs_producer.infrastructure.api;

import com.r2m.cloud.sqs_producer.application.shipment.ReceiveShipmentService;
import com.r2m.cloud.sqs_producer.domain.requests.ReceiveShipmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/shipments")
public class ShipmentController {

    private final ReceiveShipmentService receiveShipmentService;

    @Autowired
    public ShipmentController(ReceiveShipmentService receiveShipmentService) {
        this.receiveShipmentService = receiveShipmentService;
    }

    @PutMapping("/tracks")
    public ResponseEntity<Void> receiveShipment(@RequestBody ReceiveShipmentRequest request){
        receiveShipmentService.execute(request.getShipment(), request.getCurrentLocation());
        return ResponseEntity.accepted().build();
    }
}
