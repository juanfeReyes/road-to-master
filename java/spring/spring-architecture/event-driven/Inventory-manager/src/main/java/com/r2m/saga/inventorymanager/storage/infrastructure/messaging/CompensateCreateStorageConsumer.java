package com.r2m.saga.inventorymanager.storage.infrastructure.messaging;

import com.r2m.saga.inventorymanager.storage.application.CreateStorage;
import com.r2m.saga.inventorymanager.storage.domain.event.CompensateCreateStorageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class CompensateCreateStorageConsumer implements Consumer<CompensateCreateStorageEvent> {

  private final CreateStorage createStorage;

  @Autowired
  public CompensateCreateStorageConsumer(CreateStorage createStorage) {
    this.createStorage = createStorage;
  }

  @Override
  public void accept(CompensateCreateStorageEvent compensateCreateStorageEvent) {
    createStorage.compensate(compensateCreateStorageEvent);
    //TODO: what happen if the compensation fails?
  }
}
