package com.r2m.saga.inventorymanager.storage.application.mapper;

import com.r2m.saga.inventorymanager.storage.domain.command.CreateStorageCommand;
import com.r2m.saga.inventorymanager.storage.domain.command.UpdateStorageCommand;
import com.r2m.saga.inventorymanager.storage.domain.event.CreateStorageEvent;
import com.r2m.saga.inventorymanager.storage.domain.event.UpdateStorageEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class RackEventMapper {

  public abstract CreateStorageEvent toEvent(CreateStorageCommand command);

  public abstract UpdateStorageEvent toEvent(UpdateStorageCommand updateStorageCommand);

  public abstract UpdateStorageCommand toCommand(UpdateStorageEvent updateStorageEvent);
}
