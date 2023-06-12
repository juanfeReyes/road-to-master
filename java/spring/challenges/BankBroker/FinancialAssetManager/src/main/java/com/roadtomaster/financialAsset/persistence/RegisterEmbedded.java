package com.roadtomaster.financialAsset.persistence;



import com.roadtomaster.financialAsset.persistence.account.AccountTable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Embeddable
public class RegisterEmbedded {

  @OneToMany
  private Set<AccountTable> accounts;
}
