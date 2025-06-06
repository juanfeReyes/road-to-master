package com.r2m.saga.inventorymanager.storage.infrastructure.persistence;

import com.r2m.saga.inventorymanager.storage.domain.Rack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class RackEntityMapper {

  @Mapping(source = "id", target = "id")
  public abstract RackEntity toEntity(Rack rack);

  public abstract Rack toAggregate(RackEntity entity);
}
