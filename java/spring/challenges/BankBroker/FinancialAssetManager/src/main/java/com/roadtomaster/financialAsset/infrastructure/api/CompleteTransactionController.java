package com.roadtomaster.financialAsset.infrastructure.api;

import com.roadtomaster.financialAsset.application.service.CompleteTransaction;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class CompleteTransactionController {

  private final CompleteTransaction completeTransaction;

  @Autowired
  public CompleteTransactionController(CompleteTransaction completeTransaction) {
    this.completeTransaction = completeTransaction;
  }

  @PutMapping("/{transactionId}/complete")
  public Transaction completePendingTransaction(@PathVariable UUID transactionId) {
    return completeTransaction.complete(transactionId);
  }
}
