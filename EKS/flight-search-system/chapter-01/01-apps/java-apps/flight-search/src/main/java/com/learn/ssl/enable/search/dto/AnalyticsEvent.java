package com.learn.ssl.enable.search.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO representing analytics event data published to Kafka.
 *
 * <p>
 * This event is generated whenever a user performs a flight search.
 * The event can be consumed by downstream analytics, reporting,
 * recommendation, or monitoring services.
 * </p>
 *
 * <p>
 * Example event types:
 * </p>
 *
 * <pre>
 * FLIGHT_SEARCH
 * FLIGHT_BOOKING
 * SEARCH_FAILED
 * </pre>
 */
@Data
@Builder
public class AnalyticsEvent {

    /**
     * Type of analytics event.
     *
     * Example:
     * FLIGHT_SEARCH
     */
    private String eventType;

    /**
     * Source location / departure city.
     *
     * Example:
     * Bangalore
     */
    private String from;

    /**
     * Destination location / arrival city.
     *
     * Example:
     * Delhi
     */
    private String to;

    /**
     * Flight travel date.
     *
     * Example:
     * 2026-05-10
     */
    private String travelDate;

    /**
     * Total number of travellers.
     */
    private Integer travelerCount;

    /**
     * Event generation timestamp.
     *
     * Example:
     * 2026-05-09T18:30:00Z
     */
    private String timestamp;

}