package com.example.DonationManager.infrastructure.mongo.repositories;

import com.example.DonationManager.infrastructure.mongo.documents.ResourceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<ResourceDocument, String> {
}
