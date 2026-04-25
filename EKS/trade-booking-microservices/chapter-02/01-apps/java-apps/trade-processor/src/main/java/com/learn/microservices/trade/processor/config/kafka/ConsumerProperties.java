package com.learn.microservices.trade.processor.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "consumer.props")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerProperties {

    private String consumerGroup;
    private boolean autoCommit;
    private String keyDeserializer;
    private String valueDeserializer;
    private String ackMode;


}
