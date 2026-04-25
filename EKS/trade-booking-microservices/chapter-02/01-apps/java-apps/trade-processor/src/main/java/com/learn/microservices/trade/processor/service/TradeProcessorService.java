package com.learn.microservices.trade.processor.service;


import com.learn.microservices.trade.processor.exception.InvalidTradeException;
import com.learn.microservices.trade.processor.feign.RefDataProviderClient;
import com.learn.microservices.trade.processor.model.NseTrade;
import com.learn.microservices.trade.processor.model.TradeRefData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TradeProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeProcessorService.class);

    @Autowired
    private RefDataProviderClient refDataClient;

    @Autowired
    private NseTradeStoreService tradeStoreService;

    public void process(NseTrade trade) throws InvalidTradeException {
        TradeRefData refData = refDataClient.getIsin(trade.getSymbol());
        if (Objects.nonNull(refData)) {
            LOGGER.info("Reference data received for symbol={}, isin={}, status={}", trade.getSymbol(), refData.getIsin(), refData.getStatus());
            if (Status.FOUND.toString().equals(refData.getStatus())) {
                trade.setIsin(refData.getIsin());
            } else{
                throw  new InvalidTradeException("Reference data is missing, symbol="+trade.getSymbol());
            }
        }
        tradeStoreService.save(trade);
    }


    private enum Status {
        FOUND,
        NOT_FOUND
    }
}
