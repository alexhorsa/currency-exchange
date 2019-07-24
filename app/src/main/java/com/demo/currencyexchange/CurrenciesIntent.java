package com.demo.currencyexchange;

import com.demo.currencyexchange.mvibase.MviIntent;
import com.google.auto.value.AutoValue;

interface CurrenciesIntent extends MviIntent {

    @AutoValue
    abstract class InitialIntent implements CurrenciesIntent {

        public static InitialIntent create() {
            return new AutoValue_CurrenciesIntent_InitialIntent();
        }
    }

    @AutoValue
    abstract class RefreshIntent implements CurrenciesIntent {

        public static RefreshIntent create() {
            return new AutoValue_CurrenciesIntent_RefreshIntent();
        }
    }
}
