package com.roadtomaster.financialAsset.infrastructure.persistence.account;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Entity
public class AccountTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private double balance;

  @Column(nullable = false)
  private String ownerId;

  //TODO: Add audit fields
}
