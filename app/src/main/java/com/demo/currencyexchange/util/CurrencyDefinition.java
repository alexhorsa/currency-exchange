package com.demo.currencyexchange.util;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.demo.currencyexchange.R;

import java.util.HashMap;

public class CurrencyDefinition {

    private static final CurrencyDefinition EMPTY =
            newBuilder()
            .code(R.string.currency_code_unknown)
            .name(R.string.currency_name_unknown)
            .flagResId(R.drawable.eu)
            .build();

    private static final CurrencyDefinition AUD =
            newBuilder()
            .code(R.string.currency_code_aud)
            .name(R.string.currency_name_aud)
            .flagResId(R.drawable.au)
            .build();

    private static final CurrencyDefinition BGN =
            newBuilder()
                    .code(R.string.currency_code_bgn)
                    .name(R.string.currency_name_bgn)
                    .flagResId(R.drawable.bg)
                    .build();

    private static final CurrencyDefinition BRL =
            newBuilder()
                    .code(R.string.currency_code_brl)
                    .name(R.string.currency_name_brl)
                    .flagResId(R.drawable.br)
                    .build();

    private static final CurrencyDefinition CAD =
            newBuilder()
                    .code(R.string.currency_code_cad)
                    .name(R.string.currency_name_cad)
                    .flagResId(R.drawable.ca)
                    .build();

    private static final CurrencyDefinition CHF =
            newBuilder()
                    .code(R.string.currency_code_chf)
                    .name(R.string.currency_name_chf)
                    .flagResId(R.drawable.ch)
                    .build();

    private static final CurrencyDefinition CNY =
            newBuilder()
                    .code(R.string.currency_code_cny)
                    .name(R.string.currency_name_cny)
                    .flagResId(R.drawable.cn)
                    .build();

    private static final CurrencyDefinition CZK =
            newBuilder()
                    .code(R.string.currency_code_czk)
                    .name(R.string.currency_name_czk)
                    .flagResId(R.drawable.cz)
                    .build();

    private static final CurrencyDefinition DKK =
            newBuilder()
                    .code(R.string.currency_code_dkk)
                    .name(R.string.currency_name_dkk)
                    .flagResId(R.drawable.dk)
                    .build();

    private static final CurrencyDefinition GBP =
            newBuilder()
                    .code(R.string.currency_code_gbp)
                    .name(R.string.currency_name_gbp)
                    .flagResId(R.drawable.gb)
                    .build();

    private static final CurrencyDefinition HKD =
            newBuilder()
                    .code(R.string.currency_code_hkd)
                    .name(R.string.currency_name_hkd)
                    .flagResId(R.drawable.hk)
                    .build();

    private static final CurrencyDefinition HRK =
            newBuilder()
                    .code(R.string.currency_code_hrk)
                    .name(R.string.currency_name_hrk)
                    .flagResId(R.drawable.hr)
                    .build();

    private static final CurrencyDefinition HUF =
            newBuilder()
                    .code(R.string.currency_code_huf)
                    .name(R.string.currency_name_huf)
                    .flagResId(R.drawable.hu)
                    .build();

    private static final CurrencyDefinition IDR =
            newBuilder()
                    .code(R.string.currency_code_idr)
                    .name(R.string.currency_name_idr)
                    .flagResId(R.drawable.id)
                    .build();

    private static final CurrencyDefinition ILS =
            newBuilder()
                    .code(R.string.currency_code_ils)
                    .name(R.string.currency_name_ils)
                    .flagResId(R.drawable.il)
                    .build();

    private static final CurrencyDefinition INR =
            newBuilder()
                    .code(R.string.currency_code_inr)
                    .name(R.string.currency_name_inr)
                    .flagResId(R.drawable.in)
                    .build();

    private static final CurrencyDefinition ISK =
            newBuilder()
                    .code(R.string.currency_code_isk)
                    .name(R.string.currency_name_isk)
                    .flagResId(R.drawable.is)
                    .build();

    private static final CurrencyDefinition JPY =
            newBuilder()
                    .code(R.string.currency_code_jpy)
                    .name(R.string.currency_name_jpy)
                    .flagResId(R.drawable.jp)
                    .build();

    private static final CurrencyDefinition KRW =
            newBuilder()
                    .code(R.string.currency_code_krw)
                    .name(R.string.currency_name_krw)
                    .flagResId(R.drawable.kr)
                    .build();

    private static final CurrencyDefinition MXN =
            newBuilder()
                    .code(R.string.currency_code_mxn)
                    .name(R.string.currency_name_mxn)
                    .flagResId(R.drawable.mx)
                    .build();

    private static final CurrencyDefinition MYR =
            newBuilder()
                    .code(R.string.currency_code_myr)
                    .name(R.string.currency_name_myr)
                    .flagResId(R.drawable.my)
                    .build();

