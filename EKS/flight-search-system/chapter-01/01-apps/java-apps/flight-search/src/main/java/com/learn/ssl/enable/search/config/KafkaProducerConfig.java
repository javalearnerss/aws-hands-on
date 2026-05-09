package com.learn.ssl.enable.search.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer configuration for Flight Search service.
 * This configuration creates:
 * 1. Kafka Producer Factory
 * 2. Kafka Template
 * Kafka producer properties are loaded from: application.properties / environment variables.
 */
//@Configuration
public class KafkaProducerConfig {

    @Autowired
    private Environment env;

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public DefaultKafkaProducerFactory<String, String> kafkaProducerFactory() {
        /* Loads default Kafka consumer properties, Additional producer properties are overridden below. */
        Map<String, Object> properties = new HashMap<>(kafkaProperties.buildConsumerProperties());

        /* Kafka bootstrap servers.  */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.producer.bootstrap-servers"));

        /* Number of bytes accumulated before sending batch. */
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.batch-size", "16384")));

        /*  Delay before sending batch messages. */
        properties.put(ProducerConfig.LINGER_MS_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.linger-ms", "5")));

        /* Compression type used for Kafka messages. */
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, env.getProperty("kafka.producer.compression-type", "lz4"));

        /* Producer acknowledgment strategy. all = strongest durability guarantee. */
        properties.put(ProducerConfig.ACKS_CONFIG, env.getProperty("kafka.producer.ack-strategy", "all"));

        /* Number of retry attempts. */
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.retries", "5")));

        /* Retry backoff interval in milliseconds. */
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.retry-backoff-ms", "1000")));

        /* Enables idempotent producer for duplicate prevention. */
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.parseBoolean(env.getProperty("kafka.producer.idempotence", "true")));

        /* Kafka request timeout. */
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.request-timeout", "30000")));

        /* Maximum delivery timeout. */
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, Integer.parseInt(env.getProperty("kafka.producer.delivery-timeout", "120000")));

        /* Kafka message key serializer. */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        /* Kafka message value serializer. */
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    /**
     * Creates KafkaTemplate used for sending Kafka messages.
     *
     * @return KafkaTemplate instance
     */
    @Bean
    public KafkaTemplate<String, String> kafkaProducer() {
        return new KafkaTemplate<String, String>(kafkaProducerFactory());
    }
}