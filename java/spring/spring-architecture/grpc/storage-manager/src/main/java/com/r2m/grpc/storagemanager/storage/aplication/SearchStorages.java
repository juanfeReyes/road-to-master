package com.r2m.grpc.storagemanager.storage.aplication;

import com.r2m.grpc.storagemanager.storage.StorageQuery;
import com.r2m.grpc.storagemanager.storage.domain.Rack;
import com.r2m.grpc.storagemanager.storage.infrastructure.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SearchStorages {

  private final RackRepository rackRepository;

  @Autowired
  public SearchStorages(RackRepository rackRepository) {
    this.rackRepository = rackRepository;
  }

  public Stream<Rack> searchStorage(StorageQuery query) {
    return rackRepository.streamAllRacks();
  }
}
