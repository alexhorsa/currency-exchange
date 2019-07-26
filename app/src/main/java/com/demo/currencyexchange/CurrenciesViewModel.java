package com.demo.currencyexchange;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.demo.currencyexchange.mvibase.MviIntent;
import com.demo.currencyexchange.mvibase.MviResult;
import com.demo.currencyexchange.mvibase.MviView;
import com.demo.currencyexchange.mvibase.MviViewModel;
import com.demo.currencyexchange.mvibase.MviViewState;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class CurrenciesViewModel extends AndroidViewModel
        implements MviViewModel<CurrenciesIntent, CurrenciesViewState> {

    private CurrenciesRepository currenciesRepository;

    @NonNull
    private PublishSubject<CurrenciesIntent> intentsSubject;
    @NonNull
    private Observable<CurrenciesViewState> statesObservables;

    public CurrenciesViewModel(@NonNull Application application) {
        super(application);

        intentsSubject = PublishSubject.create();
        statesObservables = compose();

        currenciesRepository = new CurrenciesRepository();
    }

    public void processIntents(Observable<CurrenciesIntent> intents) {
        intents.subscribe(intentsSubject);
    }

    @Override
    public Observable<CurrenciesViewState> states() {
        return statesObservables;
    }

    private Observable<CurrenciesViewState> compose() {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(CurrenciesViewState.idle(), reducer)
                // When a reducer just emits previousState, there's no reason to call render. In fact,
                // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
                // by showing the same snackbar twice in rapid succession).
                .distinctUntilChanged()
                // Emit the last one event of the stream on subscription
                // Useful when a View rebinds to the ViewModel after rotation.
                .replay(1)
                // Create the stream on creation without waiting for anyone to subscribe
                // This allows the stream to stay alive even when the UI disconnects and
                // match the stream's lifecycle to the ViewModel's one.
                .autoConnect(0);
    }

    private ObservableTransformer<CurrenciesIntent, CurrenciesIntent> intentFilter =
            intents -> intents.publish(shared ->
                    Observable.merge(
                            shared.ofType(CurrenciesIntent.InitialIntent.class).take(1),
                            shared.filter(intent -> !(intent instanceof CurrenciesIntent.InitialIntent))
                    )
            );

    private CurrenciesAction actionFromIntent(MviIntent intent) {
        if (intent instanceof CurrenciesIntent.InitialIntent) {
            return CurrenciesAction.LoadCurrencies.load();
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    /**
     * The Reducer is where {@link MviViewState}, that the {@link MviView} will use to
     * render itself, are created.
     * It takes the last cached {@link MviViewState}, the latest {@link MviResult} and
     * creates a new {@link MviViewState} by only updating the related fields.
     * This is basically like a big switch statement of all possible types for the {@link MviResult}
     */
    private static BiFunction<CurrenciesViewState, CurrenciesResult, CurrenciesViewState> reducer =
            (previousState, result) -> {

        CurrenciesViewState.Builder stateBuilder = previousState.buildWith();

                if (result instanceof CurrenciesResult.LoadCurrencies) {
                    CurrenciesResult.LoadCurrencies loadResult = (CurrenciesResult.LoadCurrencies) result;

                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).currencies(loadResult.currencies()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else {
                    throw new IllegalArgumentException("Don't know this result " + result);
                }

                throw new IllegalStateException("Mishandled result? Should never happen.");

            };

    private ObservableTransformer<CurrenciesAction.LoadCurrencies, CurrenciesResult.LoadCurrencies> loadCurrenciesProcessor =
            actions -> actions.flatMap(action ->
                    currenciesRepository.getRates("")
                            .flatMap(result -> Single.just(result.rates.toCurrenciesList()))
                            // Transform the Single to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map(CurrenciesResult.LoadCurrencies::success)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn(CurrenciesResult.LoadCurrencies::failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            // We emit it after observing on the UI thread to allow the event to be emitted
                            // on the current frame and avoid jank.
                            .startWith(CurrenciesResult.LoadCurrencies.inFlight()));

    private ObservableTransformer<CurrenciesAction.ComputeExchangeRate, CurrenciesResult.LoadCurrencies> exchangeRatesProcessor =
            actions -> actions.flatMap(action ->
                    Single.just(new ArrayList<Currency>(0))
                            // Transform the Single to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map(CurrenciesResult.LoadCurrencies::success)
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn(CurrenciesResult.LoadCurrencies::failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            // We emit it after observing on the UI thread to allow the event to be emitted
                            // on the current frame and avoid jank.
                            .startWith(CurrenciesResult.LoadCurrencies.inFlight()));

    private ObservableTransformer<CurrenciesAction, CurrenciesResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    // Match LoadTasks to loadTasksProcessor
                    shared.ofType(CurrenciesAction.LoadCurrencies.class).compose(loadCurrenciesProcessor),
                    // Match ActivateTaskAction to populateTaskProcessor
                    shared.ofType(CurrenciesAction.ComputeExchangeRate.class).compose(exchangeRatesProcessor)
                    .mergeWith(
                            // Error for not implemented actions
                            shared.filter(v -> !(v instanceof CurrenciesAction.LoadCurrencies)
                                    && !(v instanceof CurrenciesAction.ComputeExchangeRate))
                                    .flatMap(w -> Observable.error(
                                            new IllegalArgumentException("Unknown Action type: " + w))))));

}
