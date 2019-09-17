package com.demo.currencyexchange;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.currencyexchange.mvibase.MviIntent;
import com.google.auto.value.AutoValue;

interface RatesIntent extends MviIntent {

    @AutoValue
    abstract class InitialIntent implements RatesIntent {

        public static InitialIntent create() {
            return new AutoValue_RatesIntent_InitialIntent();
        }
    }

    @AutoValue
    abstract class RefreshIntent implements RatesIntent {

        @NonNull
        abstract ExchangeRate base();

        @NonNull
        abstract boolean refreshAll();

        public static RefreshIntent create(ExchangeRate base, boolean refreshAll) {
            return new AutoValue_RatesIntent_RefreshIntent(base, refreshAll);
        }
    }

    @AutoValue
    abstract class AutoRefreshIntent implements RatesIntent {

        @Nullable
        abstract ExchangeRate base();

        public static AutoRefreshIntent create(ExchangeRate base) {
            return new AutoValue_RatesIntent_AutoRefreshIntent(base);
        }

        public static AutoRefreshIntent empty() {
            return new AutoValue_RatesIntent_AutoRefreshIntent(null);
        }

    }
}
