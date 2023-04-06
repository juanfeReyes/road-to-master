package com.example.DonationManager.application.Donation;

import com.example.DonationManager.domain.Donation;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocumentMapper;
import com.example.DonationManager.infrastructure.mongo.repositories.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetDonationByFilter {

  private final DonationRepository donationRepository;

  private final RedisTemplate<String, Object> redisTemplate;

  private final DonationDocumentMapper donationDocumentMapper;

  @Autowired
  public GetDonationByFilter(DonationRepository donationRepository,
                             RedisTemplate<String, Object> redisTemplate, DonationDocumentMapper donationDocumentMapper) {
    this.donationRepository = donationRepository;
    this.redisTemplate = redisTemplate;
    this.donationDocumentMapper = donationDocumentMapper;
  }

  public Donation execute(String id) {
    var donation = (Donation) redisTemplate.opsForValue().get(id);

    if (donation != null) {
      return donation;
    }

    donation = donationRepository.getDonationWithResources(id);
    redisTemplate.opsForValue().set(id, donation);
    return donation;
  }
}
