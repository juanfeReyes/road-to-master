package com.r2m.architecture.eventdriven.spring_boot_kafka;

import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.ChatConsumer;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.OrderConsumer;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.ShipmentConsumer;
import com.r2m.architecture.eventdriven.spring_boot_kafka.messaging.KafkaConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(
        KafkaConfig.class
)
public class BaseIntegrationTest {

    @Container
    static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.10")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setup() throws InterruptedException {
        RestAssured.baseURI = "http://localhost:" + port;
        Thread.sleep(1000);
    }

    @MockitoSpyBean
    protected ChatConsumer chatConsumer;

    @MockitoSpyBean
    protected OrderConsumer orderConsumer;

    @MockitoSpyBean
    protected ShipmentConsumer shipmentConsumer;
}
