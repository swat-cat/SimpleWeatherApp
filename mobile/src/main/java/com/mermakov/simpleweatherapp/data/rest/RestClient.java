package com.mermakov.simpleweatherapp.data.rest;

import android.net.Uri;
import android.text.TextUtils;

import com.mermakov.simpleweatherapp.Utils.Constants;

import java.io.IOException;

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
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(Constants.PROTOCOL)
                .authority(Constants.BASE_URL);

        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(uriBuilder.toString());


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newReq = request.newBuilder()
                            .build();
                    return chain.proceed(newReq);
                }
            }).build();

            builder.client(client);

        return builder.build().create(WeatherApi.class);
    }
}
