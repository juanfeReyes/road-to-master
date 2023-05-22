package com.roadtomaster.financialAsset.persistence;



import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class RegisterEmbedded {

  @OneToMany
  private Set<AccountTable> accounts;
}
