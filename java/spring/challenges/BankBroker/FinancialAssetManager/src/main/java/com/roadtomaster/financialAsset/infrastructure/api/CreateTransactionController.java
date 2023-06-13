package com.roadtomaster.financialAsset.infrastructure.api;

import com.roadtomaster.financialAsset.application.service.CreateTransaction;
import com.roadtomaster.financialAsset.domain.model.Transaction;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transactions", description = "Manage transactions")
public class CreateTransactionController {

  private final CreateTransaction createTransactionService;

  @Autowired
  public CreateTransactionController(CreateTransaction createTransaction) {
    this.createTransactionService = createTransaction;
  }

  @PostMapping("")
  public Transaction createTransaction(@RequestBody CreateTransactionRequest requestBody) {
    return createTransactionService.saveTransaction(
            requestBody.getOriginId(),
            requestBody.getDestinationId(),
            requestBody.getAmount());
  }
}
