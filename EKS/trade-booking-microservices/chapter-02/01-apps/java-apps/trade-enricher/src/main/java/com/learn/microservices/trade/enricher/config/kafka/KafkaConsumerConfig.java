package com.learn.microservices.trade.enricher.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerProperties conproperties;

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public DefaultKafkaConsumerFactory<String, String> consumerFactory(){
        Map<String, Object> properties = new HashMap<>(kafkaProperties.buildConsumerProperties());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, conproperties.getConsumerGroup());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, conproperties.isAutoCommit());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, conproperties.getKeyDeserializer());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, conproperties.getValueDeserializer());

        // Stability / backpressure
        // Each poll() → max 200 messages
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);

        // If consumer doesn't call poll() for 5 min → Kafka kicks it out
        // If you don't poll → you are dead or stuck -> triggers rebalance
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);  // 5 min

        // If no heartbeat for 15 sec → consumer removed
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);


        // How often consumer tells broker: "I'm alive"
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5000);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // This fixes: "No Acknowledgment available..."
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        factory.setConcurrency(3);
        // Optional: concurrency (threads)
        // factory.setConcurrency(3);

        return factory;
    }

}
