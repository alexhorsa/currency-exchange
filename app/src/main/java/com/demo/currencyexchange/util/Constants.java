package com.demo.currencyexchange.util;

import com.demo.currencyexchange.rates.ExchangeRate;

import java.math.BigDecimal;

public class Constants {

    public static final ExchangeRate INITIAL_BASE_RATE =
            ExchangeRate
                    .newBuilder()
                    .code("EUR")
                    .value(BigDecimal.ONE)
                    .build();
}
