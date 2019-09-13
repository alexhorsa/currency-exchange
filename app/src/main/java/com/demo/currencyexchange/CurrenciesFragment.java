package com.demo.currencyexchange;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.currencyexchange.mvibase.MviView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class CurrenciesFragment extends Fragment
        implements MviView<CurrenciesIntent, CurrenciesViewState> {

    static final String TAG = "CurrenciesFragment";

    private RecyclerView currenciesRecyclerView;
    private CurrenciesAdapter currenciesAdapter;

    private CurrenciesViewModel currenciesViewModel;
    private PublishSubject<CurrenciesIntent.RefreshIntent> userInputIntentPublisher =
            PublishSubject.create();
    private PublishSubject<CurrenciesIntent.AutoRefreshIntent> autoRefreshIntentPublisher =
            PublishSubject.create();
    private CompositeDisposable disposables = new CompositeDisposable();

    static CurrenciesFragment newInstance() {
        return new CurrenciesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currenciesAdapter = new CurrenciesAdapter(new ArrayList<>(0));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currenciesRecyclerView = view.findViewById(R.id.exchange_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        currenciesRecyclerView.setLayoutManager(linearLayoutManager);
        currenciesRecyclerView.setAdapter(currenciesAdapter);

        currenciesViewModel = ViewModelProviders.of(this).get(CurrenciesViewModel.class);

        bindViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();

        disposables.add(currenciesViewModel.states().subscribe(this::render, this::onError));
        disposables.add(currenciesAdapter.getCurrencyClickObservable().subscribe(
                this::onCurrencyClicked, this::onError
        ));
        disposables.add(currenciesAdapter.getCurrencyValueChangeObservable().subscribe(
                this::onCurrencyChanged, this::onError
        ));
        startRefreshRatesEverySecond();
    }

    @Override
    public void onPause() {
        super.onPause();
        disposables.clear();
    }

    private void bindViewModel() {
        currenciesViewModel.processIntents(intents());
    }

    @Override
    public Observable<CurrenciesIntent> intents() {
        return Observable.merge(initialIntent(), userInputIntent(), autoRefreshIntent());
    }

    @Override
    public void render(CurrenciesViewState state) {
        if (state.error() != null) {
            Log.e(TAG, "render", state.error());
            return;
        }

        if (state.isLoading()) {
            return;
        }

        if (state.currencies().isEmpty()) {

        } else {
            currenciesAdapter.updateData(state.currencies(), state.refreshAll());
            if (state.refreshAll()) {
                currenciesRecyclerView.scrollToPosition(0);
            }
        }
    }

    private Observable<CurrenciesIntent.InitialIntent> initialIntent() {
        return Observable.just(CurrenciesIntent.InitialIntent.create());
    }

    private Observable<CurrenciesIntent.RefreshIntent> userInputIntent() {
        return userInputIntentPublisher;
    }

    private Observable<CurrenciesIntent.AutoRefreshIntent> autoRefreshIntent() {
        return autoRefreshIntentPublisher;
    }

    private void onCurrencyClicked(Currency currency) {
        userInputIntentPublisher.onNext(CurrenciesIntent.RefreshIntent.create(currency, true));
    }

    private void onCurrencyChanged(Currency currency) {
        userInputIntentPublisher.onNext(CurrenciesIntent.RefreshIntent.create(currency, false));
    }

    private void startRefreshRatesEverySecond() {
        disposables.add(Observable
                .interval(1, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> {
                            final Currency base = currenciesAdapter.getBaseCurrency();
                            if (null != base) {
                                autoRefreshIntentPublisher.onNext(CurrenciesIntent.AutoRefreshIntent.create(base));
                            }
                        },
                        this::onError
                )
        );
    }

    private void onError(Throwable throwable) {
        Log.e(TAG, "onError", throwable);
    }
}
