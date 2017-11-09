package com.mermakov.simpleweatherapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mermakov.simpleweatherapp.current_weather.CurrentWeatherContract;
import com.mermakov.simpleweatherapp.current_weather.CurrentWeatherPresenter;
import com.mermakov.simpleweatherapp.current_weather.CurrentWeatherView;

public class MainActivity extends Activity{
    private CurrentWeatherContract.View view;
    private CurrentWeatherContract.UserActionEvents presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = new CurrentWeatherView(this);
        presenter = new CurrentWeatherPresenter(view);
    }
}
