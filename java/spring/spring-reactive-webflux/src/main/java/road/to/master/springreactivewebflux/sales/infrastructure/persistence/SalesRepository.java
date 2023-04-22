package road.to.master.springreactivewebflux.sales.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends ReactiveCrudRepository<SaleEntity, String> {
}
