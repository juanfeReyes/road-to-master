package r2m.spring.db.spring_jpa.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import r2m.spring.db.spring_jpa.domain.Shipment;
import r2m.spring.db.spring_jpa.domain.Travel;
import r2m.spring.db.spring_jpa.domain.TravelMediaType;
import r2m.spring.db.spring_jpa.domain.request.ShipmentRequest;
import r2m.spring.db.spring_jpa.infrastructure.persistence.repositories.ShipmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @PostMapping("/")
    public void createShipment(@RequestBody ShipmentRequest request) {
        var shipment = new Shipment();
        shipment.setDestination(request.getDestination());
        shipment.setSource(request.getSource());

        Travel travel = Travel.builder()
                .address("cra 123")
                .mediaType(TravelMediaType.FLIGHT)
                .arrivalDate(LocalDateTime.now())
                .shipment(shipment)
                .build();
        shipment.getTravels().add(travel);
        shipmentRepository.save(shipment);
    }

    @GetMapping("/")
    public List<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }
}
