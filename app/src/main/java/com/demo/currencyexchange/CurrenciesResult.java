package com.demo.currencyexchange;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.currencyexchange.mvibase.MviResult;
import com.demo.currencyexchange.util.LceStatus;
import com.google.auto.value.AutoValue;

import java.util.List;

import static com.demo.currencyexchange.util.LceStatus.FAILURE;
import static com.demo.currencyexchange.util.LceStatus.IN_FLIGHT;
import static com.demo.currencyexchange.util.LceStatus.SUCCESS;

interface CurrenciesResult extends MviResult {

    @AutoValue
    abstract class LoadCurrencies implements CurrenciesResult {

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract String base();

        @Nullable
        abstract List<Currency> currencies();

        @NonNull
        abstract boolean refreshAll();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadCurrencies success(@NonNull String base, @NonNull List<Currency> currencyList) {
            return new AutoValue_CurrenciesResult_LoadCurrencies(SUCCESS, base, currencyList, false, null);
        }

        @NonNull
        static LoadCurrencies success(@NonNull String base, @NonNull List<Currency> currencyList, boolean refreshAll) {
            return new AutoValue_CurrenciesResult_LoadCurrencies(SUCCESS, base, currencyList, refreshAll, null);
        }

        @NonNull
        static LoadCurrencies failure(Throwable error) {
            return new AutoValue_CurrenciesResult_LoadCurrencies(FAILURE, null,null, false, error);
        }

        @NonNull
        static LoadCurrencies inFlight() {
            return new AutoValue_CurrenciesResult_LoadCurrencies(IN_FLIGHT,null, null, false, null);
        }
    }

    @AutoValue
    abstract class ComputeExchangeRate implements CurrenciesResult {

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<Currency> currencies();

        @Nullable
        abstract Throwable error();

        @NonNull
        static ComputeExchangeRate success(@NonNull List<Currency> currencyList) {
            return new AutoValue_CurrenciesResult_ComputeExchangeRate(SUCCESS, currencyList, null);
        }

        @NonNull
        static ComputeExchangeRate failure(Throwable error) {
            return new AutoValue_CurrenciesResult_ComputeExchangeRate(FAILURE, null, error);
        }

        @NonNull
        static ComputeExchangeRate inFlight() {
            return new AutoValue_CurrenciesResult_ComputeExchangeRate(IN_FLIGHT, null, null);
        }
    }
}
