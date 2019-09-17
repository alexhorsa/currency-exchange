package com.demo.currencyexchange;

import java.math.BigDecimal;

public class Constants {

    static final Currency INITIAL_BASE_RATE =
            Currency
                    .newBuilder()
                    .code("EUR")
                    .value(BigDecimal.ONE)
                    .build();
}
