package com.demo.currencyexchange;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.currencyexchange.rates.RatesFragment;
import com.demo.currencyexchange.util.CurrencyDefinition;

public class MainActivity extends AppCompatActivity {

    public static final Point SCREEN_SIZE = new Point();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            display.getSize(SCREEN_SIZE);
        }

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
