package com.example.DonationManager.application.Donation;

import com.example.DonationManager.domain.Donation;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocumentMapper;
import com.example.DonationManager.infrastructure.mongo.repositories.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimDonation {

  private final DonationRepository donationRepository;

  private final DonationDocumentMapper donationDocumentMapper;

  @Autowired
  public ClaimDonation(DonationRepository donationRepository, DonationDocumentMapper donationDocumentMapper) {
    this.donationRepository = donationRepository;
    this.donationDocumentMapper = donationDocumentMapper;
  }

  public Donation execute(String donationId, String claimer) {

    var document = donationRepository.findById(donationId).get();

    if (document == null) {
      throw new RuntimeException("Donation not found");
    }

    document.setClaimerId(claimer);
    donationRepository.save(document);

    var donation = donationDocumentMapper.donationToDomain(document);
    return donation;
  }
}
