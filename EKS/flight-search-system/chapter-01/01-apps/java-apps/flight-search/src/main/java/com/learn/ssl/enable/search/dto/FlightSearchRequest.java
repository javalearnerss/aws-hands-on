package com.learn.ssl.enable.search.dto;

import lombok.Data;

/**
 * DTO representing a flight search request.
 *
 * <p>
 * This request is received from frontend clients
 * such as Angular applications to search available flights.
 * </p>
 *
 * <p>
 * The request contains source location, destination,
 * travel date, and traveller count.
 * </p>
 */
@Data
public class FlightSearchRequest {

    /**
     * Source / departure location.
     *
     * Example:
     * Bangalore
     */
    private String from;

    /**
     * Destination / arrival location.
     *
     * Example:
     * Delhi
     */
    private String to;

    /**
     * Journey travel date.
     *
     * Example:
     * 2026-05-10
     */
    private String travelDate;

    /**
     * Total number of travellers.
     */
    private Integer travelerCount;

}