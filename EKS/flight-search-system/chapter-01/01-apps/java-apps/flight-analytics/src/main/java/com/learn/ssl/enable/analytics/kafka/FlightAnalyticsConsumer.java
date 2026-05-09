package com.learn.ssl.enable.analytics.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ssl.enable.analytics.dto.AnalyticsEvent;
import com.learn.ssl.enable.analytics.service.FlightAnalyticsService;
import com.learn.ssl.enable.analytics.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer responsible for consuming
 * flight analytics events.
 *
 * <p>
 * This consumer listens to flight search events
 * published by Flight Search Service and updates
 * analytics metrics.
 * </p>
 */
@Component
public class FlightAnalyticsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightAnalyticsConsumer.class);

    /** Jackson ObjectMapper for JSON processing. */
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private FlightAnalyticsService analyticsService;

    /**
     * Kafka listener method for consuming
     * flight search analytics events.
     *
     * @param searchEvent raw Kafka JSON message
     * @param offset Kafka message offset
     * @param topic Kafka source topic
     * @param ack Kafka acknowledgment
     * @throws JsonProcessingException JSON parsing exception
     */
    @KafkaListener(
            topics = "${topic.flight-search}",
            groupId = "${consumer.props.consumer-group}"
    )
    public void consume(
            @Payload String searchEvent,
            @Header(value = KafkaHeaders.OFFSET) Long offset,
            @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack) throws JsonProcessingException {

        LOGGER.info("Received Kafka message topic={} offset={} payload={}", topic, offset, searchEvent);

        /* Convert JSON message into AnalyticsEvent DTO  */
        AnalyticsEvent analyticsEvent =  JsonUtil.MAPPER.readValue(searchEvent, AnalyticsEvent.class);

        LOGGER.info(
                "Processing analytics event type={} from={} to={}",
                analyticsEvent.getEventType(),
                analyticsEvent.getFrom(),
                analyticsEvent.getTo()
        );

        /* Process analytics event.  */
        analyticsService.processAnalyticsEvent(analyticsEvent);

        /* Manually acknowledge Kafka offset. */
        ack.acknowledge();

        LOGGER.info("Kafka message acknowledged successfully offset={}", offset );
    }
}