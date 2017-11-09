package com.mermakov.simpleweatherapp.data;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.mermakov.simpleweatherapp.App;

import rx.Observable;
import rx.Subscriber;

public class LocationModel {
    private static final String TAG = LocationModel.class.getSimpleName();

    private LocationManager locationManager;

    public LocationModel() {
        locationManager = (LocationManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public Observable<Location> getLocation(){
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {

                final LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();

                        if (Looper.myLooper()!=null) {
                            Looper.myLooper().quit();
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };

                boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                String locationProvider ="";
                if(gpsStatus){
                    locationProvider = LocationManager.GPS_PROVIDER;
                }
                else if(networkStatus){
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                }
                else {
                    subscriber.onError(new NoAvailableProviderException());
                }
                Looper.prepare();

                try {
                    locationManager.requestSingleUpdate(locationProvider,
                            locationListener, Looper.myLooper());
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

                Looper.loop();
            }
        });
    }

}
