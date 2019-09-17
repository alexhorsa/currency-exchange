package com.demo.currencyexchange;

import androidx.annotation.NonNull;

import com.demo.currencyexchange.mvibase.MviAction;
import com.google.auto.value.AutoValue;

interface RatesAction extends MviAction {

    @AutoValue
    abstract class LoadRates implements RatesAction {

        @NonNull
        abstract String base();

        public static RatesAction load(String base) {
            return new AutoValue_RatesAction_LoadRates(base);
        }
    }

    @AutoValue
    abstract class ComputeExchangeRate implements RatesAction {

        @NonNull
        abstract ExchangeRate base();

        @NonNull
        abstract boolean refreshAll();

        public static RatesAction compute(ExchangeRate base, boolean refreshAll) {
            return new AutoValue_RatesAction_ComputeExchangeRate(base, refreshAll);
        }

    }

    @AutoValue
    abstract class AutoRefreshRates implements RatesAction {

        @NonNull
        abstract ExchangeRate base();

        public static RatesAction create(ExchangeRate base) {
            return new AutoValue_RatesAction_AutoRefreshRates(base);
        }

    }
}
