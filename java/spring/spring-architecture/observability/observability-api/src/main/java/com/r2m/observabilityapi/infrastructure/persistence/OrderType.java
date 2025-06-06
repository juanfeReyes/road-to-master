package com.r2m.observabilityapi.infrastructure.persistence;

public enum OrderType {

  IN("in"),
  OUT("out");

  private String name;

  private OrderType(String name){
    this.name = name;
  }
}
