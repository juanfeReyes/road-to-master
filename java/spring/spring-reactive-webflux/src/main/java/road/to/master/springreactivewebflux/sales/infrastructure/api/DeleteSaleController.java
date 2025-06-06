package road.to.master.springreactivewebflux.sales.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import road.to.master.springreactivewebflux.sales.application.DeleteSaleUseCase;

@RestController
@RequestMapping("/sales")
public class DeleteSaleController {

  private final DeleteSaleUseCase deleteSaleUseCase;

  @Autowired
  public DeleteSaleController(DeleteSaleUseCase deleteSaleUseCase) {
    this.deleteSaleUseCase = deleteSaleUseCase;
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<Void> deleteSale(@PathVariable String id){
    return deleteSaleUseCase.deleteSale(id);
  }
}
