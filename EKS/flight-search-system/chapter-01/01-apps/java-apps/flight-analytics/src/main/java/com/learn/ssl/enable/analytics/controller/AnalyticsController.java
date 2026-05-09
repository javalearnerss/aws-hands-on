package com.learn.ssl.enable.analytics.controller;

import com.learn.ssl.enable.analytics.dto.AnalyticsDashboardResponse;
import com.learn.ssl.enable.analytics.service.FlightAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for exposing
 * analytics dashboard APIs.
 *
 * <p>
 * This controller provides analytics data
 * related to flight searches and trends.
 * </p>
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    /**
     * Service responsible for analytics processing.
     */
    private final FlightAnalyticsService analyticsService;

    /**
     * Retrieves analytics dashboard data.
     *
     * <p>
     * Example API:
     * </p>
     *
     * <pre>
     * GET /api/analytics/dashboard
     * </pre>
     *
     * @return analytics dashboard response
     */
    @GetMapping("/dashboard")
    public AnalyticsDashboardResponse getDashboard() {

        log.info("Received request for analytics dashboard");

        AnalyticsDashboardResponse response = analyticsService.getDashboardData();

        log.info("Successfully fetched analytics dashboard topSearches={}", response);

        return response;
    }
}