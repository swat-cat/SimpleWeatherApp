package com.mermakov.simpleweatherapp.current_weather;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.mermakov.simpleweatherapp.R;
import com.mermakov.simpleweatherapp.Utils.TemperatureFormatter;

import rx.Observable;
import rx.Subscriber;

public class CurrentWeatherView implements CurrentWeatherContract.View {
    private static final String TAG = CurrentWeatherView.class.getSimpleName();

    private Activity activity;
    private View root;
    private TextView city;
    private TextView temperature;
    private TextView status;
    private TextView windSpeed;
    private TextView pressure;
    private TextView himidity;
    private ImageView syncButton;


    public CurrentWeatherView(Activity activity) {
        this.activity = activity;
        root = activity.findViewById(R.id.main_root);
        initUI();
    }

    private void initUI(){
        city = (TextView)root.findViewById(R.id.city);
        temperature = (TextView)root.findViewById(R.id.temperature);
        status = (TextView)root.findViewById(R.id.weather_status);
        windSpeed = (TextView)root.findViewById(R.id.wind_speed);
        pressure = (TextView)root.findViewById(R.id.pressure);
        himidity = (TextView)root.findViewById(R.id.humidity);
        syncButton = (ImageView)root.findViewById(R.id.synchronize_btn);
    }

    @Override
    public void showUI(boolean show) {
        if(show){
            city.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            windSpeed.setVisibility(View.VISIBLE);
            pressure.setVisibility(View.VISIBLE);
            himidity.setVisibility(View.VISIBLE);
        }
        else {
            city.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            windSpeed.setVisibility(View.GONE);
            pressure.setVisibility(View.GONE);
            himidity.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupCity(String city) {
        this.city.setText(city);
    }

    @Override
    public void setupTemperature(float temperature) {
        this.temperature.setText(TemperatureFormatter.format(temperature));
    }

    @Override
    public void setupWeatherStatus(String status) {
        this.status.setText(status);
    }

    @Override
    public void setupWindSpeed(float speed) {
        this.windSpeed.setText(root.getResources().getString(R.string.wind_speed,String.valueOf(speed)));
    }

    @Override
    public void setupPressure(float pressure) {
        this.pressure.setText(root.getResources().getString(R.string.pressure,String.valueOf(pressure)));
    }

    @Override
    public void setupHumidity(float humidity) {
        this.himidity.setText(root.getResources().getString(R.string.humidity,String.valueOf(humidity)));
    }

    @Override
    public void startSyncAnimation() {
        syncButton.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.rotate));
    }

    @Override
    public void stopSyncAnimation() {
        syncButton.clearAnimation();
    }

    public Observable<Void> syncBtnClick(){
        return RxView.clicks(syncButton);
    }
}
