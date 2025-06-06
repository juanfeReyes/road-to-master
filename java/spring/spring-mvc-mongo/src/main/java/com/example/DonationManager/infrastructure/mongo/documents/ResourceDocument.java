package com.example.DonationManager.infrastructure.mongo.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDocument {

  @Id
  private String id;

  private String description;

  private double amount;

  private String unit;

  private InventoryLocationDocument inventoryLocation;

}
