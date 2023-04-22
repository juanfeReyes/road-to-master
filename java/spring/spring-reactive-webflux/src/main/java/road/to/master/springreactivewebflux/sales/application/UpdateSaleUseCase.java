package road.to.master.springreactivewebflux.sales.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SalesRepository;

@Service
public class UpdateSaleUseCase {

  private final SalesRepository salesRepository;

  @Autowired
  public UpdateSaleUseCase(SalesRepository salesRepository) {
    this.salesRepository = salesRepository;
  }

  public Mono<SaleEntity> updateSale(SaleEntity sale){
    return Mono.just(sale)
            .flatMap(value -> salesRepository.findById(sale.getId())) // verify sale exists
            .switchIfEmpty(Mono.error(new Exception("sale not found")))
            .flatMap(salesRepository::save); //Verifies sale exists
  }
}
