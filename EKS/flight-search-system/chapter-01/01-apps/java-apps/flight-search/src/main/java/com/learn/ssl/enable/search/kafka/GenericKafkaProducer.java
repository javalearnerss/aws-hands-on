package com.learn.ssl.enable.search.kafka;

import com.learn.ssl.enable.search.utils.KafkaCallbackFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Generic reusable Kafka producer component.
 *
 * <p>
 * This producer supports publishing messages to Kafka topics
 * with or without message keys.
 * </p>
 *
 * <p>
 * It provides:
 * </p>
 *
 * <ul>
 *     <li>Asynchronous Kafka publishing</li>
 *     <li>Success logging</li>
 *     <li>Error logging</li>
 *     <li>Generic key/value support</li>
 * </ul>
 *
 * @param <K> Kafka message key type
 * @param <V> Kafka message value type
 */
@Component
public class GenericKafkaProducer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericKafkaProducer.class);

    /**
     * Spring Kafka template used for publishing messages.
     */
    private final KafkaTemplate<K, V> kafkaTemplate;

    public GenericKafkaProducer(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends Kafka message without a key.
     *
     * @param topic Kafka topic name
     * @param data message payload
     */
    public void send(String topic, V data) {

        LOGGER.info("Sending Kafka message topic={} payload={}", topic, data);

        CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, data);

        new KafkaCallbackFuture<>(future)
                .onSuccess(result ->
                        LOGGER.info("Message sent topic={} partition={} offset={} payload={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                data))
                .onFailure(ex ->
                        LOGGER.error("Unable to send message topic={} payload={} due to {}",
                                topic, data, ex.toString(), ex));

    }

    /**
     * Sends Kafka message with a key.
     *
     * <p>
     * Using a key ensures deterministic partitioning
     * and message ordering for the same key.
     * </p>
     *
     * @param topic Kafka topic name
     * @param key Kafka message key
     * @param data Kafka message payload
     */
    public void send(String topic, K key, V data) {

        LOGGER.info("Sending Kafka message topic={} key={} payload={}", topic, key, data);

        CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, key, data);

        new KafkaCallbackFuture<>(future)
                .onSuccess(result ->
                        LOGGER.info("Message sent topic={} partition={} offset={} key={} payload={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                key,
                                data))
                .onFailure(ex ->
                        LOGGER.error("Unable to send message topic={} key={} payload={} due to {}",
                                topic, key, data, ex.toString(), ex));

    }


}