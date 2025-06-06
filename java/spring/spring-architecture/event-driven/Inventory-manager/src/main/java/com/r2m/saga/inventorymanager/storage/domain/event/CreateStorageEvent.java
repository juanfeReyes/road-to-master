package com.r2m.saga.inventorymanager.storage.domain.event;

import com.r2m.saga.inventorymanager.shared.domain.DomainEvent;
import com.r2m.saga.inventorymanager.storage.domain.Rack;
import lombok.Value;

import java.util.UUID;


public record CreateStorageEvent(String name, String location) implements DomainEvent {

  public void apply(Rack rack){
    rack.setId(UUID.randomUUID().toString());
    rack.setName(name);
    rack.setLocation(location);
    rack.setStockCount(0);
  }

}
