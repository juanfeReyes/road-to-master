package com.roadtomaster.financialAsset.infrastructure.api;

import com.roadtomaster.financialAsset.application.service.GetTransactions;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class GetTransactionsController {

  private final GetTransactions getTransactions;

  @Autowired
  public GetTransactionsController(GetTransactions getTransactions) {
    this.getTransactions = getTransactions;
  }

  @GetMapping("/{userId}")
  public List<Transaction> getTransactions(@PathVariable String userId) {
    return getTransactions.getByUser(userId);
  }
}
