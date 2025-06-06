package com.example.DonationManager.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class Request {

  private String id;

  private String petId;

  private PriorityEnum priority;

  private List<Resource> resources;

  private Date creationDate;
}
