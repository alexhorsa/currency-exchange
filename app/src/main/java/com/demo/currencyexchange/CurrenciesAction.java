package com.demo.currencyexchange;

import com.demo.currencyexchange.mvibase.MviAction;
import com.google.auto.value.AutoValue;

interface CurrenciesAction extends MviAction {

    @AutoValue
    abstract class LoadCurrencies implements CurrenciesAction {

        public static CurrenciesAction load() {
            return new AutoValue_CurrenciesAction_LoadCurrencies();
        }
    }

    @AutoValue
    abstract class ComputeExchangeRate implements CurrenciesAction {

        public static CurrenciesAction compute() {
            return new AutoValue_CurrenciesAction_ComputeExchangeRate();
        }

    }
}
