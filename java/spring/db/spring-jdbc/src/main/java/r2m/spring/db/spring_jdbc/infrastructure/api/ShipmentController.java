package r2m.spring.db.spring_jdbc.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import r2m.spring.db.spring_jdbc.domain.Shipment;
import r2m.spring.db.spring_jdbc.domain.Travel;
import r2m.spring.db.spring_jdbc.domain.TravelMediaType;
import r2m.spring.db.spring_jdbc.domain.request.ShipmentRequest;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.repositories.ShipmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentRepository repository;

    @Autowired
    public ShipmentController(ShipmentRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/")
    public void createShipment(@RequestBody ShipmentRequest request) {
        var shipment = new Shipment();
        shipment.setDestination(request.getDestination());
        shipment.setSource(request.getSource());

        Travel travel = Travel.builder()
                .id(UUID.randomUUID())
                .address("cra 123")
                .mediaType(TravelMediaType.FLIGHT)
                .arrivalDate(LocalDateTime.now())
                .build();
        shipment.setTravels(Set.of(travel));

        repository.save(shipment);
    }

    @GetMapping("/")
    public List<Shipment> getShipments() {
        return repository.findAll();
    }
}
