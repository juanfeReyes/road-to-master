package com.roadtomaster.financialAsset.persistence;

import com.roadtomaster.financialAsset.domain.TransactionState;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class TransactionsTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private double amount;

  @ManyToOne
  @JoinColumn(name = "fk_account_origin")
  private AccountTable origin;

  @ManyToOne
  @JoinColumn(name = "fk_account_destination")
  private AccountTable destination;

  @Enumerated(EnumType.STRING)
  private TransactionState transactionState;

  //TODO: add audit info from hibernate
}
