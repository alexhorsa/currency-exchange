package com.demo.currencyexchange;

import io.reactivex.Single;

public class RatesRepository {

    private RatesApiClient.ExchangeRatesApi ratesApi;

    private RatesApiResponse cachedRates;

    private boolean cacheIsDirty = false;

    RatesRepository() {
        ratesApi = RatesApiClient.createService(RatesApiClient.ExchangeRatesApi.class);
    }

    public Single<RatesApiResponse> getRates(final String base) {
        final boolean useCached
                = cachedRates != null
                && !cacheIsDirty
                && cachedRates.base.equals(base);
        if (useCached) {
            return Single.just(cachedRates);
        }
        return ratesApi
                .getLatest(base)
                .doOnSuccess(ratesApiResponse -> cachedRates = ratesApiResponse);
    }

    public Single<RatesApiResponse> forceRefresh(final String base) {
        invalidateCache();
        return getRates(base);
    }

    public void invalidateCache() {
        cacheIsDirty = true;
    }

}
