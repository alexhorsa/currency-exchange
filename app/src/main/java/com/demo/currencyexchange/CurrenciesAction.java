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

        public static CurrenciesAction compute() {
            return new AutoValue_CurrenciesAction_ComputeExchangeRate();
        }

    }
}
