package com.learn.ssl.enable.analytics.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Kafka consumer configuration for Analytics service.
 *
 * <p>
 * This configuration creates:
 * </p>
 *
 * <ul>
 *     <li>Kafka Consumer Factory</li>
 *     <li>Kafka Listener Container Factory</li>
 * </ul>
 *
 * <p>
 * Kafka properties are loaded from:
 * application.properties / environment variables.
 * </p>
 */
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private Environment env;

    /**
     * Spring Kafka default properties.
     */
    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Creates Kafka Consumer Factory with all required
     * Kafka consumer configurations.
     *
     * @return configured DefaultKafkaConsumerFactory
     */
    @Bean
    public DefaultKafkaConsumerFactory<String, String> consumerFactory() {

        /*
         * Load default Kafka consumer properties.
         */
        Map<String, Object> properties =
                new HashMap<>(kafkaProperties.buildConsumerProperties());

        /*
         * Kafka bootstrap servers.
         */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                env.getProperty("spring.kafka.bootstrap-servers", "localhost:9092"));

        /*
         * Kafka consumer group id.
         */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,
                env.getProperty("consumer.props.consumer-group"));

        /*
         * Disable auto commit.
         * Offset commits will be handled manually.
         */
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                Boolean.parseBoolean(env.getProperty("consumer.props.auto-commit", "false")));

        /*
         * Kafka key deserializer.
         */
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        /*
         * Kafka value deserializer.
         */
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        /*
         * Maximum records returned in single poll().
         */
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);

        /*
         * Maximum delay between poll() calls.
         * If exceeded, consumer is removed from group.
         */
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

        /*
         * Consumer session timeout.
         * If no heartbeat received within timeout,
         * consumer is considered dead.
         */
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);

        /*
         * Interval for sending heartbeats
         * to Kafka broker.
         */
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    /**
     * Kafka listener container factory.
     *
     * <p>
     * Used internally by @KafkaListener methods.
     * </p>
     *
     * @param consumerFactory Kafka consumer factory
     * @return configured listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        /* Set Kafka consumer factory. */
        factory.setConsumerFactory(consumerFactory);

        /* Manual acknowledgment mode. Offsets are committed explicitly.  */
        factory.getContainerProperties().setAckMode(Objects.equals(env.getProperty("consumer.props.auto-commit"), "false") ? ContainerProperties.AckMode.MANUAL : ContainerProperties.AckMode.BATCH);

        /* Number of concurrent Kafka consumer threads. */
        factory.setConcurrency(3);

        return factory;
    }
}