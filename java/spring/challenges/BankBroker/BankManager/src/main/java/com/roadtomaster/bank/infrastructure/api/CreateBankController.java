package com.roadtomaster.bank.infrastructure.api;

import com.roadtomaster.bank.application.service.CreateBank;
import com.roadtomaster.bank.domain.model.Bank;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banks")
public class CreateBankController {

  private final CreateBank createBankService;

  @Autowired
  public CreateBankController(CreateBank createBank) {
    this.createBankService = createBank;
  }

  @PostMapping("")
  public Bank createBank(@RequestBody @Valid Bank bank){
    return createBankService.save(bank);
  }
}
