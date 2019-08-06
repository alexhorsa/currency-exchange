package com.demo.currencyexchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRates {

    public String AUD = "0";
    public String BGN = "0";
    public String BRL = "0";
    public String CAD = "0";
    public String CHF = "0";
    public String CNY = "0";
    public String CZK = "0";
    public String DKK = "0";
    public String GBP = "0";
    public String HKD = "0";
    public String HRK = "0";
    public String HUF = "0";
    public String IDR = "0";
    public String ILS = "0";
    public String INR = "0";
    public String ISK = "0";
    public String JPY = "0";
    public String KRW = "0";
    public String MXN = "0";
    public String MYR = "0";
    public String NOK = "0";
    public String NZD = "0";
    public String PHP = "0";
    public String PLN = "0";
    public String RON = "0";
    public String RUB = "0";
    public String SEK = "0";
    public String SGD = "0";
    public String THB = "0";
    public String TRY = "0";
    public String USD = "0";
    public String ZAR = "0";
    public String EUR = "0";

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
