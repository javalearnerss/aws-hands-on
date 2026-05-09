package com.learn.ssl.enable.availability.store;

import com.learn.ssl.enable.availability.dto.FlightResponse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory flight inventory store.
 *
 * Simulates flights available in airline systems.
 */
@Component
@Data
public class FlightInventoryStore {

    private final List<FlightInventory> inventory = new ArrayList<>();

    @PostConstruct
    public void loadFlights() {

        inventory.add(new FlightInventory(
                "AI101",
                "Bangalore",
                "Delhi",
                "10:00",
                "12:30",
                20,
                6500.0
        ));

        inventory.add(new FlightInventory(
                "6E202",
                "Bangalore",
                "Delhi",
                "14:00",
                "16:20",
                12,
                7200.0
        ));

        inventory.add(new FlightInventory(
                "UK303",
                "Delhi",
                "Hyderabad",
                "18:00",
                "20:45",
                5,
                8100.0
        ));
    }


}