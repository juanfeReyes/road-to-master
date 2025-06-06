package road.to.master.springreactivewebflux.sales.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import road.to.master.springreactivewebflux.sales.application.GetSalesUseCase;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class GetSalesController {

  private final GetSalesUseCase getSalesUseCase;

  @Autowired
  public GetSalesController( GetSalesUseCase getSalesUseCase) {
    this.getSalesUseCase = getSalesUseCase;
  }


  @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<SaleEntity> getSales(){
    return getSalesUseCase.getSales();
  }

  @GetMapping(value = "/buffer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<List<SaleEntity>> getSalesWithBuffer(){
    return getSalesUseCase.getSalesWithBuffer();
  }

  @GetMapping(value = "/control", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<SaleEntity> getSalesWithControl(){
    return getSalesUseCase.getSalesBackPressureControl();
  }

  @GetMapping(value = "/drop", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<SaleEntity> getSalesWithDrop(){
    return getSalesUseCase.getSalesBackPressureDrop();
  }
}
