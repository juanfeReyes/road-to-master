package com.roadtomaster.bank.application.service;

import com.roadtomaster.bank.application.adapter.BankMapper;
import com.roadtomaster.bank.domain.model.Bank;
import com.roadtomaster.bank.persistence.BankRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBank {

  private final BankRepository bankRepository;

  private final BankMapper bankMapper;

  @Autowired
  public CreateBank(BankRepository bankRepository, BankMapper bankMapper) {
    this.bankRepository = bankRepository;
    this.bankMapper = bankMapper;
  }

  public Bank save(@Valid Bank bank){
    if(bankRepository.existsByName(bank.getName())){
      throw new RuntimeException("Bank with name already exists");
    }

    var savedBank = bankRepository.save(bankMapper.toTable(bank));

    return bankMapper.toDomain(savedBank);
  }
}
