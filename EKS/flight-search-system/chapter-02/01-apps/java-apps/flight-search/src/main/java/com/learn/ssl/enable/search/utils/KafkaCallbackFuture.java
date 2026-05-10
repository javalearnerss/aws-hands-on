package com.learn.ssl.enable.search.utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Utility wrapper around CompletableFuture
 * for handling Kafka async callbacks in a
 * clean and fluent way.
 *
 * <p>
 * This utility simplifies:
 * </p>
 *
 * <ul>
 *     <li>Success callback handling</li>
 *     <li>Error callback handling</li>
 *     <li>Fluent chaining style</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * new KafkaCallbackFuture<>(future)
 *      .onSuccess(result -> log.info("Success"))
 *      .onFailure(ex -> log.error("Failure"));
 * </pre>
 *
 * @param <T> CompletableFuture response type
 */
public class KafkaCallbackFuture<T> {

    /**
     * Underlying CompletableFuture instance.
     */
    private final CompletableFuture<T> completableFuture;

    /**
     * Creates callback wrapper for CompletableFuture.
     *
     * @param completableFuture async future instance
     */
    public KafkaCallbackFuture(CompletableFuture<T> completableFuture) {
        this.completableFuture = completableFuture;
    }

    /**
     * Registers success callback.
     *
     * <p>
     * Callback executes when Kafka publish succeeds.
     * </p>
     *
     * @param success success callback consumer
     * @return current KafkaCallbackFuture instance
     */
    public KafkaCallbackFuture<T> onSuccess(Consumer<T> success) {

        completableFuture.thenAccept(success);

        return this;
    }

    /**
     * Registers failure callback.
     *
     * <p>
     * Callback executes when Kafka publish fails.
     * </p>
     *
     * @param failure failure callback consumer
     * @return current KafkaCallbackFuture instance
     */
    public KafkaCallbackFuture<T> onFailure(Consumer<Throwable> failure) {

        completableFuture.exceptionally(ex -> {
            failure.accept(ex);
            return null;
        });

        return this;
    }
}