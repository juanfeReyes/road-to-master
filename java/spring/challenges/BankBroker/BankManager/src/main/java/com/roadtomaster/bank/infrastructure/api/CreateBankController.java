package com.roadtomaster.bank.infrastructure.api;

import com.roadtomaster.bank.application.adapter.BankMapper;
import com.roadtomaster.bank.application.service.CreateBank;
import com.roadtomaster.bank.domain.model.Bank;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banks")
@Tag(name = "Banks", description = "Manage banks")
public class CreateBankController {

  private final CreateBank createBankService;

  private final BankMapper bankMapper;

  @Autowired
  public CreateBankController(CreateBank createBank, BankMapper bankMapper) {
    this.createBankService = createBank;
    this.bankMapper = bankMapper;
  }

  @PostMapping("")
  public Bank createBank(@RequestBody @Valid BankRequest request) {
    return createBankService.save(bankMapper.toDomain(request));
  }
}
