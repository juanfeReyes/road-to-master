package com.roadtomaster.user.infrastructure.persistence;


import com.roadtomaster.bank.infrastructure.persistence.BankTable;
import com.roadtomaster.financialAsset.infrastructure.persistence.RegisterEmbedded;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@Entity
public class UserTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phone;

  @ManyToOne
  @JoinColumn(name = "fk_bank_id")
  private BankTable bank;

  @Embedded
  private RegisterEmbedded register;
}
