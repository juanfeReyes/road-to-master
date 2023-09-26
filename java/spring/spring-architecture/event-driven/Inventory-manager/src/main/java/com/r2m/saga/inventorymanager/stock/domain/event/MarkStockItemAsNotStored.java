package com.r2m.saga.inventorymanager.stock.domain.event;

import com.r2m.saga.inventorymanager.shared.domain.DomainEvent;
import com.r2m.saga.inventorymanager.stock.domain.StockItem;

public record MarkStockItemAsNotStored(
        String itemId) implements DomainEvent {

  public void apply(StockItem item){
    item.getMetadata().put("storage_status", "NOT_STORED");
  }
}
