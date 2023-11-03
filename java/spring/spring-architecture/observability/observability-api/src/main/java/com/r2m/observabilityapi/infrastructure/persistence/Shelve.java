package com.r2m.observabilityapi.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Shelve {

  @Id
  private String id;

  private String location;

  @ManyToOne
  private Warehouse warehouse;
}
