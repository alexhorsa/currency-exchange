package com.demo.currencyexchange;

import io.reactivex.Single;

public class CurrenciesRepository {

    private ExchangeRatesClient.ExchangeRatesApi ratesApi;

    private ExchangeRates cachedRates;

    private boolean cacheIsDirty = false;

    CurrenciesRepository() {
        ratesApi = ExchangeRatesClient.createService(ExchangeRatesClient.ExchangeRatesApi.class);
    }

    public Single<ExchangeRates> getRates(final String base) {
        final boolean useCached
                = cachedRates != null
                && !cacheIsDirty
                && cachedRates.base.equals(base);
        if (useCached) {
            return Single.just(cachedRates);
        }
        return ratesApi
                .getLatest(base)
                .doOnSuccess(exchangeRates -> cachedRates = exchangeRates);
    }

    public Single<ExchangeRates> forceRefresh(final String base) {
        invalidateCache();
        return getRates(base);
    }

    public void invalidateCache() {
        cacheIsDirty = true;
    }

}
