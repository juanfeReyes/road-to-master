package com.r2m.userapi.infrastructure.api;

public class UserResponse {

  private String name;
  private String lastname;
  private String phone;

  public UserResponse(String name, String lastname, String phone){
    this.name = name;
    this.lastname = lastname;
    this.phone = phone;
  }
}
