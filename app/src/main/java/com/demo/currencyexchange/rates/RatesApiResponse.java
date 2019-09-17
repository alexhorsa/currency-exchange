package com.demo.currencyexchange.rates;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatesApiResponse {

    public String base;
    public Map<String, String> rates;

    public List<ExchangeRate> toCurrenciesList(@NonNull BigDecimal baseValue) {
        final List<ExchangeRate> result = new ArrayList<>();
        result.add(
                ExchangeRate
                        .newBuilder()
                        .code(base)
                        .value(baseValue)
                        .build()
        );

        for (Map.Entry<String, String> entrySet : rates.entrySet()) {
            final String currencyCode = entrySet.getKey();
            final String currencyValue = entrySet.getValue();
            final BigDecimal value = new BigDecimal(currencyValue).multiply(baseValue);
            result.add(
                    ExchangeRate
                            .newBuilder()
                            .code(currencyCode)
                            .value(value)
                            .build()
            );
        }
        return result;
    }
}
