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

        @NonNull
        abstract boolean refreshAll();

        public static RefreshIntent create(Currency base, boolean refreshAll) {
            return new AutoValue_CurrenciesIntent_RefreshIntent(base, refreshAll);
        }
    }
}
