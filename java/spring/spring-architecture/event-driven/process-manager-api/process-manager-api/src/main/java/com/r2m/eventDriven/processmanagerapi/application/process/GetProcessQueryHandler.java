package com.r2m.eventDriven.processmanagerapi.application.process;

import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import com.r2m.eventDriven.processmanagerapi.infrastructure.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetProcessQueryHandler {

  private final EventRepository eventRepository;

  @Autowired
  public GetProcessQueryHandler(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }


  public ProcessAggregate execute(String processId){
    var events = eventRepository.findByPayloadId(processId);
    var process = new ProcessAggregate();
    /**
     * Following snapshot strategy to build a materialized view
     */
    events.forEach((event) -> event.getPayload().apply(process));

    return process;
  }
}
