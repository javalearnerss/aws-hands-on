package com.learn.microservices.refdata.provider.service;

import com.learn.microservices.refdata.provider.entity.ReferenceData;
import com.learn.microservices.refdata.provider.repository.ReferenceDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for fetching ISIN for a given symbol.
 *
 * Caching Strategy:
 * - Uses an in-memory HashMap to cache symbol → ISIN mappings.
 * - On cache miss, queries the database and updates the cache.
 *
 */
@Service
public class ReferenceDataService {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceDataService.class);

    // In-memory cache for symbol → ISIN mapping
    Map<String, String> symbolIsinCache = new HashMap<>();

    private final ReferenceDataRepository refDataRepository;

    public ReferenceDataService(ReferenceDataRepository refDataRepository) {
        this.refDataRepository = refDataRepository;
    }

    public String getIsin(String symbol) {
        logger.info("Fetching ISIN for symbol: {}", symbol);

        if (symbol == null || symbol.isBlank()) {
            logger.info("Invalid symbol provided: '{}'. Returning null.", symbol);
            return null;
        }

        String normalizedSymbol = symbol.trim().toUpperCase();

        return symbolIsinCache.computeIfAbsent(normalizedSymbol, sys -> {
            logger.info("Cache miss for symbol: {}. Querying database...", normalizedSymbol);
            Optional<ReferenceData> refData = refDataRepository.findBySymbol(normalizedSymbol);
            return refData.map(ReferenceData::getIsin).orElse(null);
        });

    }

}
