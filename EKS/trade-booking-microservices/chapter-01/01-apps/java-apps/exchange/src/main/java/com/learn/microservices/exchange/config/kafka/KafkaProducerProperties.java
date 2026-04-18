package com.learn.microservices.exchange.config.kafka;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.producer")
@Getter
@Setter
@NoArgsConstructor
public class KafkaProducerProperties {

    private String bootstrapServers;
    private Integer batchSize;
    private Long lingerMs;
    private String compressionType;
    private String ackStrategy;
    private Integer retries;
    private Long requestTimeout;
    private Long deliveryTimeout;
    private Boolean idempotence;
    private String keySerializer;
    private String valueSerializer;
    private Integer retryBackoffMs;
    private String transIdPrefix;

}
