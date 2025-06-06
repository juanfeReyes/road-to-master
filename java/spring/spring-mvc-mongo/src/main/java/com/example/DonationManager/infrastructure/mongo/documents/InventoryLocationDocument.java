package com.example.DonationManager.infrastructure.mongo.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class InventoryLocationDocument implements Serializable {

  private String sector;

  private String section;

  private int cubicleNumber;
}
