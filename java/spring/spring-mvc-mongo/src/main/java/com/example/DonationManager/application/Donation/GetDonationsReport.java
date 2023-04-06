package com.example.DonationManager.application.Donation;

import com.example.DonationManager.infrastructure.mongo.repositories.DonationRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

@Service
public class GetDonationsReport {

  private final DonationRepository donationRepository;

  @Autowired
  public GetDonationsReport(DonationRepository donationRepository) {
    this.donationRepository = donationRepository;
  }

  public Path execute() throws IOException {
    var previousTimeMonth = LocalDateTime.now().minusMonths(1);
    var donations = donationRepository.getDonationsBetweenDates( previousTimeMonth, LocalDateTime.now() );
    var content = new Gson().toJson(donations);

    var filePath = Paths.get("report.json");
    return Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
  }
}
