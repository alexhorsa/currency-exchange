package com.demo.currencyexchange;

import java.math.BigDecimal;

public class Currency {

    public String code;
    public BigDecimal value;

    private Currency(Builder builder) {
        code = builder.code;
        value = builder.value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Currency copy) {
        Builder builder = new Builder();
        builder.code = copy.code;
        builder.value = copy.value;
        return builder;
    }

    public static final class Builder {
        private String code;
        private BigDecimal value;

        private Builder() {
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder value(BigDecimal val) {
            value = val;
            return this;
        }

        public Currency build() {
            return new Currency(this);
        }
    }
}
