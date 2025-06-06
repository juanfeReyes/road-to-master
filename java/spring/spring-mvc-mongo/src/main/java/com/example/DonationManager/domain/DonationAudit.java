package com.example.DonationManager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DonationAudit {

  private Donation donation;

  private String claimer;
}
