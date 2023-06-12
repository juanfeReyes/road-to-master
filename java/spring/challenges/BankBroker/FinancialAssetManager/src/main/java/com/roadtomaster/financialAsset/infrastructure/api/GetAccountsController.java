package com.roadtomaster.financialAsset.infrastructure.api;

import com.roadtomaster.financialAsset.application.service.GetAccounts;
import com.roadtomaster.financialAsset.domain.model.Account;
import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class GetAccountsController {

  private final GetAccounts getAccounts;

  @Autowired
  public GetAccountsController(GetAccounts getAccounts) {
    this.getAccounts = getAccounts;
  }

  @GetMapping("")
  public List<Account> searchAccounts(AccountQuery searchParams) {
    return getAccounts.searchAccounts(searchParams);
  }
}
