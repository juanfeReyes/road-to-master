package road.to.master.springreactivewebflux.sales.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SalesRepository;

@Service
public class CreateSaleUseCase {

  private final SalesRepository salesRepository;

  @Autowired
  public CreateSaleUseCase(SalesRepository salesRepository) {
    this.salesRepository = salesRepository;
  }

  public Mono<SaleEntity> createSale(SaleEntity sale){
    return Mono.just(sale)
            .flatMap(value ->  salesRepository.save(value)); //Verifies sale exists
  }
}
