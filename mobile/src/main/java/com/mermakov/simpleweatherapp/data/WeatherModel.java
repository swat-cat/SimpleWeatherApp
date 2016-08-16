package com.mermakov.simpleweatherapp.data;

import com.mermakov.simpleweatherapp.Utils.Constants;
import com.mermakov.simpleweatherapp.Utils.RetryWithDelay;
import com.mermakov.simpleweatherapp.data.dto.WeatherData;
import com.mermakov.simpleweatherapp.data.rest.RestClient;
import com.mermakov.simpleweatherapp.data.rest.WeatherApi;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherModel {
    private static final String TAG = WeatherModel.class.getSimpleName();

    private WeatherApi weatherApi;

    public WeatherModel() {
        weatherApi = RestClient.createWeatherApi();
    }

    public Observable<WeatherData> resetModel(String adrress){
        return weatherApi
                .getCurrentWeather(adrress)
                .retryWhen(new RetryWithDelay(5,1000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
