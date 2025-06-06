package com.example.DonationManager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InventoryLocation {

  private String sector;

  private String section;

  private int cubicleNumber;
}
