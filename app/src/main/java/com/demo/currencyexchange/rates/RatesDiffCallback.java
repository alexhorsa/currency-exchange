package com.demo.currencyexchange.rates;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class RatesDiffCallback extends DiffUtil.Callback {

    private List<ExchangeRate> oldList = new ArrayList<>();
    private List<ExchangeRate> newList = new ArrayList<>();

    public void setLists(List<ExchangeRate> oldList, List<ExchangeRate> newList) {
        this.oldList.clear();
        this.newList.clear();
        this.oldList.addAll(oldList);
        this.newList.addAll(newList);
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ExchangeRate oldItem = oldList.get(oldItemPosition);
        ExchangeRate newItem = newList.get(newItemPosition);
        return oldItem.code.equals(newItem.code);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldItemPosition == 0 && newItemPosition == 0) return true;
        ExchangeRate oldItem = oldList.get(oldItemPosition);
        ExchangeRate newItem = newList.get(newItemPosition);
        return oldItem.value.equals(newItem.value);
    }
}
