package com.r2m.observabilityapi.application;

import com.r2m.observabilityapi.infrastructure.api.WarehouseResponse;
import com.r2m.observabilityapi.infrastructure.persistence.repositories.ShelveRepository;
import com.r2m.observabilityapi.infrastructure.persistence.repositories.WarehouseRepository;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Observed(name = "search-warehouse")
public class SearchWarehouse {

  private final WarehouseRepository warehouseRepository;

  private final ShelveRepository shelveRepository;

  private final Logger log = LoggerFactory.getLogger(SearchWarehouse.class);

  @Autowired
  public SearchWarehouse(WarehouseRepository warehouseRepository, ShelveRepository shelveRepository) {
    this.warehouseRepository = warehouseRepository;
    this.shelveRepository = shelveRepository;
  }

  public WarehouseResponse searchById(String id) throws Exception {
    var warehouse = warehouseRepository.findById(id);
    var shelves = shelveRepository.findAllByWarehouseId(id);

    if(warehouse.isEmpty()){
      var error = new Exception("Warehouse does not exist");
      log.error("Warehouse does not exists", error);
      throw error;
    }

    log.info("Warehouse found %s", id);
    return WarehouseResponse.builder()
            .warehouse(warehouse.get())
            .shelves(shelves)
            .build();

  }
}
