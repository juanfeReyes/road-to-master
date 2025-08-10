package com.r2m.architecture.eventdriven.spring_boot_kafka.shipment;

import com.r2m.architecture.eventdriven.spring_boot_kafka.BaseIntegrationTest;
import com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.producer.ChatProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.SHIPMENT_TOPIC;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class SerializingMessagesTest extends BaseIntegrationTest {

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, String> stringKafkaTemplate;

    @Autowired
    @Qualifier("stringifyObjectTemplate")
    private KafkaTemplate<String, Object> stringifyKafkaTemplate;

    @Autowired
    @Qualifier("jsonTemplate")
    private KafkaTemplate<String, Object> jsonKafkaTemplate;

    @Autowired
    private ChatProducer producer;

    @Test
    public void shouldConsumeStringMessage() {
        ProducerRecord<String, String> record = new ProducerRecord<>(SHIPMENT_TOPIC, "String message unit test");
        stringKafkaTemplate.send(record);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(shipmentConsumer)
                        .listen(eq("String message unit test")));
    }

    @Test
    public void shouldSerializeShipmentToString() {
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .address("123456")
                .build();

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(SHIPMENT_TOPIC, shipment);
        stringifyKafkaTemplate.send(record);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(shipmentConsumer)
                        .listen(eq(shipment.toString())));
    }

    @Test
    // TODO: fix the value parser configured in objectConsumerFactory
    public void shouldDeserializeStringToShipment() {
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .address("123456")
                .build();

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(SHIPMENT_TOPIC, shipment);
        stringifyKafkaTemplate.send(record);

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(shipmentConsumer)
                        .listen(payloadCaptor.capture()));
        Object payload = payloadCaptor.getValue();
    }

    @Test
    public void shouldConsumeJsonSerializeMessage() {
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .address("123456")
                .build();

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(SHIPMENT_TOPIC, shipment);
        jsonKafkaTemplate.send(record);

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(shipmentConsumer)
                        .listenJson(payloadCaptor.capture()));
        Object payload = payloadCaptor.getValue();
    }

    @Test
    public void shouldConsumeSpringMessage() {
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID())
                .address("123456")
                .build();

        Map<String, Object> headers = Map.of(
                KafkaHeaders.TOPIC, SHIPMENT_TOPIC
        );
        Message msg = new GenericMessage(shipment, headers);
        jsonKafkaTemplate.send(msg);

        ArgumentCaptor<Shipment> payloadCaptor = ArgumentCaptor.forClass(Shipment.class);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(shipmentConsumer)
                        .listenJsonMessage(payloadCaptor.capture()));
        Object payload = payloadCaptor.getValue();
    }
}
