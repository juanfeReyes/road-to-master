package com.roadtomaster.user.domain.model;

import com.roadtomaster.financialAsset.domain.model.Register;
import com.roadtomaster.user.domain.specification.PhoneValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class User {

  @NotNull
  private UUID id;

  @NotEmpty
  private String name;

  @NotEmpty
  private String lastName;

  @Email
  private String email;

  @PhoneValid
  private String phone;

  @NotNull
  private Register register;

  public User(String name, String lastName, String email, String phone){
    this.name = name;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.id = UUID.randomUUID();
    this.register = new Register();
  }

  public void updateContactInformation(String email, String phone){
    this.email = email;
    this.phone = phone;
  }
}
