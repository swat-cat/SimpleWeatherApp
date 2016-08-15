package com.mermakov.simpleweatherapp.current_weather;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mermakov.simpleweatherapp.R;
import com.mermakov.simpleweatherapp.Utils.TemperatureFormatter;

import rx.Observable;
import rx.Subscriber;

public class CurrentWeatherView implements CurrentWeatherContract.View {
    private static final String TAG = CurrentWeatherView.class.getSimpleName();

    private Activity activity;
    private View root;
    private TextView temperature;
    private TextView status;
    private TextView windSpeed;
    private TextView pressure;
    private TextView himidity;
    private ImageView syncButton;
    private ProgressBar progressBar;

    public CurrentWeatherView(Activity activity) {
        this.activity = activity;
        root = activity.findViewById(R.id.main_root);
        initUI();
    }

    private void initUI(){
        temperature = (TextView)root.findViewById(R.id.temperature);
        status = (TextView)root.findViewById(R.id.weather_status);
        windSpeed = (TextView)root.findViewById(R.id.wind_speed);
        pressure = (TextView)root.findViewById(R.id.pressure);
        himidity = (TextView)root.findViewById(R.id.humidity);
        syncButton = (ImageView)root.findViewById(R.id.synchronize_btn);
        progressBar = (ProgressBar)root.findViewById(R.id.progress);
    }

    public Observable<View> setupSyncBtnListener(){
        return Observable.create(new Observable.OnSubscribe<View>() {
            @Override
            public void call(final Subscriber<? super View> subscriber) {
               syncButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (subscriber.isUnsubscribed()) return;
                        subscriber.onNext(v);
                    }
                });
            }
        });
    }

    @Override
    public void showProgressIndicator(boolean show) {
        if(show)progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUI(boolean show) {
        if(show){
            temperature.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            windSpeed.setVisibility(View.VISIBLE);
            pressure.setVisibility(View.VISIBLE);
            himidity.setVisibility(View.VISIBLE);
        }
        else {
            temperature.setVisibility(View.GONE);
            status.setVisibility(View.GONE);
            windSpeed.setVisibility(View.GONE);
            pressure.setVisibility(View.GONE);
            himidity.setVisibility(View.GONE);
        }
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
        this.windSpeed.setText(String.valueOf(speed));
    }

    @Override
    public void setupPressure(float pressure) {
        this.pressure.setText(String.valueOf(pressure));
    }

    @Override
    public void setupHumidity(float humidity) {
        this.himidity.setText(String.valueOf(humidity));
    }
}
