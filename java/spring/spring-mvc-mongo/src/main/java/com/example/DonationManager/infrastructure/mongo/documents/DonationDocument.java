package com.example.DonationManager.infrastructure.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document("Donation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationDocument implements Serializable {

  @Id
  private String id;

  private String donor;

  private Date date;

  private String description;

  private String claimerId;

  private List<String> resourceIds;



  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime modifiedDate;

}
