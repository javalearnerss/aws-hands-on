package com.learn.ssl.enable.availability.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightInventory {

    private String flightNo;
    private String from;
    private String to;
    private String departure;
    private String arrival;
    private Integer availableSeats;
    private Double price;
}