package com.r2m.eventDriven.processmanagerapi.infrastructure.persistence;

import com.r2m.eventDriven.processmanagerapi.domain.aggregates.ProcessAggregate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends CrudRepository<ProcessAggregate, String> {
}
