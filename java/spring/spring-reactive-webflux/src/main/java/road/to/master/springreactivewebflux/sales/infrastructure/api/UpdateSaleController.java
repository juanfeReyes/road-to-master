package road.to.master.springreactivewebflux.sales.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.application.UpdateSaleUseCase;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;

@RestController
@RequestMapping("/sales")
public class UpdateSaleController {

  private final UpdateSaleUseCase updateSaleUseCase;

  @Autowired
  public UpdateSaleController(UpdateSaleUseCase updateSaleUseCase) {
    this.updateSaleUseCase = updateSaleUseCase;
  }

  @PutMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<SaleEntity> updateSale(SaleEntity sale){
    return updateSaleUseCase.updateSale(sale);
  }
}
