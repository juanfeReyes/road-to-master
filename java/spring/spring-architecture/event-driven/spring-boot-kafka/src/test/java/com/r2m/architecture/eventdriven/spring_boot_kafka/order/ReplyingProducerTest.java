package com.r2m.architecture.eventdriven.spring_boot_kafka.order;

import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification.NotificationService;
import com.r2m.architecture.eventdriven.spring_boot_kafka.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReplyingProducerTest extends BaseIntegrationTest {

    @MockitoSpyBean
    private NotificationService notificationService;

    /**
     * Replying producer send a message and allow to wait (given timeout) for a processing response
     * By using completable futures we can wait for processing synchronously
     */
    @Test
    public void shouldSendNotificationAfterProcessing() {
        given()
                .contentType(ContentType.JSON)
                .body("unit test order")
                .when()
                .post("/orders")
                .then()
                .statusCode(200);

        // Processing step
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> verify(orderConsumer, times(1))
                                .listen(payloadCaptor.capture(), anyString()));
        String messages = payloadCaptor.getValue();
        assertThat(messages).isEqualTo("unit test order");

        // Waiting step to send notification after processing
        ArgumentCaptor<Object> notificationCaptor = ArgumentCaptor.forClass(Object.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> verify(notificationService, times(1))
                                .execute(notificationCaptor.capture(), eq("Synced order in CreateOrderService")));
        Object notificationContent = notificationCaptor.getValue();
        assertThat(notificationContent).isEqualTo("Order processed for order: unit test order");
    }
}
