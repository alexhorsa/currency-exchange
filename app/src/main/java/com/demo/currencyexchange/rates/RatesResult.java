package com.demo.currencyexchange.rates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.currencyexchange.mvibase.MviResult;
import com.demo.currencyexchange.util.LceStatus;
import com.google.auto.value.AutoValue;

import java.util.List;

import static com.demo.currencyexchange.util.LceStatus.FAILURE;
import static com.demo.currencyexchange.util.LceStatus.IN_FLIGHT;
import static com.demo.currencyexchange.util.LceStatus.SUCCESS;

interface RatesResult extends MviResult {

    @AutoValue
    abstract class LoadRates implements RatesResult {

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract String base();

        @Nullable
        abstract List<ExchangeRate> currencies();

        @NonNull
        abstract boolean refreshAll();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadRates success(@NonNull String base, @NonNull List<ExchangeRate> exchangeRateList) {
            return new AutoValue_RatesResult_LoadRates(SUCCESS, base, exchangeRateList, false, null);
        }

        @NonNull
        static LoadRates success(@NonNull String base, @NonNull List<ExchangeRate> exchangeRateList, boolean refreshAll) {
            return new AutoValue_RatesResult_LoadRates(SUCCESS, base, exchangeRateList, refreshAll, null);
        }

        @NonNull
        static LoadRates failure(Throwable error) {
            return new AutoValue_RatesResult_LoadRates(FAILURE, null,null, false, error);
        }

        @NonNull
        static LoadRates inFlight() {
            return new AutoValue_RatesResult_LoadRates(IN_FLIGHT,null, null, false, null);
        }
    }

    @AutoValue
    abstract class ComputeExchangeRate implements RatesResult {

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<ExchangeRate> currencies();

        @Nullable
        abstract Throwable error();

        @NonNull
        static ComputeExchangeRate success(@NonNull List<ExchangeRate> exchangeRateList) {
            return new AutoValue_RatesResult_ComputeExchangeRate(SUCCESS, exchangeRateList, null);
        }

        @NonNull
        static ComputeExchangeRate failure(Throwable error) {
            return new AutoValue_RatesResult_ComputeExchangeRate(FAILURE, null, error);
        }

        @NonNull
        static ComputeExchangeRate inFlight() {
            return new AutoValue_RatesResult_ComputeExchangeRate(IN_FLIGHT, null, null);
        }
    }
}
