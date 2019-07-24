package com.demo.currencyexchange;

import io.reactivex.Single;

public class CurrenciesRepository {

    private ExchangeRatesClient.ExchangeRatesApi ratesApi;

    CurrenciesRepository() {
        ratesApi = ExchangeRatesClient.createService(ExchangeRatesClient.ExchangeRatesApi.class);
    }

    public Single<ExchangeRatesResponse> getRates(final String base) {
        return ratesApi.getLatest(base);
    }
}
