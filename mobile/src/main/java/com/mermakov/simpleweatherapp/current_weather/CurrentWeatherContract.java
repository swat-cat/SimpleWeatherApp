package com.mermakov.simpleweatherapp.current_weather;

public interface CurrentWeatherContract {
    interface View{
        void showUI(boolean show);
        void setupTemperature(float temperature);
        void setupWeatherStatus(String status);
        void setupWindSpeed(float speed);
        void setupPressure(float pressure);
        void setupHumidity(float humidity);
    }

    interface UserActionEvents{
        void resetCurrentWeather();
    }
}
