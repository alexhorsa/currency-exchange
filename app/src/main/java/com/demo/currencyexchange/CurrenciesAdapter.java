package com.demo.currencyexchange;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class CurrenciesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    private List<Currency> currencies;

    private PublishSubject<Currency> currencyClickObservable = PublishSubject.create();
    private PublishSubject<Currency> currencyValueChangeObservable = PublishSubject.create();

    CurrenciesAdapter(List<Currency> currencies) {
        this.currencies = currencies;
        setupNumberFormat();
    }

    Observable<Currency> getCurrencyClickObservable() {
        return currencyClickObservable;
    }

    Observable<Currency> getCurrencyValueChangeObservable() {
        return currencyValueChangeObservable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View rowView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Currency currency = currencies.get(position);
        final CurrencyViewHolder currencyVH = (CurrencyViewHolder) holder;

        currencyVH.code.setText(currency.code);

        currencyVH.value.setText(currency.value.toString());

        currencyVH.updateTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String textValue = editable.toString();
                if (textValue.isEmpty()) return;
                currency.value = new BigDecimal(editable.toString());
                currencyValueChangeObservable.onNext(currency);
            }
        });
        currencyVH.itemView.setOnClickListener(ignored -> currencyClickObservable.onNext(currency));
    }

    @Override
    public int getItemCount() {
        if (null == currencies) return 0;
        return currencies.size();
    }

    public void updateData(List<Currency> currencies) {
        if (null == currencies) throw new NullPointerException();
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    private String formatCurrencyValue(BigDecimal value) {
        return numberFormat.format(value);
    }

    private void setupNumberFormat() {
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView code;
        EditText value;

        private TextWatcher textWatcher;

        CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.item_currency_name);
            value = itemView.findViewById(R.id.item_currency_value);
        }

        void updateTextWatcher(TextWatcher newTextWatcher) {
            if (null != textWatcher) {
                value.removeTextChangedListener(textWatcher);
            }
            textWatcher = newTextWatcher;
            value.addTextChangedListener(newTextWatcher);
        }
    }
}
