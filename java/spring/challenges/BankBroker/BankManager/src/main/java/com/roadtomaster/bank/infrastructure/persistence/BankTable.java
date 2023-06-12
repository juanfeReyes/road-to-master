package com.roadtomaster.bank.infrastructure.persistence;

import com.roadtomaster.user.infrastructure.persistence.UserTable;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
public class BankTable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "bank")
  private Set<UserTable> users;
}
