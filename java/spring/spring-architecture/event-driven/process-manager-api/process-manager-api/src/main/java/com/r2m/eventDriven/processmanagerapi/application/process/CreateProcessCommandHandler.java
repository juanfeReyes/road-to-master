package com.r2m.eventDriven.processmanagerapi.application.process;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import com.r2m.eventDriven.processmanagerapi.domain.commands.CreateProcessCommand;
import com.r2m.eventDriven.processmanagerapi.domain.events.CreateProcessEvent;
import com.r2m.eventDriven.processmanagerapi.domain.events.DomainEvent;
import com.r2m.eventDriven.processmanagerapi.infrastructure.persistence.EventRepository;
import com.r2m.eventDriven.processmanagerapi.infrastructure.persistence.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@JsonIgnoreProperties("hibernateLazyInitializer")
@Service
public class CreateProcessCommandHandler {

  private final ProcessRepository processRepository;

  private final EventRepository eventRepository;

  private final StreamBridge streamBridge;

  @Autowired
  public CreateProcessCommandHandler(ProcessRepository processRepository, EventRepository eventRepository, StreamBridge streamBridge) {
    this.processRepository = processRepository;
    this.eventRepository = eventRepository;
    this.streamBridge = streamBridge;
  }

  @Transactional
  public DomainEvent execute(CreateProcessCommand command) {
    var event = CreateProcessEvent.builder()
            .id(UUID.randomUUID().toString())
            .name(command.getName())
            .build();
    var process = new ProcessAggregate();
    event.apply(process);

    processRepository.save(process);
    var domainEvent = eventRepository.save(new DomainEvent(event, event.id));
    streamBridge.send("domain-event-out-0", domainEvent);
    return domainEvent;
  }
}
