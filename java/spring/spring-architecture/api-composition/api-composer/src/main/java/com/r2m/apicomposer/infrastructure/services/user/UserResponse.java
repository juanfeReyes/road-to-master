package com.r2m.apicomposer.infrastructure.services.user;

public class UserResponse {

  private String name;
  private String lastname;
  private String phone;

  public UserResponse(String name, String lastname, String phone){
    this.name = name;
    this.lastname = lastname;
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public String getLastname() {
    return lastname;
  }

  public String getPhone() {
    return phone;
  }
}
