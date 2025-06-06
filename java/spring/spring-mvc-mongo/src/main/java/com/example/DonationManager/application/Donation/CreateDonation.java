package com.example.DonationManager.application.Donation;

import com.example.DonationManager.domain.Donation;
import com.example.DonationManager.domain.Resource;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocumentMapper;
import com.example.DonationManager.infrastructure.mongo.repositories.DonationRepository;
import com.example.DonationManager.infrastructure.mongo.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CreateDonation {

  private final DonationRepository donationRepository;

  private final ResourceRepository resourceRepository;

  private final DonationDocumentMapper donationDocumentMapper;

  @Autowired
  public CreateDonation(DonationRepository donationRepository, ResourceRepository resourceRepository, DonationDocumentMapper donationDocumentMapper) {
    this.donationRepository = donationRepository;
    this.resourceRepository = resourceRepository;
    this.donationDocumentMapper = donationDocumentMapper;
  }

  public void execute(Donation donation) {
    var resourceDocuments = donation.getResources().stream()
        .map(donationDocumentMapper::resourceToDocument).collect(Collectors.toList());
    resourceRepository.saveAll(resourceDocuments);

    var resourceIds = donation.getResources().stream().map(Resource::getId).collect(Collectors.toList());
    this.donationRepository.save(donationDocumentMapper.donationToDocument(donation, resourceIds));
  }
}
