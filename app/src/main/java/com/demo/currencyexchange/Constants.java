package com.demo.currencyexchange;

import java.math.BigDecimal;

public class Constants {

    static final ExchangeRate INITIAL_BASE_RATE =
            ExchangeRate
                    .newBuilder()
                    .code("EUR")
                    .value(BigDecimal.ONE)
                    .build();
}
