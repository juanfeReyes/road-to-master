package com.r2m.cloud.sqs_producer.application.shipment;

import com.r2m.cloud.sqs_producer.domain.Shipment;
import com.r2m.cloud.sqs_producer.infrastructure.messaging.ShipmentMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReceiveShipmentService {

    private final ShipmentMessageProducer shipmentMessageProducer;

    @Autowired
    public ReceiveShipmentService(ShipmentMessageProducer shipmentMessageProducer) {
        this.shipmentMessageProducer = shipmentMessageProducer;
    }

    public void execute(Shipment shipment, String currentLocation){
        shipment.getTrackLabels().put(currentLocation, LocalDate.now());

        shipmentMessageProducer.produce(shipment);
    }
}
