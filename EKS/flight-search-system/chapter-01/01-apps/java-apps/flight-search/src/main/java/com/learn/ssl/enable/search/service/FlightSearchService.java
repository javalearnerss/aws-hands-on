package com.learn.ssl.enable.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ssl.enable.search.client.FlightAvailabilityFeignClient;
import com.learn.ssl.enable.search.dto.AnalyticsEvent;
import com.learn.ssl.enable.search.dto.FlightSearchRequest;
import com.learn.ssl.enable.search.dto.FlightSearchResponse;
import com.learn.ssl.enable.search.kafka.GenericKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for handling flight search operations.
 *
 * <p>
 * This service:
 * </p>
 *
 * <ul>
 *     <li>Calls downstream Flight Availability Service</li>
 *     <li>Builds analytics events</li>
 *     <li>Publishes analytics events to Kafka</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FlightSearchService {

    /* Kafka topic used for publishing flight search analytics events.  */
    @Value("${topic.flight-search}")
    private String flightSearchTopic;

    /** Feign client used for downstream Flight Availability Service communication. */
    private final FlightAvailabilityFeignClient feignClient;

    /** Generic Kafka producer used for publishing analytics events. */
    private final GenericKafkaProducer<String, String> genericKafkaProducer;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Searches available flights and publishes analytics events.
     *
     * @param request flight search request
     * @return available flight search response
     */
    public FlightSearchResponse searchFlights(FlightSearchRequest request) {

        log.info("Initiating flight search from={} to={} date={} travelers={}",
                request.getFrom(),
                request.getTo(),
                request.getTravelDate(),
                request.getTravelerCount());

        /* Call downstream flight availability service. */
        FlightSearchResponse response = feignClient.searchFlights(request);

        log.info("Received response from Flight Availability Service flightsFound={}",
                response.getFlights() != null ? response.getFlights().size() : 0);

        /* Create analytics event for Kafka publishing. */
        AnalyticsEvent event = AnalyticsEvent.builder()
                .eventType("FLIGHT_SEARCHED")
                .from(request.getFrom())
                .to(request.getTo())
                .travelDate(request.getTravelDate())
                .travelerCount(request.getTravelerCount())
                .timestamp(LocalDateTime.now().toString())
                .build();

        log.info("Publishing analytics event topic={} eventType={}",
                flightSearchTopic, event.getEventType());

        /* Publish analytics event to Kafka. */
        try {
            genericKafkaProducer.send(flightSearchTopic, mapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
           log.error("Error serializing analytics event for Kafka publishing", e);
        }
        log.info("Flight search completed successfully");
        return response;
    }
}