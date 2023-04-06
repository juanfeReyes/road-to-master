package com.road.master.PetShelter.infrastructure.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

  @Async
  public CompletableFuture<Boolean> sendEmail() throws InterruptedException {
    Thread.sleep(5000);
    return CompletableFuture.completedFuture(Boolean.TRUE);
  }

}
