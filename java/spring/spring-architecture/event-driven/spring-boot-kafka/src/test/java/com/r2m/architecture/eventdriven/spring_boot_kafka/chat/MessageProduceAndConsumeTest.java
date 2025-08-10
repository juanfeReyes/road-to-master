package com.r2m.architecture.eventdriven.spring_boot_kafka.chat;

import com.r2m.architecture.eventdriven.spring_boot_kafka.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessageProduceAndConsumeTest extends BaseIntegrationTest {

    @Test
    public void shouldPublishAndConsumeChatMessage() {
        given()
                .contentType(ContentType.JSON)
                .body("Test")
                .when()
                .post("/chat")
                .then()
                .statusCode(200);

        ArgumentCaptor<List<String>> messagesCaptor = ArgumentCaptor.forClass((Class) List.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> verify(chatConsumer, times(1))
                                .listen(messagesCaptor.capture()));
        List<String> messages = messagesCaptor.getValue();
        assertThat(messages).contains("Test");
        assertThat(messages).size().isEqualTo(1);
    }
}
