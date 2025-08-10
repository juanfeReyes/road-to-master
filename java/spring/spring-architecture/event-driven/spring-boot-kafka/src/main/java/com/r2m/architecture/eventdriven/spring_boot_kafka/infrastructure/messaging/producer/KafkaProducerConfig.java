package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.Map;

import static com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.KafkaConfig.SHIPMENT_TOPIC;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaUrl;

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> shipmentContainer
    ){
        return new ReplyingKafkaTemplate<>(pf, shipmentContainer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> shipmentContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory
    ) {
        ConcurrentMessageListenerContainer<String, String> repliesContainer =
                containerFactory.createContainer(SHIPMENT_TOPIC);
        repliesContainer.getContainerProperties().setGroupId("shipmentGroup");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }
}
