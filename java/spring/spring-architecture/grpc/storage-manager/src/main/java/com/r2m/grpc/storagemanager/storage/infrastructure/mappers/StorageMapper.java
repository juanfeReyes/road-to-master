package com.r2m.grpc.storagemanager.storage.infrastructure.mappers;

import com.r2m.grpc.storagemanager.storage.CreateStorageRequest;
import com.r2m.grpc.storagemanager.storage.StorageResponse;
import com.r2m.grpc.storagemanager.storage.UpdateStorageRequest;
import com.r2m.grpc.storagemanager.storage.domain.Rack;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StorageMapper {

  public abstract Rack toDomain(CreateStorageRequest request);

  public abstract Rack toDomain(UpdateStorageRequest request);

  public abstract StorageResponse toResponse(Rack rack);


}
