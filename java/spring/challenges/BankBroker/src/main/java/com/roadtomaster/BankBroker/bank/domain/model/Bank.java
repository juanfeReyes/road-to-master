package com.roadtomaster.BankBroker.bank.domain.model;

import com.roadtomaster.BankBroker.user.domain.model.User;
import com.roadtomaster.BankBroker.user.persistence.UserTable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Bank {

  @NotNull
  private UUID id;

  @NotEmpty
  private String name;

  private Set<User> users;

  public Bank(String name){
    this.id = UUID.randomUUID();
    this.name = name;
    this.users = new HashSet<>();
  }

  public void addUser(User user){
    users.add(user);
  }


}
