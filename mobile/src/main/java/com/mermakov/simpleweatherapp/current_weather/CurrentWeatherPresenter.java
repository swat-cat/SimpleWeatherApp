package com.mermakov.simpleweatherapp.current_weather;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferObserver;
import com.mermakov.simpleweatherapp.App;
import com.mermakov.simpleweatherapp.data.LocationModel;
import com.mermakov.simpleweatherapp.data.WeatherModel;
import com.mermakov.simpleweatherapp.data.dto.WeatherData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CurrentWeatherPresenter implements CurrentWeatherContract.UserActionEvents{
    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();

    private final CurrentWeatherContract.View view;
    private WeatherModel weatherModel;
    private LocationModel locationModel;

    public CurrentWeatherPresenter(final CurrentWeatherContract.View view) {
        this.view = view;
        view.showUI(false);
        weatherModel = new WeatherModel();
        locationModel = new LocationModel();
        resetCurrentWeather();
    }

    private void updateUI(CurrentWeatherView view) {
        view.syncBtnClick()
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        resetCurrentWeather();
                    }
                });
    }

    @Override
    public void resetCurrentWeather() {
        view.startSyncAnimation();
        view.showUI(false);
        locationModel.getLocation()
                .map(new Func1<Location, String>() {
                    @Override
                    public String call(Location location) {
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(App.getInstance().getApplicationContext(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            Observable.error(e);
                        }

                        String city = "";
                        if (addresses != null) {
                            city = addresses.get(0).getLocality();
                        }
                        return city;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                        view.showUI(true);
                    }

                    @Override
                    public void onNext(final String city) {
                        weatherModel.resetModel(city)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
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
                                        view.setupCity(city);
                                        if (weatherData.getMain() != null) {
                                            if (weatherData.getMain().getTemp() != null) {
                                                view.setupTemperature(weatherData.getMain().getTemp());
                                            }
                                            if (weatherData.getMain().getPressure() != null) {
                                                view.setupPressure(weatherData.getMain().getPressure());
                                            }
                                            if (weatherData.getMain().getHumidity() != null) {
                                                view.setupHumidity(weatherData.getMain().getHumidity());
                                            }
                                        }
                                        if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                                            view.setupWeatherStatus(weatherData.getWeather().get(0).getDescription());
                                        }
                                        if (weatherData.getWind() != null && weatherData.getWind().getSpeed() != null) {
                                            view.setupWindSpeed(weatherData.getWind().getSpeed());
                                        }
                                        updateUI((CurrentWeatherView) view);
                                        view.stopSyncAnimation();
                                    }
                                });
                    }
                });
    }
}
