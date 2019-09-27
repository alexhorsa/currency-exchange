package com.demo.currencyexchange.rates;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.currencyexchange.R;
import com.demo.currencyexchange.util.CurrencyDefinition;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private List<ExchangeRate> exchangeRates;

    private PublishSubject<ExchangeRate> rateClickObservable = PublishSubject.create();
    private PublishSubject<ExchangeRate> rateValueChangeObservable = PublishSubject.create();

    private RatesDiffCallback ratesDiffCallback = new RatesDiffCallback();
    private ValueChangeWatcher exchangeRateWatcher;

    RatesAdapter(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.exchangeRateWatcher = new ValueChangeWatcher(rateValueChangeObservable);
    }

    Observable<ExchangeRate> getRateClickObservable() {
        return rateClickObservable;
    }

    Observable<ExchangeRate> getRateValueChangeObservable() {
        return rateValueChangeObservable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View rowView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_exchange_rate, parent, false);
        return new ExchangeRateViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ExchangeRate exchangeRate = exchangeRates.get(position);
        final CurrencyDefinition currencyDefinition = CurrencyDefinition.findWithCode(exchangeRate.code);
        final ExchangeRateViewHolder rateVH = (ExchangeRateViewHolder) holder;

        rateVH.flag.setImageResource(currencyDefinition.flagResId);
        rateVH.code.setText(currencyDefinition.code);
        rateVH.name.setText(currencyDefinition.name);

        String amountText;
        if (position > 0) {
            amountText = decimalFormat.format(exchangeRate.value);
            rateVH.value.removeTextChangedListener(exchangeRateWatcher);
        } else {
            amountText
                    = exchangeRate.value.scale() >= 2
                    ? decimalFormat.format(exchangeRate.value)
                    : exchangeRate.value.toString();
        }

        rateVH.value.clearFocus();
        rateVH.value.setText(amountText);

        if (position == 0) {
            exchangeRateWatcher.setExchangeRate(exchangeRate);
            exchangeRateWatcher.setTextView(rateVH.value);
            rateVH.updateTextWatcher(exchangeRateWatcher);
        }

        rateVH.value.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rateVH.value.post(() -> rateVH.value.setSelection(rateVH.value.length()));

                if (position > 0) {
                    rateClickObservable.onNext(exchangeRate);
                }
            }
        });

        rateVH.itemView.setOnClickListener(ignored -> rateClickObservable.onNext(exchangeRate));
    }

    @Override
    public int getItemCount() {
        if (null == exchangeRates) return 0;
        return exchangeRates.size();
    }

    public void updateData(List<ExchangeRate> rates, boolean refreshAll) {
        if (null == rates) throw new NullPointerException();

//        ratesDiffCallback.setLists(this.exchangeRates, rates);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(ratesDiffCallback);

        this.exchangeRates.clear();
        this.exchangeRates.addAll(rates);

        if (refreshAll) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(1, rates.size() - 1);
        }

//        diffResult.dispatchUpdatesTo(this);
    }

    public static class ExchangeRateViewHolder extends RecyclerView.ViewHolder {

        ImageView flag;
        TextView code;
        TextView name;
        EditText value;

        private TextWatcher textWatcher;

        ExchangeRateViewHolder(@NonNull View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.item_currency_flag);
            code = itemView.findViewById(R.id.item_currency_code);
            name = itemView.findViewById(R.id.item_currency_name);
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

            exchangeRate.value = new BigDecimal(result);
            valueChangeStream.onNext(exchangeRate);
        }
    }
}
