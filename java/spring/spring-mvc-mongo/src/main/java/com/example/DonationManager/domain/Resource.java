package com.example.DonationManager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resource implements Serializable {

  @Id
  private String id;

  private String description;

  private double amount;

  private String unit;

  private InventoryLocation inventoryLocation;
}
