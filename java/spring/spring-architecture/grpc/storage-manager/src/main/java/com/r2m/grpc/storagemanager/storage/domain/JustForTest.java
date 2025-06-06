package com.r2m.grpc.storagemanager.storage.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class JustForTest {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
}
