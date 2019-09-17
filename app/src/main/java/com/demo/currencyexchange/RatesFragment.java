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

public class RatesFragment extends Fragment
        implements MviView<RatesIntent, RatesViewState> {

    static final String TAG = "RatesFragment";

    private RecyclerView currenciesRecyclerView;
    private RatesAdapter ratesAdapter;

    private RatesViewModel ratesViewModel;
    private PublishSubject<RatesIntent.RefreshIntent> userInputIntentPublisher =
            PublishSubject.create();
    private PublishSubject<RatesIntent.AutoRefreshIntent> autoRefreshIntentPublisher =
            PublishSubject.create();
    private CompositeDisposable disposables = new CompositeDisposable();

    static RatesFragment newInstance() {
        return new RatesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ratesAdapter = new RatesAdapter(new ArrayList<>(0));
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
        currenciesRecyclerView.setAdapter(ratesAdapter);

        ratesViewModel = ViewModelProviders.of(this).get(RatesViewModel.class);

        bindViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();

        disposables.add(ratesViewModel.states().subscribe(this::render, this::onError));
        disposables.add(ratesAdapter.getCurrencyClickObservable().subscribe(
                this::onCurrencyClicked, this::onError
        ));
        disposables.add(ratesAdapter.getCurrencyValueChangeObservable().subscribe(
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
        ratesViewModel.processIntents(intents());
    }

    @Override
    public Observable<RatesIntent> intents() {
        return Observable.merge(
                initialIntent(),
                userInputIntent(),
                autoRefreshIntent()
        );
    }

    @Override
    public void render(RatesViewState state) {
        if (state.error() != null) {
            Log.e(TAG, "render", state.error());
            return;
        }

        if (state.isLoading()) {
            return;
        }

        if (state.currencies().isEmpty()) {

        } else {
            ratesAdapter.updateData(state.currencies(), state.refreshAll());
            if (state.refreshAll()) {
                currenciesRecyclerView.scrollToPosition(0);
            }
        }
    }

    private Observable<RatesIntent.InitialIntent> initialIntent() {
        return Observable.just(RatesIntent.InitialIntent.create());
    }

    private Observable<RatesIntent.RefreshIntent> userInputIntent() {
        return userInputIntentPublisher;
    }

    private Observable<RatesIntent.AutoRefreshIntent> autoRefreshIntent() {
        RatesIntent.RefreshIntent defaultUserInput =
                RatesIntent.RefreshIntent.create(
                        Constants.INITIAL_BASE_RATE, false
                );

        Observable<RatesIntent.RefreshIntent> latestUserInput =
                userInputIntent().startWith(defaultUserInput);

        return autoRefreshIntentPublisher
                .withLatestFrom(
                        latestUserInput,
                        (autoRefreshIntent, latestInput) ->
                                RatesIntent.AutoRefreshIntent.create(latestInput.base()));
    }

    private void onCurrencyClicked(ExchangeRate exchangeRate) {
        userInputIntentPublisher.onNext(RatesIntent.RefreshIntent.create(exchangeRate, true));
    }

    private void onCurrencyChanged(ExchangeRate exchangeRate) {
        userInputIntentPublisher.onNext(RatesIntent.RefreshIntent.create(exchangeRate, false));
    }

    private void startRefreshRatesEverySecond() {
        disposables.add(Observable
                .interval(1, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ignored -> autoRefreshIntentPublisher.onNext(RatesIntent.AutoRefreshIntent.empty()),
                        this::onError
                )
        );
    }

    private void onError(Throwable throwable) {
        Log.e(TAG, "onError", throwable);
    }
}
