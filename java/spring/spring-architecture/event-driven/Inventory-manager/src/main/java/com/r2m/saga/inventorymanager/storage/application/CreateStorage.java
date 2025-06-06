package com.r2m.saga.inventorymanager.storage.application;

import com.r2m.saga.inventorymanager.shared.application.Saga;
import com.r2m.saga.inventorymanager.shared.domain.Message;
import com.r2m.saga.inventorymanager.storage.application.mapper.RackEventMapper;
import com.r2m.saga.inventorymanager.storage.domain.Rack;
import com.r2m.saga.inventorymanager.storage.domain.command.CreateStorageCommand;
import com.r2m.saga.inventorymanager.storage.domain.event.CompensateCreateStorageEvent;
import com.r2m.saga.inventorymanager.storage.domain.event.CreateStorageEvent;
import com.r2m.saga.inventorymanager.storage.infrastructure.persistence.RackEntityMapper;
import com.r2m.saga.inventorymanager.storage.infrastructure.persistence.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateStorage implements Saga {

  private final RackRepository rackRepository;

  private final RackEntityMapper rackEntityMapper;

  private final RackEventMapper rackEventMapper;

  @Autowired
  public CreateStorage(RackRepository rackRepository, RackEntityMapper rackEntityMapper, RackEventMapper rackEventMapper) {
    this.rackRepository = rackRepository;
    this.rackEntityMapper = rackEntityMapper;
    this.rackEventMapper = rackEventMapper;
  }

  @Override
  public Message execute(Message command) {
    var rack = Rack.builder().build();
    var event = rackEventMapper.toEvent((CreateStorageCommand) command);
    event.apply(rack);

    rackRepository.save(rackEntityMapper.toEntity(rack));
    return event;
  }

  @Override
  public void compensate(Message event) {
    var compensateEvent = (CompensateCreateStorageEvent) event;
    rackRepository.deleteById(compensateEvent.id());
  }
}
