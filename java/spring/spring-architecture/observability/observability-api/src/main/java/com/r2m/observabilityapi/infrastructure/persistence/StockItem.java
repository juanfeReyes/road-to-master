package com.r2m.observabilityapi.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StockItem {

  @Id
  private String id;

  private String name;

  private String description;

  private int count;

  @ManyToOne
  @JoinColumn(name = "shelve_id", foreignKey = @ForeignKey(name = "SHELVE_ID_FK"))
  private Shelve shelve;
}
