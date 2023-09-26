package com.r2m.saga.inventorymanager.storage.domain.event;

import com.r2m.saga.inventorymanager.shared.domain.DomainEvent;
import com.r2m.saga.inventorymanager.shared.domain.Message;
import lombok.Value;

public record CompensateCreateStorageEvent(String id) implements DomainEvent {

}
