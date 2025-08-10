package com.r2m.architecture.eventdriven.spring_boot_kafka.messaging;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.support.serializer.ToStringSerializer;

import java.util.Map;

@TestConfiguration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaUrl;

    @Bean
    @Qualifier("objectProducerFactory")
    public ProducerFactory<String, Object> objectProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ToStringSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @Qualifier("stringifyObjectTemplate")
    public KafkaTemplate<String, Object> stringifyObjectTemplate(
            @Qualifier("objectProducerFactory") ProducerFactory<String, Object> objectProducerFactory) {
        return new KafkaTemplate<>(objectProducerFactory);
    }


    @Bean
    @Qualifier("jsonProducerFactory")
    public ProducerFactory<String, Object> jsonProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                JsonDeserializer.VALUE_DEFAULT_TYPE, "java.lang.Object"
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @Qualifier("jsonTemplate")
    public KafkaTemplate<String, Object> jsonKafkaTemplate(
            @Qualifier("jsonProducerFactory") ProducerFactory<String, Object> objectProducerFactory) {
        return new KafkaTemplate<>(objectProducerFactory);
    }

    @Bean
    @Qualifier("bytesProducerFactory")
    public ProducerFactory<String, byte[]> bytesProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class,
                JsonDeserializer.VALUE_DEFAULT_TYPE, "java.lang.Object"
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @Qualifier("bytesTemplate")
    public KafkaTemplate<String, byte[]> bytesKafkaTemplate(
            @Qualifier("bytesProducerFactory") ProducerFactory<String, byte[]> objectProducerFactory) {
        return new KafkaTemplate<>(objectProducerFactory);
    }
}
