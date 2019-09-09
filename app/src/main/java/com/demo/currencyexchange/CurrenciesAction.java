package com.demo.currencyexchange;

import androidx.annotation.NonNull;

import com.demo.currencyexchange.mvibase.MviAction;
import com.google.auto.value.AutoValue;

interface CurrenciesAction extends MviAction {

    @AutoValue
    abstract class LoadCurrencies implements CurrenciesAction {

        @NonNull
        abstract String base();

        public static CurrenciesAction load(String base) {
            return new AutoValue_CurrenciesAction_LoadCurrencies(base);
        }
    }

    @AutoValue
    abstract class ComputeExchangeRate implements CurrenciesAction {

        @NonNull
        abstract Currency base();

        @NonNull
        abstract boolean refreshAll();

        public static CurrenciesAction compute(Currency base, boolean refreshAll) {
            return new AutoValue_CurrenciesAction_ComputeExchangeRate(base, refreshAll);
        }

    }
}
