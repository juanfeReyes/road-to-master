package com.example.DonationManager.infrastructure.mongo.documents;

import com.example.DonationManager.domain.resource.PriorityEnum;
import com.example.DonationManager.domain.resource.Request;
import com.example.DonationManager.domain.resource.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("ResourceRequest")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequestDocument {

  private String id;

  private String petId;

  private PriorityEnum priority;

  private List<Resource> resources;

  private Date creationDate;

  public static ResourceRequestDocument toDocument(Request resourceRequest) {
    return ResourceRequestDocument.builder()
        .id(resourceRequest.getId())
        .petId(resourceRequest.getPetId())
        .priority(resourceRequest.getPriority())
        .creationDate(resourceRequest.getCreationDate())
        .resources(resourceRequest.getResources())
        .build();
  }
}
