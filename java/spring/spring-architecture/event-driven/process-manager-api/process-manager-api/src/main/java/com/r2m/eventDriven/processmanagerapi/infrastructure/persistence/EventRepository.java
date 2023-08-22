package com.r2m.eventDriven.processmanagerapi.infrastructure.persistence;

import com.r2m.eventDriven.processmanagerapi.domain.events.DomainEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<DomainEvent, String> {

  public List<DomainEvent> findByPayloadId(String payloadId);
}