    private static final CurrencyDefinition NOK =
            newBuilder()
                    .code(R.string.currency_code_nok)
                    .name(R.string.currency_name_nok)
                    .flagResId(R.drawable.no)
                    .build();

    private static final CurrencyDefinition NZD =
            newBuilder()
                    .code(R.string.currency_code_nzd)
                    .name(R.string.currency_name_nzd)
                    .flagResId(R.drawable.nz)
                    .build();

    private static final CurrencyDefinition PHP =
            newBuilder()
                    .code(R.string.currency_code_php)
                    .name(R.string.currency_name_php)
                    .flagResId(R.drawable.ph)
                    .build();

    private static final CurrencyDefinition PLN =
            newBuilder()
                    .code(R.string.currency_code_pln)
                    .name(R.string.currency_name_pln)
                    .flagResId(R.drawable.pl)
                    .build();

    private static final CurrencyDefinition RON =
            newBuilder()
                    .code(R.string.currency_code_ron)
                    .name(R.string.currency_name_ron)
                    .flagResId(R.drawable.ro)
                    .build();

    private static final CurrencyDefinition RUB =
            newBuilder()
                    .code(R.string.currency_code_rub)
                    .name(R.string.currency_name_rub)
                    .flagResId(R.drawable.ru)
                    .build();

    private static final CurrencyDefinition SEK =
            newBuilder()
                    .code(R.string.currency_code_sek)
                    .name(R.string.currency_name_sek)
                    .flagResId(R.drawable.se)
                    .build();

    private static final CurrencyDefinition SGD =
            newBuilder()
                    .code(R.string.currency_code_sgd)
                    .name(R.string.currency_name_sgd)
                    .flagResId(R.drawable.sg)
                    .build();

    private static final CurrencyDefinition THB =
            newBuilder()
                    .code(R.string.currency_code_thb)
                    .name(R.string.currency_name_thb)
                    .flagResId(R.drawable.th)
                    .build();

    private static final CurrencyDefinition TRY =
            newBuilder()
                    .code(R.string.currency_code_try)
                    .name(R.string.currency_name_try)
                    .flagResId(R.drawable.tr)
                    .build();

    private static final CurrencyDefinition USD =
            newBuilder()
                    .code(R.string.currency_code_usd)
                    .name(R.string.currency_name_usd)
                    .flagResId(R.drawable.us)
                    .build();

    private static final CurrencyDefinition ZAR =
            newBuilder()
                    .code(R.string.currency_code_zar)
                    .name(R.string.currency_name_zar)
                    .flagResId(R.drawable.za)
                    .build();

    private static final CurrencyDefinition EUR =
            newBuilder()
                    .code(R.string.currency_code_eur)
                    .name(R.string.currency_name_eur)
                    .flagResId(R.drawable.eu)
                    .build();

    private static final CurrencyDefinition[] ALL = new CurrencyDefinition[] {
            AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK, GBP, HKD, HRK, HUF, IDR, ILS, INR, ISK, JPY,
            KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON, RUB, SEK, SGD, THB, TRY, USD, ZAR, EUR
    };

    private static final HashMap<String, CurrencyDefinition> CODE_DEFINITION_MAP = new HashMap<>();

    public static void initialize(Context context) {
        CODE_DEFINITION_MAP.clear();
        for (CurrencyDefinition definition : ALL) {
            CODE_DEFINITION_MAP.put(context.getString(definition.code), definition);
        }
    }

    public static CurrencyDefinition findWithCode(String code) {
        CurrencyDefinition result = CODE_DEFINITION_MAP.get(code);
        if (null == result) return CurrencyDefinition.EMPTY;
        return result;
    }

    public boolean isEmpty() {
        return EMPTY.equals(this);
    }

    @StringRes
    public int code;
    @StringRes
    public int name;
    @DrawableRes
    public int flagResId;

    private CurrencyDefinition(Builder builder) {
        code = builder.code;
        name = builder.name;
        flagResId = builder.flagResId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(CurrencyDefinition copy) {
        Builder builder = new Builder();
        builder.code = copy.code;
        builder.name = copy.name;
        builder.flagResId = copy.flagResId;
        return builder;
    }

    public static final class Builder {
        private int code;
        private int name;
        private int flagResId;

        private Builder() {
        }

        public Builder code(@StringRes int val) {
            code = val;
            return this;
        }

        public Builder name(@StringRes int val) {
            name = val;
            return this;
        }

        public Builder flagResId(@DrawableRes int val) {
            flagResId = val;
            return this;
        }

        public CurrencyDefinition build() {
            return new CurrencyDefinition(this);
        }
    }

}
