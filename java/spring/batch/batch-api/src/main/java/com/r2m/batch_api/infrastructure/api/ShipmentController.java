package com.r2m.batch_api.infrastructure.api;

import com.r2m.batch_api.application.service.shipment.ImportShipment;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller to launch a shipment processing
 */
@RestController
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentController {

    private final ImportShipment importShipment;

    @PostMapping("/import")
    public void importShipments(@RequestPart("file") MultipartFile multipartFile) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, IOException, JobParametersInvalidException, JobRestartException {
        importShipment.execute(multipartFile);
    }
}
