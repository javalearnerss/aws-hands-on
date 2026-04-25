import { environment } from "../../environments/environment";

export const API = {
    baseUrl: environment.url,
    exchangeUrl: `${environment.url}/api/v1/trades`,
    tradeEnricherUrl: `${environment.url}/api/v1/enrichment`,
    tradeProcessorUrl: `${environment.url}/api/v1/processing`,
    refdataProviderUrl: `${environment.url}/api/v1/reference`
};