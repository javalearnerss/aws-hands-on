package com.learn.ssl.enable.search.controller;

import com.learn.ssl.enable.search.dto.FlightSearchRequest;
import com.learn.ssl.enable.search.dto.FlightSearchResponse;
import com.learn.ssl.enable.search.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling flight search APIs.
 *
 * <p>
 * This controller receives flight search requests from frontend clients
 * such as Angular applications and delegates processing to the
 * FlightSearchService.
 * </p>
 */
@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Slf4j
public class FlightSearchController {

    /** Flight search service that contains business logic for processing flight search requests. */
    private final FlightSearchService flightSearchService;

    /**
     * Endpoint to search for available flights based on search criteria.
     *
     * @param request flight search request containing:
     *                source, destination, travel date,
     *                passenger count, etc.
     *
     * @return flight search response containing list of available flights
     */
    @PostMapping("/search")
    public FlightSearchResponse searchFlights(@RequestBody FlightSearchRequest request) {

        log.info("Received flight search request from={} to={} date={} travellers={}",
                request.getFrom(), request.getTo(), request.getTravelDate(), request.getTravelerCount());

        FlightSearchResponse response = flightSearchService.searchFlights(request);

        log.info("Flight search completed successfully. flightsFound={}",
                response.getFlights() != null ? response.getFlights().size() : 0);

        return response;
    }
}
