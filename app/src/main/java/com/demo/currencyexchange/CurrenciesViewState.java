package com.demo.currencyexchange;

import androidx.annotation.Nullable;

import com.demo.currencyexchange.mvibase.MviViewState;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

@AutoValue
abstract class CurrenciesViewState implements MviViewState {

    public abstract boolean isLoading();

    public abstract List<Currency> currencies();

    public abstract boolean refreshAll();

    @Nullable
    abstract Throwable error();

    static CurrenciesViewState idle() {
        return builder()
                .isLoading(false)
                .currencies(Collections.emptyList())
                .refreshAll(false)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_CurrenciesViewState.Builder();
    }

    public abstract Builder buildWith();

    @AutoValue.Builder
    abstract static class Builder {

        public abstract Builder isLoading(boolean isLoading);

        public abstract Builder currencies(List<Currency> currencies);

        public abstract Builder error(Throwable error);

        public abstract Builder refreshAll(boolean refreshAll);

        abstract CurrenciesViewState build();
    }
}
