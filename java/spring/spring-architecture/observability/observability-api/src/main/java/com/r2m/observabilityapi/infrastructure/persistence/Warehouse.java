package com.r2m.observabilityapi.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Warehouse {

  @Id
  private String id;

  private String address;

  private String country;

  private String region;
}
