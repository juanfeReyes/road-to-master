package com.roadtomaster.bank.domain.model;


import com.roadtomaster.user.domain.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class Bank {

  @NotNull
  private UUID id;

  @NotEmpty
  private String name;

  @NotNull
  private Set<User> users;

  public Bank(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.users = new HashSet<>();
  }

  public void addUser(User user) {
    users.add(user);
  }
}
