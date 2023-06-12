package com.roadtomaster.financialAsset.infrastructure.persistence;



import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountTable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class RegisterEmbedded {

  @OneToMany
  private Set<AccountTable> accounts;
}
