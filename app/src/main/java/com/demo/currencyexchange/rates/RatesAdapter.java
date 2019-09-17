package com.demo.currencyexchange.rates;

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

import com.demo.currencyexchange.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private List<ExchangeRate> currencies;

    private PublishSubject<ExchangeRate> currencyClickObservable = PublishSubject.create();
    private PublishSubject<ExchangeRate> currencyValueChangeObservable = PublishSubject.create();

    private ValueChangeWatcher currencyWatcher;

    RatesAdapter(List<ExchangeRate> currencies) {
        this.currencies = currencies;
        this.currencyWatcher = new ValueChangeWatcher(currencyValueChangeObservable);
    }

    Observable<ExchangeRate> getCurrencyClickObservable() {
        return currencyClickObservable;
    }

    Observable<ExchangeRate> getCurrencyValueChangeObservable() {
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
        final ExchangeRate exchangeRate = currencies.get(position);
        final CurrencyViewHolder currencyVH = (CurrencyViewHolder) holder;

        currencyVH.code.setText(exchangeRate.code);

        String amountText;
        if (position > 0) {
            amountText = decimalFormat.format(exchangeRate.value);
            currencyVH.value.removeTextChangedListener(currencyWatcher);
        } else {
            amountText
                    = exchangeRate.value.scale() >= 2
                    ? decimalFormat.format(exchangeRate.value)
                    : exchangeRate.value.toString();
        }

        currencyVH.value.clearFocus();
        currencyVH.value.setText(amountText);

        if (position == 0) {
            currencyWatcher.setExchangeRate(exchangeRate);
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
//                Log.d("Adapter", "afterTextChanged - exchangeRate " + exchangeRate.code);
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
//                exchangeRate.value = new BigDecimal(result);
//                currencyValueChangeObservable.onNext(exchangeRate);
//            }
//        });
        currencyVH.value.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currencyVH.value.post(() -> currencyVH.value.setSelection(currencyVH.value.length()));

                if (position > 0) {
                    currencyClickObservable.onNext(exchangeRate);
                }
            }
        });

        currencyVH.itemView.setOnClickListener(ignored -> currencyClickObservable.onNext(exchangeRate));
    }

    @Override
    public int getItemCount() {
        if (null == currencies) return 0;
        return currencies.size();
    }

    public void updateData(List<ExchangeRate> currencies, boolean refreshAll) {
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

        private PublishSubject<ExchangeRate> valueChangeStream;
        private ExchangeRate exchangeRate;
        private String textBefore = "";
        private String textChanged;

        ValueChangeWatcher(PublishSubject<ExchangeRate> valueChangeStream) {
            this.valueChangeStream = valueChangeStream;
        }

        void setTextView(TextView textView) {
            target = textView;
        }

        void setExchangeRate(ExchangeRate exchangeRate) {
            this.exchangeRate = exchangeRate;
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

            Log.d("Adapter", "afterTextChanged - exchangeRate " + exchangeRate.code);

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

            exchangeRate.value = new BigDecimal(result);
            valueChangeStream.onNext(exchangeRate);
        }
    }
}
