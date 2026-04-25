package com.learn.microservices.refdata.provider.model;

/** * Stats is a record class that encapsulates various metrics related to symbol lookup requests in the application.
 *
 * This class is used to represent the current state of metrics.
 *
 * The fields in this record are:
 * - received: Total number of symbol lookup requests received by the application.
 * - present: Number of symbol lookup requests that were successfully resolved (ISIN found).
 * - missing: Number of symbol lookup requests that could not be resolved (ISIN missing).
 * - processed: Number of symbol lookup requests that have been processed (either present or missing).
 * - pendingProcessing: Number of symbol lookup requests that are pending processing (received but not yet processed).
 */
public record Stats(long received, long present, long missing, long processed, long pendingProcessing) {
}
