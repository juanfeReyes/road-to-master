package com.r2m.observabilityapi.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class User {

  @Id
  private String id;

  private String name;

  private String phone;
}
