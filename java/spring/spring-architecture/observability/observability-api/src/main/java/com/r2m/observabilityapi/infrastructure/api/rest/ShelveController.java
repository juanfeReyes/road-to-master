package com.r2m.observabilityapi.infrastructure.api.rest;

import com.r2m.observabilityapi.application.RegisterStockItem;
import com.r2m.observabilityapi.infrastructure.persistence.StockItem;
import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shelve")
public class ShelveController {

  private final RegisterStockItem registerStockItem;

  @Autowired
  public ShelveController(RegisterStockItem registerStockItem) {
    this.registerStockItem = registerStockItem;
  }

  @PostMapping("/{id}")
  @NewSpan
  public StockItem addItem(@PathVariable String id, @RequestBody StockItem item) throws Exception {
    return registerStockItem.register(id, item);
  }
}
