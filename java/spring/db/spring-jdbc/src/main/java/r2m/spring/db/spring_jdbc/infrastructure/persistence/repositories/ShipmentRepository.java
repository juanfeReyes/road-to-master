package r2m.spring.db.spring_jdbc.infrastructure.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import r2m.spring.db.spring_jdbc.domain.Shipment;

import java.util.stream.Stream;

@Repository
public interface ShipmentRepository extends ListCrudRepository<Shipment, Integer> {

//    Stream<Shipment> streamShipments();
}
