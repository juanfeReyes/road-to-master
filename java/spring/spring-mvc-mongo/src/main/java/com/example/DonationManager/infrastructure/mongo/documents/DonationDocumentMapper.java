package com.example.DonationManager.infrastructure.mongo.documents;

import com.example.DonationManager.domain.Donation;
import com.example.DonationManager.domain.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface DonationDocumentMapper {

  @Mapping(source = "resourceIds", target = "resourceIds")
  DonationDocument donationToDocument(Donation donation, List<String> resourceIds);

  Donation donationToDomain(DonationDocument document);

  Resource resourceToDomain(ResourceDocument document);

  ResourceDocument resourceToDocument(Resource resource);
}
