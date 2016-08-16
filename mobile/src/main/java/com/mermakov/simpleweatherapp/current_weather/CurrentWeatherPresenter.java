package com.mermakov.simpleweatherapp.current_weather;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mermakov.simpleweatherapp.data.LocationModel;
import com.mermakov.simpleweatherapp.data.WeatherModel;
import com.mermakov.simpleweatherapp.data.dto.WeatherData;

import rx.Subscriber;
import rx.functions.Action1;

public class CurrentWeatherPresenter implements CurrentWeatherContract.UserActionEvents{
    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();

    private final CurrentWeatherContract.View view;
    private WeatherModel weatherModel;
    private LocationModel locationModel;

    public CurrentWeatherPresenter(final CurrentWeatherContract.View view, GoogleApiClient googleApiClient) {
        this.view = view;
        view.showUI(false);
        weatherModel = new WeatherModel();
        locationModel = new LocationModel(googleApiClient);
        resetCurrentWeather();
        ((CurrentWeatherView)view).syncBtnClick().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                locationModel.getLocation().subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        Log.d(TAG,"lat: "+location.getLatitude()+"lang: "+location.getLongitude());
                    }
                });
                resetCurrentWeather();
            }
        });
    }

    @Override
    public void resetCurrentWeather() {
        ((CurrentWeatherView)view).syncAnimation();
        weatherModel.resetModel("London")
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                        view.showUI(true);
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        view.showUI(true);
                        if (weatherData.getMain()!=null) {
                            if (weatherData.getMain().getTemp()!=null) {
                                view.setupTemperature(weatherData.getMain().getTemp());
                            }
                            if (weatherData.getMain().getPressure()!=null){
                                view.setupPressure(weatherData.getMain().getPressure());
                            }
                            if (weatherData.getMain().getHumidity()!=null){
                                view.setupHumidity(weatherData.getMain().getHumidity());
                            }
                        }
                        if (weatherData.getWeather()!=null&& !weatherData.getWeather().isEmpty()) {
                            view.setupWeatherStatus(weatherData.getWeather().get(0).getDescription());
                        }
                        if(weatherData.getWind()!=null && weatherData.getWind().getSpeed()!=null){
                            view.setupWindSpeed(weatherData.getWind().getSpeed());
                        }
                    }
                });
    }
}
