package com.r2m.eventDriven.processmanagerapi.domain.aggregates;

import com.r2m.eventDriven.processmanagerapi.domain.events.AddTaskEvent;
import com.r2m.eventDriven.processmanagerapi.domain.events.CreateProcessEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class ProcessAggregate {

  @Id
  private String id;

  private String name;

  @Column(columnDefinition = "JSON")
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, List<BusinessTask>> tasks;

  public void addTask(BusinessTask task) {
    tasks.putIfAbsent(task.getName(), new ArrayList<>());
  }

  public void removeTask(BusinessTask task) {
    tasks.values().forEach(t -> t.remove(task));
    tasks.remove(task.getName());
  }

  public void addTaskAssociation(BusinessTask originTask, BusinessTask destinationTask) {
    tasks.get(originTask.getName()).add(destinationTask);
  }
}
