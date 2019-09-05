package com.demo.currencyexchange;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class CurrenciesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private List<Currency> currencies;

    private PublishSubject<Currency> currencyClickObservable = PublishSubject.create();
    private PublishSubject<Currency> currencyValueChangeObservable = PublishSubject.create();

    CurrenciesAdapter(List<Currency> currencies) {
        this.currencies = currencies;
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

        String amountText;
        if (position > 0) {
            amountText = decimalFormat.format(currency.value);
        } else {
            amountText
                    = currency.value.scale() >= 2
                    ? decimalFormat.format(currency.value)
                    : currency.value.toString();
        }

        currencyVH.value.clearFocus();
        currencyVH.value.setText(amountText);

        currencyVH.updateTextWatcher(new TextWatcher() {

            private String textBefore = "";
            private String textChanged;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!currencyVH.value.hasFocus()) return;
                textBefore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!currencyVH.value.hasFocus()) return;
                Log.d("Adapter", "onTextChanged");

                CharSequence strChange = charSequence.subSequence(start, start + count);
                textChanged = strChange.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!currencyVH.value.hasFocus()) return;

                final String textValue = editable.toString();
                if (textValue.isEmpty()) return;
                if (".".equals(textChanged)) return;

                Log.d("Adapter", "afterTextChanged - currency " + currency.code);

                String result;
                if (textBefore.length() > editable.length()) { // Backspace detected
                    result = textBefore.substring(0, textBefore.length() - 1);
                } else {
                    result = textBefore + textChanged;
                }

//                EditText editText = currencyVH.value;
//                editText.removeTextChangedListener(this);
//                editText.setText(result);
//                editText.addTextChangedListener(this);

                currency.value = new BigDecimal(result);
                currencyValueChangeObservable.onNext(currency);
            }
        });
        currencyVH.value.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currencyVH.value.post(() -> currencyVH.value.setSelection(currencyVH.value.length()));

                if (position > 0) {
                    currencyClickObservable.onNext(currency);
                }
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
