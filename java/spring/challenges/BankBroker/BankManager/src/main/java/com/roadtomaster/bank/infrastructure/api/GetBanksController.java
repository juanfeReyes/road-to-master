package com.roadtomaster.bank.infrastructure.api;

import com.roadtomaster.bank.application.service.GetBanks;
import com.roadtomaster.bank.domain.model.Bank;
import com.roadtomaster.bank.infrastructure.persistence.BankQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banks")
public class GetBanksController {

  private final GetBanks getBanksService;

  @Autowired
  public GetBanksController(GetBanks getBanksService) {
    this.getBanksService = getBanksService;
  }

  @GetMapping("")
  public List<Bank> getBanks(BankQuery searchParams){
    return getBanksService.searchBanks(searchParams);
  }
}
