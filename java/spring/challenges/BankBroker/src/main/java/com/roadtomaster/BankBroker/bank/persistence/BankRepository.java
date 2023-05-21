package com.roadtomaster.BankBroker.bank.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends CrudRepository<BankTable, UUID> {
}
