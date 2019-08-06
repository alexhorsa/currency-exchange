package com.demo.currencyexchange;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ExchangeRatesClient {

    private static final String BASE_URL = "https://revolut.duckdns.org";

    public static <S> S createService(final Class<S> serviceClass) {

        HttpLoggingInterceptor debugInterceptor = new HttpLoggingInterceptor();
        debugInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .client(
                                new OkHttpClient.Builder()
                                        .addInterceptor(debugInterceptor)
                                        .build()
                        );

        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public interface ExchangeRatesApi {

        @GET("/latest")
        Single<ExchangeRatesResponse> getLatest(
                @Query("base") String baseCurrency
        );

    }
}
