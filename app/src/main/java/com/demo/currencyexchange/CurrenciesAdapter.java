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

    private ValueChangeWatcher currencyWatcher;

    CurrenciesAdapter(List<Currency> currencies) {
        this.currencies = currencies;
        this.currencyWatcher = new ValueChangeWatcher(currencyValueChangeObservable);
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
            currencyVH.value.removeTextChangedListener(currencyWatcher);
        } else {
            amountText
                    = currency.value.scale() >= 2
                    ? decimalFormat.format(currency.value)
                    : currency.value.toString();
        }

        currencyVH.value.clearFocus();
        currencyVH.value.setText(amountText);

        if (position == 0) {
            currencyWatcher.setCurrency(currency);
            currencyWatcher.setTextView(currencyVH.value);
            currencyVH.updateTextWatcher(currencyWatcher);
        }

//        currencyVH.updateTextWatcher(new TextWatcher() {
//
//            private String textBefore = "";
//            private String textChanged;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (!currencyVH.value.hasFocus()) return;
//                textBefore = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (!currencyVH.value.hasFocus()) return;
//                Log.d("Adapter", "onTextChanged");
//
//                CharSequence strChange = charSequence.subSequence(start, start + count);
//                textChanged = strChange.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!currencyVH.value.hasFocus()) return;
//
//                final String textValue = editable.toString();
//                if (textValue.isEmpty()) return;
//                if (".".equals(textChanged)) return;
//
//                Log.d("Adapter", "afterTextChanged - currency " + currency.code);
//
//                String result;
//                if (textBefore.length() > editable.length()) { // Backspace detected
//                    result = textBefore.substring(0, textBefore.length() - 1);
//                } else {
//                    result = textBefore + textChanged;
//                }
//
////                EditText editText = currencyVH.value;
////                editText.removeTextChangedListener(this);
////                editText.setText(result);
////                editText.addTextChangedListener(this);
//
//                currency.value = new BigDecimal(result);
//                currencyValueChangeObservable.onNext(currency);
//            }
//        });
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

    public void updateData(List<Currency> currencies, boolean refreshAll) {
        if (null == currencies) throw new NullPointerException();
        this.currencies = currencies;
        if (refreshAll) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(1, currencies.size() - 1);
        }
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

    public static class ValueChangeWatcher implements TextWatcher {

        private TextView target;

        private PublishSubject<Currency> valueChangeStream;
        private Currency currency;
        private String textBefore = "";
        private String textChanged;

        ValueChangeWatcher(PublishSubject<Currency> valueChangeStream) {
            this.valueChangeStream = valueChangeStream;
        }

        void setTextView(TextView textView) {
            target = textView;
        }

        void setCurrency(Currency currency) {
            this.currency = currency;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!target.hasFocus()) return;
            textBefore = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (!target.hasFocus()) return;
            Log.d("Adapter", "onTextChanged");

            CharSequence strChange = charSequence.subSequence(start, start + count);
            textChanged = strChange.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!target.hasFocus()) return;

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
            valueChangeStream.onNext(currency);
        }
    }
}
