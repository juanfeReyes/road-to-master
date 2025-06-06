package com.r2m.observabilityapi.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Transport {

  @Id
  private String id;

  private String driver;

  private String plate;

  private int yearsOfService;
}
