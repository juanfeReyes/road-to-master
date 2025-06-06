package com.example.DonationManager.infrastructure.batch;

import com.example.DonationManager.infrastructure.mongo.documents.ResourceDocument;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class RetryStepListener implements StepExecutionListener {

  private final MongoTemplate mongoTemplate;

  public RetryStepListener(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {

  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    var resources = mongoTemplate.find(Query.query(Criteria.where("inventoryLocation").is(null)), ResourceDocument.class);
    if(resources.isEmpty()){
      return new ExitStatus("FINISHED");
    }

    return new ExitStatus("CONTINUE");
  }
}
