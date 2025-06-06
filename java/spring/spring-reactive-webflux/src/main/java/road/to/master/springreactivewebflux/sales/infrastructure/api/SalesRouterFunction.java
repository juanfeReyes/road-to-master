package road.to.master.springreactivewebflux.sales.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import road.to.master.springreactivewebflux.sales.application.CreateSaleUseCase;
import road.to.master.springreactivewebflux.sales.application.GetSalesUseCase;
import road.to.master.springreactivewebflux.sales.infrastructure.persistence.SaleEntity;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class SalesRouterFunction {
  private final String SALES_FUNCTIONAL_ROOT = "/sales/func";

  private final GetSalesUseCase getSalesUseCase;
  private final CreateSaleUseCase createSaleUseCase;

  @Autowired
  public SalesRouterFunction(GetSalesUseCase getSalesUseCase, CreateSaleUseCase createSaleUseCase) {
    this.getSalesUseCase = getSalesUseCase;
    this.createSaleUseCase = createSaleUseCase;
  }

  @Bean
  public RouterFunction<ServerResponse> getSales() {
    return route(GET(SALES_FUNCTIONAL_ROOT + ""),
            request -> ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(getSalesUseCase.getSales(), SaleEntity.class))
          .andRoute(GET(SALES_FUNCTIONAL_ROOT + "/buffer"),
                  request -> ok()
                          .contentType(MediaType.TEXT_EVENT_STREAM)
                          .body(getSalesUseCase.getSalesWithBuffer(), SaleEntity.class))
          .andRoute(GET(SALES_FUNCTIONAL_ROOT + "/control"),
                  request -> ok()
                          .contentType(MediaType.TEXT_EVENT_STREAM)
                          .body(getSalesUseCase.getSalesBackPressureControl(), SaleEntity.class))
          .andRoute(GET(SALES_FUNCTIONAL_ROOT + "/drop"),
                  request -> ok()
                          .contentType(MediaType.TEXT_EVENT_STREAM)
                          .body(getSalesUseCase.getSalesBackPressureDrop(), SaleEntity.class));
  }

  @Bean
  public RouterFunction<ServerResponse> createSale(){
    return route(POST(SALES_FUNCTIONAL_ROOT + ""),
            request -> ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(createSaleUseCase.createSale(request.bodyToMono(SaleEntity.class).block()), SaleEntity.class));
  }
}
