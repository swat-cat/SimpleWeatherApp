package com.mermakov.simpleweatherapp;

import android.app.Application;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        if (instance == null) {
            throw new RuntimeException("Application initialization error!");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
