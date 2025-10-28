package r2m.spring.db.spring_jdbc.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import r2m.spring.db.spring_jdbc.application.ExportShipments;
import r2m.spring.db.spring_jdbc.application.ImportShipments;
import r2m.spring.db.spring_jdbc.domain.Shipment;
import r2m.spring.db.spring_jdbc.domain.Travel;
import r2m.spring.db.spring_jdbc.domain.TravelMediaType;
import r2m.spring.db.spring_jdbc.domain.request.ShipmentRequest;
import r2m.spring.db.spring_jdbc.infrastructure.persistence.repositories.ShipmentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentRepository repository;

    private final ImportShipments importShipments;

    private final ExportShipments exportShipments;

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

    @PostMapping("/import")
    public void importShipments(MultipartFile file) throws IOException {
        importShipments.execute(file);
    }

    @GetMapping("/export")
    public ResponseEntity<StreamingResponseBody> exportShipments() {
        StreamingResponseBody responseBody = out -> {
            exportShipments.execute(out);
            out.flush();
            out.close();
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shipments.xlsx")
                .body(responseBody);
    }
}
