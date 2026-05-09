package com.learn.ssl.enable.availability.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single flight availability response.
 *
 * <p>
 * This object contains flight details returned from the
 * Flight Availability Service.
 * </p>
 *
 * <p>
 * It is used as part of the FlightSearchResponse payload.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponse {

    /**
     * Unique flight number.
     *
     * Example:
     * AI-202
     */
    private String flightNo;

    /**
     * Flight departure location.
     *
     * Example:
     * Bangalore
     */
    private String departure;

    /**
     * Flight arrival location.
     *
     * Example:
     * Delhi
     */
    private String arrival;

    /**
     * Total available seats for the flight.
     */
    private Integer availableSeats;

    /**
     * Flight ticket price.
     */
    private Double price;

}