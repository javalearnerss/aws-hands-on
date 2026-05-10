package com.learn.ssl.enable.search.client;

import com.learn.ssl.enable.search.dto.FlightSearchRequest;
import com.learn.ssl.enable.search.dto.FlightSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client used to communicate with the Flight Availability Service.
 *
 * <p>
 * This client sends internal REST requests to retrieve available flights
 * based on source, destination, travel date, and passenger count.
 * </p>
 *
 */
@FeignClient(name = "flight-availability-service", url = "${flight.availability.url}")
public interface FlightAvailabilityFeignClient {

    /**
     * Retrieves available flights for the given search criteria.
     *
     * @param request flight search request containing:
     *                source, destination, travel date,
     *                passenger count, etc.
     *
     * @return available flight search response
     */
    @PostMapping("/internal/flights/availability")
    FlightSearchResponse searchFlights(@RequestBody FlightSearchRequest request);

}