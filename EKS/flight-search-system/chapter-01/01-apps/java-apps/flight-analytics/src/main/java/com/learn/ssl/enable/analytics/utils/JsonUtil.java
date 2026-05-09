package com.learn.ssl.enable.analytics.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class for shared JSON serialization
 * and deserialization operations.
 *
 * <p>
 * Provides a preconfigured Jackson ObjectMapper
 * instance used across the application.
 * </p>
 *
 * <p>
 * Configurations:
 * </p>
 *
 * <ul>
 *     <li>Java 8 Date/Time support</li>
 *     <li>ISO date format serialization</li>
 * </ul>
 */
public class JsonUtil {

    /**
     * Shared ObjectMapper instance.
     *
     * <p>
     * Features enabled:
     * </p>
     *
     * <ul>
     *     <li>JavaTimeModule for LocalDate/LocalDateTime support</li>
     *     <li>Disable timestamp-based date serialization</li>
     * </ul>
     */
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

}