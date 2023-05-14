package com.roadtomaster.BankBroker.financialAsset.persistence;



import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class Register {

  @OneToMany(mappedBy = "user")
  private Set<AccountTable> accounts;
}
