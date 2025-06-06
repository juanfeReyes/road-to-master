package com.example.DonationManager.infrastructure.messaging;

import com.example.DonationManager.application.resource.RequestResource;
import com.example.DonationManager.domain.resource.PriorityEnum;
import com.example.DonationManager.domain.resource.Request;
import com.example.DonationManager.domain.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import schemas.Pet.PetResourceRequest;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class RequestResourcesListener implements Consumer<Message<PetResourceRequest>> {

  private final RequestResource requestResource;

  @Autowired
  public RequestResourcesListener(RequestResource requestResource) {
    this.requestResource = requestResource;
  }

  @Override
  public void accept(Message<PetResourceRequest> message) {
    try {
      var petResourceRequest = message.getPayload();
      var request = toDomain(petResourceRequest);
      requestResource.execute(request);
    } catch (Exception e) {
      var esecion = e;
    }
  }

  //TODO: Improve code readability and type conversion
  private Request toDomain(PetResourceRequest petResourceRequest) throws ParseException {
    var resources = petResourceRequest.getResources()
        .stream().map(resource -> new Resource(resource.getId().toString(),
            resource.getName().toString(), resource.getAmount()))
        .collect(Collectors.toList());

    return new Request(
        UUID.randomUUID().toString(),
        petResourceRequest.getId().toString(),
        PriorityEnum.valueOf(petResourceRequest.getPriority().name()),
        resources,
        Date.from(Instant.parse(petResourceRequest.getCreationDate()))
    );
  }
}
