package com.roadtomaster.bank.application.service;

import com.roadtomaster.bank.application.adapter.BankMapper;
import com.roadtomaster.bank.domain.model.Bank;
import com.roadtomaster.bank.infrastructure.persistence.BankQuery;
import com.roadtomaster.bank.infrastructure.persistence.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class GetBanks {

  private final BankRepository bankRepository;

  private final BankMapper bankMapper;

  @Autowired
  public GetBanks(BankRepository bankRepository, BankMapper bankMapper) {
    this.bankRepository = bankRepository;
    this.bankMapper = bankMapper;
  }

  public List<Bank> searchBanks(BankQuery searchFilters) {
    var banks = bankRepository.findAll(searchFilters.toPredicate());

    return StreamSupport.stream(banks.spliterator(), false)
            .map(bankMapper::toDomain)
            .toList();
  }
}
