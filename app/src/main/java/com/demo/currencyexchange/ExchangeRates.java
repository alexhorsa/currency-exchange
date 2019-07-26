package com.demo.currencyexchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRates {

    public String AUD;
    public String BGN;
    public String BRL;
    public String CAD;
    public String CHF;
    public String CNY;
    public String CZK;
    public String DKK;
    public String GBP;
    public String HKD;
    public String HRK;
    public String HUF;
    public String IDR;
    public String ILS;
    public String INR;
    public String ISK;
    public String JPY;
    public String KRW;
    public String MXN;
    public String MYR;
    public String NOK;
    public String NZD;
    public String PHP;
    public String PLN;
    public String RON;
    public String RUB;
    public String SEK;
    public String SGD;
    public String THB;
    public String TRY;
    public String USD;
    public String ZAR;
    public String EUR;

    public List<Currency> toCurrenciesList() {
        final List<Currency> result = new ArrayList<>();
        result.add(Currency.newBuilder().code("AUD").value(new BigDecimal(AUD)).build());
        result.add(Currency.newBuilder().code("BGN").value(new BigDecimal(BGN)).build());
        result.add(Currency.newBuilder().code("BRL").value(new BigDecimal(BRL)).build());
        result.add(Currency.newBuilder().code("CAD").value(new BigDecimal(CAD)).build());
        result.add(Currency.newBuilder().code("CHF").value(new BigDecimal(CHF)).build());
        result.add(Currency.newBuilder().code("CNY").value(new BigDecimal(CNY)).build());
        result.add(Currency.newBuilder().code("CZK").value(new BigDecimal(CZK)).build());
        result.add(Currency.newBuilder().code("DKK").value(new BigDecimal(DKK)).build());
        result.add(Currency.newBuilder().code("GBP").value(new BigDecimal(GBP)).build());
        result.add(Currency.newBuilder().code("HKD").value(new BigDecimal(HKD)).build());
        result.add(Currency.newBuilder().code("HRK").value(new BigDecimal(HRK)).build());
        result.add(Currency.newBuilder().code("HUF").value(new BigDecimal(HUF)).build());
        result.add(Currency.newBuilder().code("IDR").value(new BigDecimal(IDR)).build());
        result.add(Currency.newBuilder().code("ILS").value(new BigDecimal(ILS)).build());
        result.add(Currency.newBuilder().code("INR").value(new BigDecimal(INR)).build());
        result.add(Currency.newBuilder().code("ISK").value(new BigDecimal(ISK)).build());
        result.add(Currency.newBuilder().code("JPY").value(new BigDecimal(JPY)).build());
        result.add(Currency.newBuilder().code("KRW").value(new BigDecimal(KRW)).build());
        result.add(Currency.newBuilder().code("MXN").value(new BigDecimal(MXN)).build());
        result.add(Currency.newBuilder().code("MYR").value(new BigDecimal(MYR)).build());
        result.add(Currency.newBuilder().code("NOK").value(new BigDecimal(NOK)).build());
        result.add(Currency.newBuilder().code("NZD").value(new BigDecimal(NZD)).build());
        result.add(Currency.newBuilder().code("PHP").value(new BigDecimal(PHP)).build());
        result.add(Currency.newBuilder().code("PLN").value(new BigDecimal(PLN)).build());
        result.add(Currency.newBuilder().code("RON").value(new BigDecimal(RON)).build());
        result.add(Currency.newBuilder().code("RUB").value(new BigDecimal(RUB)).build());
        result.add(Currency.newBuilder().code("SEK").value(new BigDecimal(SEK)).build());
        result.add(Currency.newBuilder().code("SGD").value(new BigDecimal(SGD)).build());
        result.add(Currency.newBuilder().code("THB").value(new BigDecimal(THB)).build());
        result.add(Currency.newBuilder().code("TRY").value(new BigDecimal(TRY)).build());
        result.add(Currency.newBuilder().code("USD").value(new BigDecimal(USD)).build());
        result.add(Currency.newBuilder().code("ZAR").value(new BigDecimal(ZAR)).build());
        result.add(Currency.newBuilder().code("EUR").value(new BigDecimal(EUR)).build());
        return result;
    }
}
