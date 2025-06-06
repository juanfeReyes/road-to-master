package com.r2m.observabilityapi.infrastructure.api.rest;

import com.r2m.observabilityapi.application.SearchWarehouse;
import com.r2m.observabilityapi.infrastructure.api.WarehouseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/warehouse")
@RestController
public class WarehouseController {

  private final SearchWarehouse searchWarehouse;

  @Autowired
  public WarehouseController(SearchWarehouse searchWarehouse) {
    this.searchWarehouse = searchWarehouse;
  }

  @GetMapping("/{id}")
  public WarehouseResponse getWarehouseById(@PathVariable String id) throws Exception {
    return searchWarehouse.searchById(id);
  }
}
