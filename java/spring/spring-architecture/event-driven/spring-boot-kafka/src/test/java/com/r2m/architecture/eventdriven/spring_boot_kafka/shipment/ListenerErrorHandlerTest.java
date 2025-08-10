package com.r2m.architecture.eventdriven.spring_boot_kafka.shipment;

import com.r2m.architecture.eventdriven.spring_boot_kafka.BaseIntegrationTest;
import com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.SHIPMENT_TOPIC;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class ListenerErrorHandlerTest extends BaseIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> shipmentKafkaTemplate;

    @MockitoSpyBean
    private NotificationService notificationService;

    @Test
    public void shouldHandleValidationError() throws ExecutionException, InterruptedException, TimeoutException {
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .address("123456")
                .build();

        SendResult<String, Object> result = shipmentKafkaTemplate
                .send(SHIPMENT_TOPIC, shipment)
                .get(5, TimeUnit.SECONDS);

        ArgumentCaptor<Object> notificationCaptor = ArgumentCaptor.forClass(Object.class);
        await()
                .atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(notificationService)
                        .execute(notificationCaptor.capture(), eq("")));
        Shipment messageWithError = (Shipment) notificationCaptor.getValue();
    }
}
