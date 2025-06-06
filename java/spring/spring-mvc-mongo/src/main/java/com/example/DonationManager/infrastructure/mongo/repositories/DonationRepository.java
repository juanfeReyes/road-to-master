package com.example.DonationManager.infrastructure.mongo.repositories;

import com.example.DonationManager.domain.Donation;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocument;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface DonationRepository extends MongoRepository<DonationDocument, String> {

  @Aggregation(pipeline = {
      "{ $match: { _id : ?0} }",
      "{ $lookup: { from: Resource, localField: resourceIds, foreignField: _id, as: resources }}"
  })
  Donation getDonationWithResources(String id);

  @Query("{modifiedDate: {$gte: ?0, $lte: ?1}}")
  List<Donation> getDonationsBetweenDates(LocalDateTime from, LocalDateTime to);
}
