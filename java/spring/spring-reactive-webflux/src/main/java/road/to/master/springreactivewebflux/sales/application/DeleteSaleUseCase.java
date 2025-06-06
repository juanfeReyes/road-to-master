package road.to.master.springreactivewebflux.sales.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SalesRepository;

@Service
public class DeleteSaleUseCase {

  private final SalesRepository salesRepository;

  @Autowired
  public DeleteSaleUseCase(SalesRepository salesRepository) {
    this.salesRepository = salesRepository;
  }


  public Mono<Void> deleteSale(String id){
    return Mono.just(id)
            .flatMap(value -> salesRepository.findById(id)) // verify sale exists
            .switchIfEmpty(Mono.error(new Exception("sale not found")))
            .flatMap(salesRepository::delete); //Verifies sale exists
  }
}
