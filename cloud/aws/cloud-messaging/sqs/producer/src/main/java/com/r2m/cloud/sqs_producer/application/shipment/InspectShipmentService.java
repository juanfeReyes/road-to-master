package com.r2m.cloud.sqs_producer.application.shipment;

import com.r2m.cloud.sqs_producer.domain.Product;
import com.r2m.cloud.sqs_producer.domain.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class InspectShipmentService {
    private Map<UUID, Shipment> shipmentStore = new HashMap<>();
    private List<String> alarms = new ArrayList<>(); //TODO: Move this to in-memory cache

    public void execute(Shipment shipment){
        if(!shipmentStore.containsKey(shipment.getId())) {
            log.info("Shipment has not been inspected before, adding to store");
            shipmentStore.put(shipment.getId(), shipment);
            return;
        }

        String lastLocation = shipment.getTrackLabels().entrySet().stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("NO_FOUND");
        Shipment inspectedShipment = shipmentStore.get(shipment.getId());

        if(!areProductsEquals(inspectedShipment.getProducts(), shipment.getProducts())) {
            List<Product> missingProducts = shipment.getProducts().stream()
                    .filter(p -> inspectedShipment.getProducts().contains(p))
                    .toList();
            alarms.add(String.format("Missing products: %s on location %s",
                    missingProducts,
                    lastLocation));
        }
    }

    private boolean areProductsEquals(List<Product> inspectedProducts, List<Product> currentProducts){
        return new HashSet<>(inspectedProducts).containsAll(currentProducts) &&
                new HashSet<>(currentProducts).containsAll(inspectedProducts);
    }
}
