package com.r2m.batch_api.application.service.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImportShipment {

    private final JobLauncher jobLauncher;

    @Qualifier("excelShipmentJob")
    private final Job excelShipmentJob;

    public void execute(MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        // Store file in temp file
        var tmpPath = Paths.get("tmp/" + Instant.now().toEpochMilli());

        if (!Files.exists(tmpPath)) {
            Files.createDirectories(tmpPath);
        }
        var filePath = tmpPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // launch job with file location
        var jobParams = new JobParametersBuilder()
                .addString("filePath", filePath.toString())
                .toJobParameters();
        jobLauncher.run(excelShipmentJob, jobParams);
    }
}
