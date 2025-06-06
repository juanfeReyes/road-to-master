package com.example.DonationManager.infrastructure.api.Donation.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceCsv {

  @CsvBindByName
  private String description;

  @CsvBindByName
  private double amount;

  @CsvBindByName
  private String unit;
}
