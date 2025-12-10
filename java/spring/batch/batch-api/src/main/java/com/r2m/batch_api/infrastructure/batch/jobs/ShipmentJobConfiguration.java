package com.r2m.batch_api.infrastructure.batch.jobs;

import com.r2m.batch_api.model.domain.Shipment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ShipmentJobConfiguration {

    @Bean
    public Job excelShipmentJob(JobRepository jobRepository,
                                Step importShipmentStep){
        return new JobBuilder("excelShipmentJob", jobRepository)
                .start(importShipmentStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step importShipmentStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   PoiItemReader excelReader,
                                   ShipmentWriter shipmentWriter){
        return new StepBuilder("importShipmentStep", jobRepository)
                .<Object, Shipment>chunk(20, transactionManager)
                .reader(excelReader)
                .writer(shipmentWriter)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<Shipment> excelReader(
            @Value("#{jobParameters['filePath']}") String filePath,
            RowMapper<Shipment> rowMapper
    ){
        PoiItemReader<Shipment> reader = new PoiItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setRowMapper(rowMapper);
        return reader;
    }

    @Bean
    public RowMapper<Shipment> rowMapper() {
        return new ShipmentRowMapper();
    }

//    @Bean
//    public BeanWrapperRowMapper<Shipment> rowMapper() {
//        var rowMapper = new BeanWrapperRowMapper<Shipment>();
//        rowMapper.setTargetType(Shipment.class);
//        return rowMapper;
//    }
}
