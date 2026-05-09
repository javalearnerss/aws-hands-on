package com.learn.ssl.enable.availability.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO representing flight search response.
 *
 * <p>
 * This response contains the list of available flights
 * returned from the Flight Availability Service.
 * </p>
 *
 * <p>
 * The response is sent back to frontend clients
 * such as Angular applications.
 * </p>
 */
@Data
@Builder
public class FlightSearchResponse {

    /**
     * List of available flights matching
     * the search criteria.
     */
    private List<FlightResponse> flights;

}