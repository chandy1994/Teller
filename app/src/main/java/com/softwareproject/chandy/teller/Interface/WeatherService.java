package com.softwareproject.chandy.teller.Interface;

import com.softwareproject.chandy.teller.Bean.HttpResult;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Chandy on 2016/12/7.
 */

public interface WeatherService {
    @GET("weather_mini?city=沈阳")
    Observable<HttpResult> getWeather();
}
