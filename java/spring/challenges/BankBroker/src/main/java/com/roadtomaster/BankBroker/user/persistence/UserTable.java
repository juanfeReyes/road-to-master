package com.roadtomaster.BankBroker.user.persistence;

import com.roadtomaster.BankBroker.bank.persistence.BankTable;
import com.roadtomaster.BankBroker.financialAsset.persistence.Register;
import jakarta.persistence.*;

import java.util.UUID;

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
  private Register register;
}
