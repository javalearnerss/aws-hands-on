package com.learn.ssl.enable.analytics.service;

import com.learn.ssl.enable.analytics.dto.AnalyticsDashboardResponse;
import com.learn.ssl.enable.analytics.dto.AnalyticsEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for processing
 * flight analytics events and generating
 * analytics dashboard data.
 *
 * <p>
 * This service maintains in-memory analytics
 * metrics for:
 * </p>
 *
 * <ul>
 *     <li>Total searches</li>
 *     <li>Total travelers</li>
 *     <li>Popular searched routes</li>
 * </ul>
 */
@Service
@Slf4j
public class FlightAnalyticsService {

    /**
     * Total number of flight searches.
     */
    private Long totalSearches = 0L;

    /**
     * Total number of travelers searched.
     */
    private Long totalTravelers = 0L;

    /**
     * Route-wise search count.
     *
     * Key Format:
     * FROM -> TO
     */
    private final Map<String, Integer> routeSearchCount =
            new HashMap<>();

    /**
     * Initializes analytics service.
     */
    @PostConstruct
    public void init() {

        log.info("FlightAnalyticsService initialized successfully");
    }

    /**
     * Processes analytics event received from Kafka.
     *
     * @param event analytics event
     */
    public synchronized void processAnalyticsEvent(
            AnalyticsEvent event) {

        log.info(
                "Processing analytics event type={} from={} to={} travelers={}",
                event.getEventType(),
                event.getFrom(),
                event.getTo(),
                event.getTravelerCount()
        );

        /**
         * Increment total searches.
         */
        totalSearches++;

        /**
         * Increment total travelers.
         */
        totalTravelers += event.getTravelerCount();

        /**
         * Build route key.
         */
        String route = event.getFrom() + " -> " + event.getTo();

        /**
         * Update route search count.
         */
        routeSearchCount.put(
                route,
                routeSearchCount.getOrDefault(route, 0) + 1
        );

        log.info(
                "Analytics updated totalSearches={} totalTravelers={} route={}",
                totalSearches,
                totalTravelers,
                route
        );
    }

    /**
     * Retrieves analytics dashboard response.
     *
     * <p>
     * Returns only top 2 most searched routes.
     * </p>
     *
     * @return analytics dashboard data
     */
    public AnalyticsDashboardResponse getDashboardData() {

        log.info("Fetching analytics dashboard data");

        /**
         * Get top 2 routes with highest searches.
         */
        Map<String, Integer> topRoutes =
                routeSearchCount.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue(
                                Comparator.reverseOrder()
                        ))
                        .limit(2)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new
                        ));

        AnalyticsDashboardResponse response =
                AnalyticsDashboardResponse.builder()
                        .totalSearches(totalSearches)
                        .totalTravelers(totalTravelers)
                        .popularRoutes(topRoutes)
                        .build();

        log.info(
                "Analytics dashboard generated successfully routesCount={}",
                topRoutes.size()
        );

        return response;
    }
}