package com.example.DonationManager.infrastructure.mongo.repositories;

import com.example.DonationManager.infrastructure.mongo.documents.ResourceRequestDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IResourceRequestRepository extends MongoRepository<ResourceRequestDocument, String> {
}
