package com.mermakov.simpleweatherapp.current_weather;

import android.widget.Toast;

import com.mermakov.simpleweatherapp.App;
import com.mermakov.simpleweatherapp.data.WeatherModel;
import com.mermakov.simpleweatherapp.data.dto.WeatherData;

import rx.Subscriber;

public class CurrentWeatherPresenter implements CurrentWeatherContract.UserActionEvents{
    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();

    private CurrentWeatherContract.View view;
    private WeatherModel model;

    public CurrentWeatherPresenter(CurrentWeatherContract.View view) {
        this.view = view;
        view.showProgressIndicator(true);
        view.showUI(false);
        model = new WeatherModel();
        resetCurrentWeather();
    }

    @Override
    public void resetCurrentWeather() {
        model.resetModel("London")
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgressIndicator(false);
                        view.showUI(true);
                        Toast.makeText(App.getInstance().getApplicationContext(),"Internet connectivity error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        view.showProgressIndicator(false);
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
