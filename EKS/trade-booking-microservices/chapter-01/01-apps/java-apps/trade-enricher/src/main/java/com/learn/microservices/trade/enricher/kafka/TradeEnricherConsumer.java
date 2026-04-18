package com.learn.microservices.trade.enricher.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.microservices.trade.enricher.exception.DelayException;
import com.learn.microservices.trade.enricher.metrics.TradePipelineMetrics;
import com.learn.microservices.trade.enricher.model.NseTrade;
import com.learn.microservices.trade.enricher.service.TradeEnricherProcessor;
import com.learn.microservices.trade.enricher.utils.JsonUtil;
import com.learn.microservices.trade.enricher.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka Consumer responsible for:
 * 1. Consuming raw trade messages from input topic
 * 2. Enriching trade data using business logic
 * 3. Publishing enriched trades to output topic
 * 4. Handling retries and Dead Letter Topic (DLT)
 */
@Component
public class TradeEnricherConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeEnricherConsumer.class);

    // ObjectMapper (not used directly here, using JsonUtil instead)
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TradeEnricherProcessor processor; // Business logic layer

    @Autowired
    private GenericKafkaProducer<String, String> producer; // Kafka producer for enriched data

    @Value("${producer.trade.enricher.topic}")
    private String enrichedTradesTopic;

    @Autowired
    private TradePipelineMetrics metrics; // Custom metrics (received, processed, failed, etc.)

    /**
     * Retry configuration:
     * - Retries ONLY for DelayException
     * - Max attempts = 4 (1 original + 3 retries)
     * - Retry topics will be created with suffix (e.g., topic-retry-0, topic-retry-1...)
     * - If all retries fail → message goes to DLT (topic-dlt)
     */
    @RetryableTopic(
            include = DelayException.class,
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            autoCreateTopics = "false",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            dltTopicSuffix = "-dlt"
    )

    @KafkaListener(
            topics = "${consumer.trade.enricher.topic}",
            groupId = "trade-enricher"
    )
    public void consume(
            @Payload String trade, // Raw JSON message
            @Header(value = KafkaHeaders.OFFSET) Long offset, // Kafka offset
            @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic, // Source topic
            Acknowledgment ack // Manual commit control
    ) throws Exception {

        LOGGER.info("Trade received | topic={} offset={} payload={}", topic, offset, trade);

        try {
            // Increment received counter
            metrics.incReceived();

            // Convert JSON string → Java object
            NseTrade tradeObject = JsonUtil.MAPPER.readValue(trade, NseTrade.class);

            // Apply enrichment business logic
            NseTrade enrichedTrade = processor.enrich(tradeObject);

            // Increment processed counter
            metrics.incProcessed();

            // Simulate delay (can represent downstream dependency latency)
            TimeUtil.sleep(100);

            // Publish enriched trade to output topic
            producer.send(
                    enrichedTradesTopic,
                    JsonUtil.MAPPER.writeValueAsString(enrichedTrade)
            );

            // Increment sent counter
            metrics.incSent();

        } catch (DelayException delayExp) {
            /**
             * This exception triggers retry mechanism
             * Spring will move message to retry topics automatically
             */
            LOGGER.error("Retrying message due to DelayException: {}", delayExp.getMessage());
            throw delayExp;

        } catch (Exception e) {
            /**
             * Any other exception:
             * - Message will NOT be retried (since not included in RetryableTopic)
             * - It will directly go to DLT after failure
             */
            metrics.incFailed();
            LOGGER.error("Moving message to DLT due to exception: {}", e.getMessage(), e);

            throw new Exception(e); // Wrapping exception (can be improved)
        }

        /**
         * Manual acknowledgment:
         * - Offset is committed ONLY after successful processing
         * - Prevents message loss
         */
        ack.acknowledge();
    }
}