package com.demo.currencyexchange.rates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.demo.currencyexchange.util.Constants;
import com.demo.currencyexchange.mvibase.MviIntent;
import com.demo.currencyexchange.mvibase.MviResult;
import com.demo.currencyexchange.mvibase.MviView;
import com.demo.currencyexchange.mvibase.MviViewModel;
import com.demo.currencyexchange.mvibase.MviViewState;

import java.math.BigDecimal;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RatesViewModel extends AndroidViewModel
        implements MviViewModel<RatesIntent, RatesViewState> {

    private RatesRepository ratesRepository;

    @NonNull
    private PublishSubject<RatesIntent> intentsSubject;
    @NonNull
    private Observable<RatesViewState> statesObservables;

    public RatesViewModel(@NonNull Application application) {
        super(application);

        intentsSubject = PublishSubject.create();
        statesObservables = compose();

        ratesRepository = new RatesRepository();
    }

    public void processIntents(Observable<RatesIntent> intents) {
        intents.subscribe(intentsSubject);
    }

    @Override
    public Observable<RatesViewState> states() {
        return statesObservables;
    }

    private Observable<RatesViewState> compose() {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(RatesViewState.idle(), reducer)
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

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private ObservableTransformer<RatesIntent, RatesIntent> intentFilter =
            intents -> intents.publish(shared ->
                    Observable.merge(
                            shared.ofType(RatesIntent.InitialIntent.class).take(1),
                            shared.filter(intent -> !(intent instanceof RatesIntent.InitialIntent))
                    )
            );

    private RatesAction actionFromIntent(MviIntent intent) {
        if (intent instanceof RatesIntent.InitialIntent) {
            return RatesAction.LoadRates.load(Constants.INITIAL_BASE_RATE.code);
        }
        if (intent instanceof RatesIntent.RefreshIntent) {
            RatesIntent.RefreshIntent refreshIntent = (RatesIntent.RefreshIntent) intent;
            return RatesAction.ComputeExchangeRate.compute(refreshIntent.base(), refreshIntent.refreshAll());
        }
        if (intent instanceof RatesIntent.AutoRefreshIntent) {
            RatesIntent.AutoRefreshIntent autoRefreshIntent = (RatesIntent.AutoRefreshIntent) intent;
            return RatesAction.AutoRefreshRates.create(autoRefreshIntent.base());
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
    private static BiFunction<RatesViewState, RatesResult, RatesViewState> reducer =
            (previousState, result) -> {

        RatesViewState.Builder stateBuilder = previousState.buildWith();

        if (result instanceof RatesResult.LoadRates) {
            RatesResult.LoadRates loadResult = (RatesResult.LoadRates) result;

            switch (loadResult.status()) {
                case SUCCESS:
                    return stateBuilder
                            .isLoading(false)
                            .rates(loadResult.rates())
                            .refreshAll(loadResult.refreshAll())
                            .error(null)
                            .build();
                case FAILURE:
                    return stateBuilder.isLoading(false).error(loadResult.error()).build();
                case IN_FLIGHT:
                    return stateBuilder.isLoading(true).error(null).build();
            }
        } else {
            throw new IllegalArgumentException("Don't know this result " + result);
        }

        throw new IllegalStateException("Mishandled result? Should never happen.");

    };

    private ObservableTransformer<RatesAction.LoadRates, RatesResult.LoadRates> fetchRatesProcessor =
            actions -> actions.flatMap(action ->
                    ratesRepository.getRates(action.base())
//                            .flatMap(result -> Single.just(result.rates.toExchangeRatesList()))
                            // Transform the Single to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map(response
                                    -> RatesResult.LoadRates.success(
                                            response.base, response.toExchangeRatesList(BigDecimal.ONE)
                                    )
                            )
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn(RatesResult.LoadRates::failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            // We emit it after observing on the UI thread to allow the event to be emitted
                            // on the current frame and avoid jank.
                            .startWith(RatesResult.LoadRates.inFlight()));

    private ObservableTransformer<RatesAction.ComputeExchangeRate, RatesResult.LoadRates> adjustRatesProcessor =
            actions -> actions.flatMap(action ->
                            ratesRepository.getRates(action.base().code)
                            // Transform the Single to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map(result -> RatesResult.LoadRates.success(
                                    result.base,
                                    result.toExchangeRatesList(action.base().value),
                                    action.refreshAll())
                            )
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn(RatesResult.LoadRates::failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            // We emit it after observing on the UI thread to allow the event to be emitted
                            // on the current frame and avoid jank.
                            .startWith(RatesResult.LoadRates.inFlight()));

    private ObservableTransformer<RatesAction.AutoRefreshRates, RatesResult.LoadRates> autoRefreshRatesProcessor =
            actions -> actions.flatMap(action ->
                    ratesRepository.forceRefresh(action.base().code)
                            // Transform the Single to an Observable to allow emission of multiple
                            // events down the stream (e.g. the InFlight event)
                            .toObservable()
                            // Wrap returned data into an immutable object
                            .map(result -> RatesResult.LoadRates.success(
                                    result.base,
                                    result.toExchangeRatesList(action.base().value),
                                    false)
                            )
                            // Wrap any error into an immutable object and pass it down the stream
                            // without crashing.
                            // Because errors are data and hence, should just be part of the stream.
                            .onErrorReturn(RatesResult.LoadRates::failure)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
                            // doing work and waiting on a response.
                            // We emit it after observing on the UI thread to allow the event to be emitted
                            // on the current frame and avoid jank.
                            .startWith(RatesResult.LoadRates.inFlight()));

    private ObservableTransformer<RatesAction, RatesResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    // Match LoadRates to loadRatesProcessor
                    shared.ofType(RatesAction.LoadRates.class).compose(fetchRatesProcessor),
                    // Match ComputeRates to ratesProcessor
                    shared.ofType(RatesAction.ComputeExchangeRate.class).compose(adjustRatesProcessor),
                    shared.ofType(RatesAction.AutoRefreshRates.class).compose(autoRefreshRatesProcessor)
                    .mergeWith(
                            // Error for not implemented actions
                            shared.filter(v -> !(v instanceof RatesAction.LoadRates)
                                    && !(v instanceof RatesAction.ComputeExchangeRate)
                                    && !(v instanceof RatesAction.AutoRefreshRates))
                                    .flatMap(w -> Observable.error(
                                            new IllegalArgumentException("Unknown Action type: " + w))))));

}
