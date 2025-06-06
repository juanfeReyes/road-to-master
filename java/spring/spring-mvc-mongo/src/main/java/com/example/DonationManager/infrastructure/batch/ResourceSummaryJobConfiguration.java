package com.example.DonationManager.infrastructure.batch;

import com.example.DonationManager.infrastructure.mongo.documents.ResourceDocument;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Map;

@Configuration
@EnableBatchProcessing
public class ResourceSummaryJobConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ResourceAllocatorItemProcessor resourceAllocatorItemProcessor;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private RetryStepListener retryStepListener;

  @Bean
  @Qualifier("DonationSummaryJob")
  public Job donationSummaryJob(Step allocateResourcesStep, Step generateResourceAllocationStep){
    return jobBuilderFactory.get("DonationSummary")
        .start(allocateResourcesStep)
        .on("CONTINUE").to(allocateResourcesStep)
        .end()
        .build();
  }

  @Bean
  public Step allocateResourcesStep(MongoItemReader<ResourceDocument> resourceMongoReader){

    var storageFacilityWriter = new MongoItemWriterBuilder<ResourceDocument>()
        .template(mongoTemplate)
        .build();

    return stepBuilderFactory.get("allocateResourcesStep")
        .<ResourceDocument, ResourceDocument>chunk(10)
        .reader(resourceMongoReader)
        .processor(resourceAllocatorItemProcessor)
        .writer(storageFacilityWriter)
        .listener(retryStepListener)
        .build();
  }

  @Bean
  @StepScope
  public MongoItemReader<ResourceDocument> resourceMongoReader(){
    return new MongoItemReaderBuilder<ResourceDocument>()
        .template(mongoTemplate)
        .name("ResourceReader")
        .targetType(ResourceDocument.class)
        .query(Query.query(Criteria.where("inventoryLocation").is(null)))
        .sorts(Map.of("_id", Sort.Direction.ASC))
        .build();
  }
}
