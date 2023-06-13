package com.roadtomaster.financialAsset.infrastructure.persistence.transaction;

import com.roadtomaster.financialAsset.domain.TransactionState;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
