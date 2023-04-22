package road.to.master.springreactivewebflux.sales.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.application.CreateSaleUseCase;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;

@RestController
@RequestMapping("/sales")
public class CreateSaleController {

  private final CreateSaleUseCase createSaleUseCase;

  @Autowired
  public CreateSaleController(CreateSaleUseCase createSaleUseCase) {
    this.createSaleUseCase = createSaleUseCase;
  }

  @PostMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<SaleEntity> createSale(@RequestBody SaleEntity sale){
    return createSaleUseCase.createSale(sale);
  }
}
