package com.demo.currencyexchange;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.currencyexchange.rates.RatesFragment;
import com.demo.currencyexchange.util.CurrencyDefinition;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            CurrencyDefinition.initialize(this);

            RatesFragment ratesFragment = RatesFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, ratesFragment, RatesFragment.TAG)
                    .commit();
        }
    }
}
