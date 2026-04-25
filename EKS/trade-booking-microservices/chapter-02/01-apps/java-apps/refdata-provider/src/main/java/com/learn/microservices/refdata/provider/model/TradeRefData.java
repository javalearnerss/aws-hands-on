package com.learn.microservices.refdata.provider.model;

import lombok.*;

/** * Model class representing the reference data for a trade.
 *
 * This class encapsulates the symbol, ISIN, and status of a trade. It is used to transfer
 * trade reference data between different layers of the application, such as from the service
 * layer to the controller layer.
 *
 * The 'symbol' field represents the stock symbol (e.g., "TCS"), while the 'isin' field represents
 * the International Securities Identification Number (e.g., "INE467B01029"). The 'status' field
 * indicates whether the trade reference data is present or missing in the cache.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TradeRefData {
    private String symbol;
    private String isin;
    private String status;

}
