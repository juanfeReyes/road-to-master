package com.r2m.grpc.storagemanager.storage.aplication;


import com.r2m.grpc.storagemanager.storage.domain.Rack;
import com.r2m.grpc.storagemanager.storage.infrastructure.repository.RackRepository;
import com.r2m.grpc.storagemanager.storage.infrastructure.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateStorage {

  private final TestRepository testRepository;

  private final RackRepository rackRepository;

  @Autowired
  public CreateStorage(TestRepository testRepository, RackRepository rackRepository) {
    this.testRepository = testRepository;
    this.rackRepository = rackRepository;
  }

  public Rack execute(Rack rack) {

    rackRepository.save(rack);
    return rack;
  }

}
