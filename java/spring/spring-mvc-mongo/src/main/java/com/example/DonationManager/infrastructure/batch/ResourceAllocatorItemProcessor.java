package com.example.DonationManager.infrastructure.batch;

import com.example.DonationManager.application.resource.AllocateResourceToStorageFacility;
import com.example.DonationManager.infrastructure.mongo.documents.DonationDocumentMapper;
import com.example.DonationManager.infrastructure.mongo.documents.ResourceDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceAllocatorItemProcessor implements ItemProcessor<ResourceDocument, ResourceDocument> {

  private final AllocateResourceToStorageFacility allocateResourceToStorageFacility;

  private final DonationDocumentMapper donationDocumentMapper;

  @Autowired
  public ResourceAllocatorItemProcessor(AllocateResourceToStorageFacility allocateResourceToStorageFacility, DonationDocumentMapper donationDocumentMapper){
    this.allocateResourceToStorageFacility = allocateResourceToStorageFacility;
    this.donationDocumentMapper = donationDocumentMapper;
  }

  public ResourceDocument process(ResourceDocument document) throws Exception {
    var resource = donationDocumentMapper.resourceToDomain(document);

    if(resource.getInventoryLocation() != null){
      return null;
    }

    var allocatedResource = allocateResourceToStorageFacility.execute(resource);
    return donationDocumentMapper.resourceToDocument(allocatedResource);
  }
}