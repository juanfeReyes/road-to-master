package com.r2m.grpc.storagemanager.storage.infrastructure.repository;

import com.r2m.grpc.storagemanager.storage.domain.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;


@Repository
public interface RackRepository extends JpaRepository<Rack, String> {

  @Query("select r from Rack r")
  Stream<Rack> streamAllRacks();
}
