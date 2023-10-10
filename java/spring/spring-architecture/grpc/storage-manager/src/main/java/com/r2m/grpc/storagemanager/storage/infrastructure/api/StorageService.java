package com.r2m.grpc.storagemanager.storage.infrastructure.api;


import com.google.rpc.Status;
import com.r2m.grpc.storagemanager.storage.*;
import com.r2m.grpc.storagemanager.storage.aplication.CreateStorage;
import com.r2m.grpc.storagemanager.storage.aplication.SearchStorages;
import com.r2m.grpc.storagemanager.storage.aplication.UpdateStorage;
import com.r2m.grpc.storagemanager.storage.infrastructure.mappers.StorageMapper;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageService extends StorageServiceGrpc.StorageServiceImplBase {

  private final StorageMapper storageMapper;

  private final CreateStorage createStorage;

  private final SearchStorages searchStorages;

  private final UpdateStorage updateStorage;

  @Autowired
  public StorageService(StorageMapper storageMapper, CreateStorage createStorage, SearchStorages searchStorages, UpdateStorage updateStorage) {
    this.storageMapper = storageMapper;
    this.createStorage = createStorage;
    this.searchStorages = searchStorages;
    this.updateStorage = updateStorage;
  }

  @Override
  public void create(CreateStorageRequest request, StreamObserver<StorageResponse> responseStreamObserver) {
    var rack = storageMapper.toDomain(request);
    var savedStorage = createStorage.execute(rack);
    var response = storageMapper.toResponse(savedStorage);
    responseStreamObserver.onNext(response);
    responseStreamObserver.onCompleted();
  }

  @Override
  public void update(UpdateStorageRequest request, StreamObserver<StorageResponse> streamObserver) {
    try {
      var updatedRack = updateStorage.execute(storageMapper.toDomain(request));
      streamObserver.onNext(storageMapper.toResponse(updatedRack));
      streamObserver.onCompleted();
    } catch (Exception e) {
      var status = Status.newBuilder()
              .setCode(5)
              .setMessage(e.getMessage())
              .build();
      streamObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void get(StorageQuery request, StreamObserver<StorageResponse> streamObserver) {

    try (var stream = searchStorages.searchStorage(request)) {
      stream.forEach(rack -> {
        var response = storageMapper.toResponse(rack);
        streamObserver.onNext(response);
      });
    } finally {
      streamObserver.onCompleted();
    }
  }
}
