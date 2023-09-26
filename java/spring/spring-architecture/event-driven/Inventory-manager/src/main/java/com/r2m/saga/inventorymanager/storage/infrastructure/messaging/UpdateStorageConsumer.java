package com.r2m.saga.inventorymanager.storage.infrastructure.messaging;

import com.r2m.saga.inventorymanager.storage.application.UpdateStorage;
import com.r2m.saga.inventorymanager.storage.application.mapper.RackEventMapper;
import com.r2m.saga.inventorymanager.storage.domain.event.UpdateStorageEvent;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UpdateStorageConsumer implements Consumer<UpdateStorageEvent> {

  private final UpdateStorage updateStorage;

  private final RackEventMapper rackEventMapper;

  @Autowired
  public UpdateStorageConsumer(UpdateStorage updateStorage, RackEventMapper rackEventMapper) {
    this.updateStorage = updateStorage;
    this.rackEventMapper = rackEventMapper;
  }

  @SneakyThrows
  @Override
  public void accept(UpdateStorageEvent updateStorageEvent) {
    var command = rackEventMapper.toCommand(updateStorageEvent);
    updateStorage.execute(command);
  }
}
