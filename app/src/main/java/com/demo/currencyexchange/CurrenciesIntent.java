package com.demo.currencyexchange;

import androidx.annotation.NonNull;

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

        @NonNull
        abstract Currency base();

        public static RefreshIntent create(Currency base) {
            return new AutoValue_CurrenciesIntent_RefreshIntent(base);
        }
    }
}
