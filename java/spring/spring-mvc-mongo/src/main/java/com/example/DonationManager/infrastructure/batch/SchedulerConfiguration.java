package com.example.DonationManager.infrastructure.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;

@EnableScheduling
@Configuration
public class SchedulerConfiguration {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  @Qualifier("DonationSummaryJob")
  private Job donationSummaryJob;

  // TODO: Review the cron expression
  @Scheduled(cron = "0 */5 * * * *")
  public void runInventoryAllocationJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    var params  = new JobParametersBuilder()
        .addString("JobId", UUID.randomUUID().toString())
        .toJobParameters();
    jobLauncher.run(donationSummaryJob, params);
  }
}
