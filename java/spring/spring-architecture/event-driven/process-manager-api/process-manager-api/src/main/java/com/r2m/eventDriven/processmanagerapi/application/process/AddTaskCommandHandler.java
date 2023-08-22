package com.r2m.eventDriven.processmanagerapi.application.process;

import com.r2m.eventDriven.processmanagerapi.domain.commands.AddTaskCommand;
import com.r2m.eventDriven.processmanagerapi.domain.events.AddTaskEvent;
import com.r2m.eventDriven.processmanagerapi.domain.events.DomainEvent;
import com.r2m.eventDriven.processmanagerapi.infrastructure.persistence.EventRepository;
import com.r2m.eventDriven.processmanagerapi.infrastructure.persistence.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddTaskCommandHandler {

  private final ProcessRepository processRepository;

  private final EventRepository eventRepository;

  private final StreamBridge streamBridge;

  @Autowired
  public AddTaskCommandHandler(ProcessRepository processRepository, EventRepository eventRepository, StreamBridge streamBridge) {
    this.processRepository = processRepository;
    this.eventRepository = eventRepository;
    this.streamBridge = streamBridge;
  }

  @Transactional
  public DomainEvent execute(AddTaskCommand command) {
    var event = AddTaskEvent.builder()
            .task(command.getTask())
            .processId(command.getProcessId())
            .build();
    // fetch aggregate with process id
    var process = processRepository.findById(command.getProcessId()).get();
    event.apply(process);

    processRepository.save(process);
    var domainEvent = eventRepository.save(new DomainEvent(event, event.getProcessId()));
    streamBridge.send("domain-event-out-0", domainEvent);
    return domainEvent;
  }
}
