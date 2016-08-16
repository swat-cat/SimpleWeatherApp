package com.mermakov.simpleweatherapp.data.rest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.mermakov.simpleweatherapp.Utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

import static java.lang.String.format;

public class RestClient {
    private static final String TAG = RestClient.class.getSimpleName();

    public RestClient() {
    }

    public static WeatherApi createWeatherApi(){

        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org");


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = null;
                    try {
                        response = chain.proceed(chain.request());
                    } catch (IOException e) {
                        Log.d(TAG, "Lost connectivity");
                    }

                    if (response!=null) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                Log.d(TAG,"forbidden");
                                break;
                            case HttpURLConnection.HTTP_UNAUTHORIZED:
                                Log.d(TAG,"unauthorized");
                                break;
                        }
                        if(response.code()!=HttpURLConnection.HTTP_OK){
                            response.body().close();
                        }
                    }
                    Request newReq = request.newBuilder()
                            .build();
                    Log.d(TAG, request.url().toString());
                    return chain.proceed(newReq);
                }
            }).build();

            builder.client(client);

        return builder.build().create(WeatherApi.class);
    }
}
