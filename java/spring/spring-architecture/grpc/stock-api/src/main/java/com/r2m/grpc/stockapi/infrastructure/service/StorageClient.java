package com.r2m.grpc.stockapi.infrastructure.service;

import com.r2m.grpc.storagemanager.storage.StorageResponse;
import com.r2m.grpc.storagemanager.storage.StorageServiceGrpc;
import com.r2m.grpc.storagemanager.storage.UpdateStorageRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class StorageClient {

  //@Retry(name = "updateStorage")
  @CircuitBreaker(name = "updateStorage")
  public StorageResponse updateStorage(String id, String name, String location, int count) throws Exception {
    var channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build();

    var stub = StorageServiceGrpc.newBlockingStub(channel);
    var response = stub.update(UpdateStorageRequest.newBuilder()
                    .setId(id)
                    .setName(name)
                    .setLocation(location)
                    .setStockCount(count)
                    .build());

    channel.shutdown();

    return response;
  }
}
