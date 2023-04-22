package road.to.master.springreactivewebflux.sales.application;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SalesRepository;

import java.time.Duration;
import java.util.List;

@Service
public class GetSalesUseCase {


  private final SalesRepository salesRepository;

  public GetSalesUseCase(SalesRepository salesRepository) {
    this.salesRepository = salesRepository;
  }

  public Flux<SaleEntity> getSales(){
    return salesRepository.findAll()
            .delayElements(Duration.ofMillis(500));
  }

  public Flux<List<SaleEntity>> getSalesWithBuffer(){
    return salesRepository.findAll()
            .log()
            .buffer(3)
            .delayElements(Duration.ofMillis(500));
  }

  public Flux<SaleEntity> getSalesBackPressureControl(){
    return salesRepository.findAll()
            .log()
            .limitRate(5)
            .delayElements(Duration.ofMillis(500));
  }

  public Flux<SaleEntity> getSalesBackPressureDrop(){
    return salesRepository.findAll()
            .log()
            .sample(Duration.ofMillis(50))
            .delayElements(Duration.ofMillis(500));
  }
}
