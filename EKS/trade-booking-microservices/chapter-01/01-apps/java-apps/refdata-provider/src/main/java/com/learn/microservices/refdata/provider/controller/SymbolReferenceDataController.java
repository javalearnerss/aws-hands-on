package com.learn.microservices.refdata.provider.controller;

import com.learn.microservices.refdata.provider.metrics.AppMetrics;
import com.learn.microservices.refdata.provider.model.Stats;
import com.learn.microservices.refdata.provider.model.TradeRefData;
import com.learn.microservices.refdata.provider.service.ReferenceDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller responsible for providing Symbol → ISIN reference data.
 *
 * Supported APIs:
 * 1. Single symbol lookup
 *      GET /reference/isin/{symbol}
 *
 * 2. Bulk symbol lookup (comma separated)
 *      GET /reference/isin?symbols=AAA,BBB,CCC
 *
 * Data Source:
 * Uses SymbolIsinCacheService which fetches data from in-memory cache.
 *
 * Response Model:
 * Uses TradeRefData DTO for standardized response structure.
 */
@RestController
@RequestMapping("/reference")
public class SymbolReferenceDataController {

    private static final Logger logger = LoggerFactory.getLogger(SymbolReferenceDataController.class);

    @Autowired
    private AppMetrics metrics;

    /** Cache service used to fetch ISIN using symbol.  */
    private final ReferenceDataService cacheService;

    /** Constructor Injection (Recommended Spring Practice)  */
    public SymbolReferenceDataController(ReferenceDataService cacheService) {
        this.cacheService = cacheService;
    }


    /**
     * Fetch ISIN for a single symbol.
     *
     * Example:
     * GET /reference/isin/TCS
     *
     * @param symbol Raw symbol from request path
     * @return TradeRefData containing symbol, isin and lookup status
     */
    @GetMapping("symbol/{symbol}")
    public ResponseEntity<TradeRefData> getByPath(@PathVariable String symbol) {

        metrics.incrementReceived();
        long startNs = System.nanoTime();
        logger.info("ISIN lookup requested (single). rawSymbol='{}'", symbol);

        // Normalize input → trim + uppercase
        String normalized = normalize(symbol);
        logger.debug("ISIN lookup (single). normalizedSymbol='{}'", normalized);

        // Fetch ISIN from cache
        String isin = cacheService.getIsin(normalized);

        // If symbol not found → return NOT_FOUND status
        if (isin == null) {
            metrics.incrementMissing();
            long tookMs = (System.nanoTime() - startNs) / 1_000_000;
            logger.warn("ISIN lookup result (single). symbol='{}' status=NOT_FOUND tookMs={}",
                    normalized, tookMs);
            metrics.incrementProcessed();
            return ResponseEntity.ok(new TradeRefData(normalized, null, "NOT_FOUND"));
        }

        metrics.incrementPresent();
        // If found → return FOUND status with ISIN
        long tookMs = (System.nanoTime() - startNs) / 1_000_000;
        logger.info("ISIN lookup result (single). symbol='{}' status=FOUND isin='{}' tookMs={}",
                normalized, isin, tookMs);

        metrics.incrementProcessed();
        return ResponseEntity.ok(new TradeRefData(normalized, isin, "FOUND"));
    }

    // =========================================================
    // BULK SYMBOL LOOKUP
    // =========================================================

    /**
     * Fetch ISIN for multiple symbols (comma separated).
     *
     * Example:
     * GET /reference/isin?symbols=TCS,INFY,RELIANCE
     *
     * Features:
     * - Input normalization
     * - Duplicate symbol removal
     * - Order preservation
     * - Partial success handling
     *
     * @param symbolsInput Comma separated symbol string
     * @return JSON response containing count and list of TradeRefData
     */
    @GetMapping("symbol")
    public ResponseEntity<Map<String, Object>> getBulk(@RequestParam("symbols") String symbolsInput) {
        metrics.incrementReceived();
        long startNs = System.nanoTime();
        int rawLen = (symbolsInput == null) ? 0 : symbolsInput.length();
        logger.info("ISIN lookup requested (bulk). rawLength={} rawInput='{}'", rawLen, symbolsInput);

        // Validate input
        if (symbolsInput == null || symbolsInput.isBlank()) {
            logger.warn("ISIN lookup rejected (bulk). reason='symbols parameter is required'");
            return ResponseEntity.badRequest().body(Map.of("error", "symbols parameter is required"));
        }

        /**
         * LinkedHashSet used because:
         * 1. Removes duplicates
         * 2. Preserves original request order
         */
        Set<String> normalizedSymbols = new LinkedHashSet<>();

        // Split comma separated symbols and normalize each
        for (String sym : symbolsInput.split(",")) {
            String normalized = normalize(sym);
            if (!normalized.isEmpty()) {
                normalizedSymbols.add(normalized);
            }
        }

        logger.debug("ISIN lookup (bulk). normalizedCount={} normalizedSymbols={}",
                normalizedSymbols.size(), normalizedSymbols);

        // Prepare response list
        List<TradeRefData> data = new ArrayList<>(normalizedSymbols.size());

        int found = 0, notFound = 0;

        // Fetch ISIN for each symbol
        for (String sym : normalizedSymbols) {
            String isin = cacheService.getIsin(sym);

            if (isin == null) {
                metrics.incrementMissing();
                notFound++;
                data.add(new TradeRefData(sym, null, "NOT_FOUND"));
                logger.debug("ISIN lookup (bulk item). symbol='{}' status=NOT_FOUND", sym);
            } else {
                metrics.incrementPresent();
                found++;
                data.add(new TradeRefData(sym, isin, "FOUND"));
                logger.debug("ISIN lookup (bulk item). symbol='{}' status=FOUND isin='{}'", sym, isin);
            }
        }

        long tookMs = (System.nanoTime() - startNs) / 1_000_000;
        logger.info("ISIN lookup completed (bulk). uniqueSymbols={} found={} notFound={} tookMs={}",
                normalizedSymbols.size(), found, notFound, tookMs);

        metrics.incrementProcessed();;
        // Final response wrapper
        return ResponseEntity.ok(Map.of("count", data.size(), "data", data));
    }


    /**
     * Normalizes symbol input:
     * - Null safe
     * - Trim spaces
     * - Convert to uppercase (locale safe)
     */
    private String normalize(String s) {
        return s == null ? "" : s.trim().toUpperCase(Locale.ROOT);
    }

    @GetMapping("symbol/stats")
    public ResponseEntity<Stats> getStats(){
        logger.info("Metrics stats requested");
        return ResponseEntity.ok(metrics.getStats());
    }
}
