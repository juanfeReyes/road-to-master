package com.r2m.saga.inventorymanager.stock.infrastructure.webapi;

import com.r2m.saga.inventorymanager.stock.application.RegisterItem;
import com.r2m.saga.inventorymanager.stock.domain.command.RegisterStockItemCommand;
import com.r2m.saga.inventorymanager.stock.domain.event.RegisterStockItemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class RegisterStockItemStorage {

  private final RegisterItem registerItem;

  @Autowired
  public RegisterStockItemStorage(RegisterItem registerItem) {
    this.registerItem = registerItem;
  }

  @PostMapping("/item")
  public RegisterStockItemEvent registerEvent(@RequestBody RegisterStockItemCommand command) {
    return (RegisterStockItemEvent) registerItem.execute(command);
  }
}
