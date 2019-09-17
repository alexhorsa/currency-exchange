package com.demo.currencyexchange;

import androidx.annotation.Nullable;

import com.demo.currencyexchange.mvibase.MviViewState;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

@AutoValue
abstract class RatesViewState implements MviViewState {

    public abstract boolean isLoading();

    public abstract List<ExchangeRate> currencies();

    public abstract boolean refreshAll();

    @Nullable
    abstract Throwable error();

    static RatesViewState idle() {
        return builder()
                .isLoading(false)
                .currencies(Collections.emptyList())
                .refreshAll(false)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_RatesViewState.Builder();
    }

    public abstract Builder buildWith();

    @AutoValue.Builder
    abstract static class Builder {

        public abstract Builder isLoading(boolean isLoading);

        public abstract Builder currencies(List<ExchangeRate> currencies);

        public abstract Builder error(Throwable error);

        public abstract Builder refreshAll(boolean refreshAll);

        abstract RatesViewState build();
    }
}
