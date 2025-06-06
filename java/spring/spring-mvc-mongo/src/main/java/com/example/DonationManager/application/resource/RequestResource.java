package com.example.DonationManager.application.resource;

import com.example.DonationManager.domain.resource.Request;
import com.example.DonationManager.infrastructure.mongo.documents.ResourceRequestDocument;
import com.example.DonationManager.infrastructure.mongo.repositories.IResourceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestResource {

  private final IResourceRequestRepository resourceRequestRepository;

  @Autowired
  public RequestResource(IResourceRequestRepository resourceRequestRepository) {
    this.resourceRequestRepository = resourceRequestRepository;
  }

  public void execute(Request request) {
    // Store request in mongo
    resourceRequestRepository.save(ResourceRequestDocument.toDocument(request));
  }
}
