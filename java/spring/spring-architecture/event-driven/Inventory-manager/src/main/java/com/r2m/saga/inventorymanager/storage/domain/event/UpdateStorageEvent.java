package com.r2m.saga.inventorymanager.storage.domain.event;

import com.r2m.saga.inventorymanager.shared.domain.DomainEvent;
import com.r2m.saga.inventorymanager.storage.domain.Rack;
import lombok.Value;

public record UpdateStorageEvent(
        String storageId,
        int stockCount
) implements DomainEvent {

  public void apply(Rack rack){
    rack.addStock(stockCount);
  }
}
