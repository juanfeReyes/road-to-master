package r2m.spring.db.spring_jpa.infrastructure.persistence.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import r2m.spring.db.spring_jpa.domain.Shipment;

@Repository
public interface ShipmentRepository extends ListCrudRepository<Shipment, Integer> {
}
