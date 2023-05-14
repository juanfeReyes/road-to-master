package com.roadtomaster.BankBroker.financialAsset.persistence;

import com.roadtomaster.BankBroker.user.persistence.UserTable;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class AccountTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private double balance;

  @ManyToOne
  @JoinColumn(name = "fk_user_id")
  private UserTable user;

  //TODO: Add audit fields
}
