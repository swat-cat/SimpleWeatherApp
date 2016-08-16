package com.mermakov.simpleweatherapp.data.rest;

import com.mermakov.simpleweatherapp.Utils.Constants;
import com.mermakov.simpleweatherapp.data.dto.WeatherData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WeatherApi {
    @GET("data/2.5/weather?units=metric&apikey=" + Constants.API_KEY)
    WeatherData getWeather(@Query(value = "q", encoded = true) String address);

    @GET("data/2.5/weather?units=metric&apikey=" + Constants.API_KEY)
    Observable<WeatherData> getCurrentWeather(@Query(value = "q", encoded = true) String address);
}
