package com.roadtomaster.financialAsset.persistence;

import jakarta.persistence.*;

import java.util.UUID;

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
