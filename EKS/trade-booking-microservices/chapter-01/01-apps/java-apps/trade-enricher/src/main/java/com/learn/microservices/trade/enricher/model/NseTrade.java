package com.learn.microservices.trade.enricher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NseTrade {

    private String exchange;
    private LocalDate tradeDate;
    private LocalTime tradeTime;
    private String symbol;

    private String tradeId;
    private String orderId;
    private String exchangeOrderId;

    private String buySell;
    private int quantity;
    private BigDecimal price;
    private BigDecimal tradeValue;

    private String clientCode;
    private String brokerCode;
    private String moneyManagerCode;
    private String moneyManagerName;

    private LocalDateTime timestampExchange;

}
