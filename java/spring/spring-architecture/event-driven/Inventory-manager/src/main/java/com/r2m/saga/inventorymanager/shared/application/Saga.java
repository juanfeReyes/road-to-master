package com.r2m.saga.inventorymanager.shared.application;

import com.r2m.saga.inventorymanager.shared.domain.Message;
import com.r2m.saga.inventorymanager.storage.domain.event.CreateStorageEvent;

public interface Saga {

  Message execute(Message command);

  void compensate(Message Message);
}
