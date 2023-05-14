package com.roadtomaster.BankBroker.bank.persistence;

import com.roadtomaster.BankBroker.user.persistence.UserTable;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class BankTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "bank")
  private Set<UserTable> users;
}
