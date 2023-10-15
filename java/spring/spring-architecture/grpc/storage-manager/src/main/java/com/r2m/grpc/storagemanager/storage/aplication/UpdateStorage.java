package com.r2m.grpc.storagemanager.storage.aplication;

import com.r2m.grpc.storagemanager.storage.domain.Rack;
import com.r2m.grpc.storagemanager.storage.infrastructure.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateStorage {

  private final RackRepository rackRepository;

  @Autowired
  public UpdateStorage(RackRepository rackRepository) {
    this.rackRepository = rackRepository;
  }

  public Rack execute(Rack rack) throws Exception {

    if (!rackRepository.existsById(rack.getId())) {
      throw new Exception("Storage does not exist");
    }

    return rackRepository.save(rack);
  }

}
