package com.mermakov.simpleweatherapp.data.rest;

import com.mermakov.simpleweatherapp.data.dto.WeatherData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WeatherApi {
    @GET
    WeatherData getWeather(@QueryMap(encoded=true) Map<String, String> options);

    @GET
    Observable<WeatherData> getCurrentWeather(@QueryMap(encoded=true) Map<String, String> options);
}
