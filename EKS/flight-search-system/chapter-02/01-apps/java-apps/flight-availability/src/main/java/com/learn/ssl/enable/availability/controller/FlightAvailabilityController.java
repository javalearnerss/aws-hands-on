package com.learn.ssl.enable.availability.controller;

import com.learn.ssl.enable.availability.dto.FlightSearchRequest;
import com.learn.ssl.enable.availability.dto.FlightSearchResponse;
import com.learn.ssl.enable.availability.service.FlightAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling
 * flight availability APIs.
 *
 * <p>
 * This controller exposes internal APIs used by
 * downstream services such as Flight Search Service.
 * </p>
 */
@RestController
@RequestMapping("/internal/flights")
@RequiredArgsConstructor
@Slf4j
public class FlightAvailabilityController {

    /**
     * Service responsible for retrieving
     * available flight details.
     */
    private final FlightAvailabilityService availabilityService;

    /**
     * Retrieves available flights based on
     * incoming flight search request.
     *
     * @param request flight search request
     * @return available flights response
     */
    @PostMapping("/availability")
    public FlightSearchResponse getAvailableFlights(
            @RequestBody FlightSearchRequest request) {

        log.info(
                "Received flight availability request from={} to={} date={} travelers={}",
                request.getFrom(),
                request.getTo(),
                request.getTravelDate(),
                request.getTravelerCount()
        );

        FlightSearchResponse response =
                availabilityService.searchFlights(request);

        log.info(
                "Flight availability search completed flightsFound={}",
                response.getFlights() != null
                        ? response.getFlights().size()
                        : 0
        );

        return response;
    }

    /**
     * Retrieves all available flights.
     *
     * @return available flights response
     */
    @GetMapping("/all")
    public FlightSearchResponse getAvailableFlights() {

        log.info("Received request to fetch all available flights");

        FlightSearchResponse response =
                availabilityService.searchFlights(null);

        log.info(
                "Fetched all available flights flightsFound={}",
                response.getFlights() != null
                        ? response.getFlights().size()
                        : 0
        );

        return response;
    }
}