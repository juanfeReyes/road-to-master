package com.r2m.saga.inventorymanager.storage.application;

import com.r2m.saga.inventorymanager.shared.domain.Message;
import com.r2m.saga.inventorymanager.storage.application.mapper.RackEventMapper;
import com.r2m.saga.inventorymanager.storage.domain.command.UpdateStorageCommand;
import com.r2m.saga.inventorymanager.storage.infrastructure.persistence.RackEntityMapper;
import com.r2m.saga.inventorymanager.storage.infrastructure.persistence.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateStorage {

  private final RackRepository rackRepository;

  private final RackEventMapper rackEventMapper;

  private final RackEntityMapper rackEntityMapper;

  @Autowired
  public UpdateStorage(RackRepository rackRepository, RackEventMapper rackEventMapper, RackEntityMapper rackEntityMapper) {
    this.rackRepository = rackRepository;
    this.rackEventMapper = rackEventMapper;
    this.rackEntityMapper = rackEntityMapper;
  }

  public Message execute(UpdateStorageCommand updateStorageCommand) throws Exception {
    var rackEntity = rackRepository.findById(updateStorageCommand.storageId());

    if (rackEntity.isEmpty()){
      throw new Exception("Storage not fount with id: "+updateStorageCommand.storageId());
    }

    var rack = rackEntityMapper.toAggregate(rackEntity.get());
    var event = rackEventMapper.toEvent(updateStorageCommand);
    event.apply(rack);

    rackRepository.save(rackEntityMapper.toEntity(rack));
    return event;
  }

}
