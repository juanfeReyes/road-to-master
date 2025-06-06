package com.road.master.PetShelter.infrastructure.messaging.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import schemas.Pet.PetResourceRequest;

import java.util.Map;

@Component
public class PetResourceSender {

  private final StreamBridge streamBridge;

  @Autowired
  public PetResourceSender(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void send(PetResourceRequest petRequest) {

    var headers = new MessageHeaders(Map.of());

    var message = MessageBuilder.createMessage(petRequest, headers);
    streamBridge.send("petResourceRequest-out-0", message);
  }
}
