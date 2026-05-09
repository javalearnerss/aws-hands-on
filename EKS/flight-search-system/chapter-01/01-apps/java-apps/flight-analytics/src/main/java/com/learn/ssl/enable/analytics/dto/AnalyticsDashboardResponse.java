package com.learn.ssl.enable.analytics.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * DTO representing analytics dashboard response.
 *
 * <p>
 * This response contains aggregated analytics
 * information related to flight searches.
 * </p>
 */
@Data
@Builder
public class AnalyticsDashboardResponse {

    /**
     * Total number of flight searches performed.
     */
    private Long totalSearches;

    /**
     * Total number of travelers searched across all requests.
     */
    private Long totalTravelers;

    /**
     * Popular searched routes with search count.
     *
     * <p>
     * Example:
     * </p>
     *
     * <pre>
     * Bangalore-Delhi -> 120
     * Mumbai-Hyderabad -> 75
     * </pre>
     */
    private Map<String, Integer> popularRoutes;
}