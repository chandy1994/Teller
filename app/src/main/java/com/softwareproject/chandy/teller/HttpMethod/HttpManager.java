package com.softwareproject.chandy.teller.HttpMethod;

import com.softwareproject.chandy.teller.Bean.HttpResult;
import com.softwareproject.chandy.teller.Interface.WeatherService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chandy on 2016/12/7.
 */

public class HttpManager {
    public static final String BASE_URL = "http://wthrcdn.etouch.cn/";
    private static final int DEFAULT_TIMEOUT = 5;
    private volatile static HttpManager httpManager;
    private Retrofit retrofit;
    private WeatherService weatherService;

    private HttpManager() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        weatherService=retrofit.create(WeatherService.class);
    }

    public static HttpManager getInstance() {
        if (null == httpManager) {
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    public void getWeather(Subscriber<HttpResult> subscriber){
        weatherService.getWeather()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
