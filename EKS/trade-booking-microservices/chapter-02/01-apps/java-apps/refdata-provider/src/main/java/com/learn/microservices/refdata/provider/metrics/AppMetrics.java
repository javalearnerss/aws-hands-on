package com.learn.microservices.refdata.provider.metrics;

import com.learn.microservices.refdata.provider.model.Stats;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAdder;

/**
 * AppMetrics is a Spring Component responsible for tracking and maintaining various metrics related to symbol lookup requests.
 *
 * Metrics Tracked:
 * 1. RECEIVED: Total number of symbol lookup requests received by the application.
 * 2. PRESENT: Number of symbol lookup requests that were successfully resolved (ISIN found).
 * 3. MISSING: Number of symbol lookup requests that could not be resolved (ISIN missing).
 * 4. PROCESSED: Number of symbol lookup requests that have been processed (either present or missing).
 *
 * The class provides methods to increment each metric and a method to retrieve the current statistics as a Stats object.
 *
 * Note: LongAdder is used for efficient thread-safe counting in high-concurrency scenarios.
 */
@Component
public class AppMetrics {

    /* Represents the total number of symbol lookup requests received by the application. */
    private static final LongAdder RECEIVED = new LongAdder();

    /* Represents the number of symbol lookup requests that were successfully resolved (i.e., ISIN found). */
    private static final LongAdder PRESENT = new LongAdder();

    /* Represents the number of symbol lookup requests that could not be resolved (i.e., ISIN missing). */
    private static final LongAdder MISSING = new LongAdder();

    /* Represents the number of symbol lookup requests that have been processed (either present or missing). */
    private static final LongAdder PROCESSED = new LongAdder();

    public void incrementReceived() {
        RECEIVED.increment();
    }

    public void incrementPresent() {
        PRESENT.increment();
    }


    public void incrementMissing() {
        MISSING.increment();
    }

    public void incrementProcessed() {
        PROCESSED.increment();
    }

    public Stats getStats() {
        return new Stats(RECEIVED.sum(), PRESENT.sum(), MISSING.sum(), PROCESSED.sum(), RECEIVED.sum() - PROCESSED.sum());
    }

}
