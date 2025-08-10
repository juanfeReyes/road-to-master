package com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer;


import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.messaging.consumer.interceptors.LoggingConsumerInterceptor;
import com.r2m.architecture.eventdriven.spring_boot_kafka.infrastructure.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ParseStringDeserializer;

import java.util.List;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaUrl;

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, LoggingConsumerInterceptor.class.getName()
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringListenerContainer(
            ConsumerFactory<String, String> stringConsumerFactory,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }

    @Bean
    @Qualifier("objectConsumerFactory")
    public ConsumerFactory<String, Object> objectConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ParseStringDeserializer.class,
                ParseStringDeserializer.VALUE_PARSER, "com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment.parse",
                ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, LoggingConsumerInterceptor.class.getName()
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    @Qualifier("objectListenerContainer")
    public ConcurrentKafkaListenerContainerFactory<String, Object> objectListenerContainer(
            @Qualifier("objectConsumerFactory") ConsumerFactory<String, Object> stringConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        return factory;
    }

    @Bean
    @Qualifier("jsonObjectConsumerFactory")
    public ConsumerFactory<String, Object> jsonObjectConsumerFactory() {
        List<String> trustedPackages = List.of("com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment");
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*",
                ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, LoggingConsumerInterceptor.class.getName()
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    @Qualifier("jsonListenerContainer")
    public ConcurrentKafkaListenerContainerFactory<String, Object> jsonListenerContainer(
            @Qualifier("jsonObjectConsumerFactory") ConsumerFactory<String, Object> stringConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        return factory;
    }

    @Bean
    @Qualifier("jsonMessageConsumerFactory")
    public ConsumerFactory<String, Bytes> jsonMessageConsumerFactory() {
        List<String> trustedPackages = List.of("com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment");
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*",
                ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, LoggingConsumerInterceptor.class.getName()
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    @Qualifier("jsonMessageListenerContainer")
    public KafkaListenerContainerFactory<?> jsonMessageListenerContainer(
//            @Qualifier("jsonMessageConsumerFactory") ConsumerFactory<String, Bytes> stringConsumerFactory
            ConsumerFactory<String, String> stringConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory);
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<?> batchFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        return factory;
    }

//    @Bean
//    public ConsumerFactory<String, Object> multiConsumerFactory() {
//        Map<String, Object> configProps = Map.of(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl,
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
//                ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class,
//                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName(),

    /// /                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
    /// /                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
    /// /                JsonDeserializer.TYPE_MAPPINGS, "shipment:com.r2m.architecture.eventdriven.spring_boot_kafka.domain.Shipment",
//                ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, LoggingConsumerInterceptor.class.getName()
//        );
//        return new DefaultKafkaConsumerFactory<>(configProps);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> multiKafkaListenerContainerFactory(
//            ConsumerFactory<String, Object> multiConsumerFactory
//    ) {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(multiConsumerFactory);
//        return factory;
//    }
    @Bean
    public KafkaListenerErrorHandler validationErrorHandler(NotificationService notificationService) {
        return (m, e) -> {
            log.error("Error {} processing message: {}", e.getCause(), m);
            notificationService.execute(m, "validationErrorHandler handle error");
            return "FAILED";
        };
    }
}
