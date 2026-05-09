package com.learn.ssl.enable.availability.service;

import com.learn.ssl.enable.availability.dto.FlightResponse;
import com.learn.ssl.enable.availability.dto.FlightSearchRequest;
import com.learn.ssl.enable.availability.dto.FlightSearchResponse;
import com.learn.ssl.enable.availability.store.FlightInventoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightAvailabilityService {

    /**
     * Master flight inventory.
     */
    private final FlightInventoryStore inventoryStore;

    public FlightSearchResponse searchFlights(
            FlightSearchRequest request) {

        log.info(
                "Searching flights from={} to={} travelers={}",
                request.getFrom(),
                request.getTo(),
                request.getTravelerCount()
        );

        /**
         * Filter flights from inventory.
         */
        List<FlightResponse> flights =
                inventoryStore.getInventory()
                        .stream()
                        .filter(flight ->
                                flight.getFrom().equalsIgnoreCase(request.getFrom())
                                        &&
                                        flight.getTo().equalsIgnoreCase(request.getTo())
                                        &&
                                        flight.getAvailableSeats() >= request.getTravelerCount()
                        )
                        .map(flight -> new FlightResponse(
                                flight.getFlightNo(),
                                flight.getDeparture(),
                                flight.getArrival(),
                                flight.getAvailableSeats(),
                                flight.getPrice()
                        ))
                        .toList();

        log.info(
                "Flights found={}",
                flights.size()
        );

        return FlightSearchResponse.builder()
                .flights(flights)
                .build();
    }
}