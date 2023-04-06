package com.example.DonationManager.application.Donation;

import com.example.DonationManager.domain.Resource;
import com.example.DonationManager.domain.exceptions.NotFoundException;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocumentMapper;
import com.example.DonationManager.infrastructure.mongo.repositories.DonationRepository;
import com.example.DonationManager.infrastructure.mongo.repositories.ResourceRepository;
import org.bson.types.ObjectId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoadResourcesToDonation {

  private final DonationRepository donationRepository;

  private final ResourceRepository resourceRepository;

  private final DonationDocumentMapper donationDocumentMapper;

  private final RedisTemplate<String, Object> redisTemplate;

  public LoadResourcesToDonation(DonationRepository donationRepository, ResourceRepository resourceRepository, DonationDocumentMapper donationDocumentMapper, RedisTemplate<String, Object> redisTemplate) {
    this.donationRepository = donationRepository;
    this.resourceRepository = resourceRepository;
    this.donationDocumentMapper = donationDocumentMapper;
    this.redisTemplate = redisTemplate;
  }

  //TODO: Make it transactional
  public void execute(String donationId, List<Resource> resources){
    // validate donation exists
    var donationOpt = donationRepository.findById(donationId);
    if(donationOpt.isEmpty()){
      throw new NotFoundException("Donation not found");
    }

    var donationDocument = donationOpt.get();
    var resourceDocuments = resources.stream().map(donationDocumentMapper::resourceToDocument).collect(Collectors.toList());

    var savedResources = resourceRepository.saveAll(resourceDocuments);
    var resourcesId = Stream.concat(savedResources.stream()
        .map(resource -> resource.getId()), donationDocument.getResourceIds().stream())
        .collect(Collectors.toList());
    donationDocument.setResourceIds(resourcesId);

    donationRepository.save(donationDocument);
    redisTemplate.delete(donationId);
  }
}
