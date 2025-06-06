package com.example.DonationManager.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Resource {

  private String id;

  private String name;

  private long amount;
}
