package com.flame.bulgogi.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.flame.bulgogi.utils.LogUtils;
import com.flame.bulgogi.utils.UserPreference;


/**
 * Created by ln-003 on 8/8/17.
 */

public class LocationUpdateServiceNew extends Service {

    private static final String TAG = LocationUpdateServiceNew.class.getSimpleName();

    private static final long UPDATE_LOCATION_DISTANCE = 50;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 1;
    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "Service Running()");
            handler.postDelayed(runnable, 5000);
        }
    };

    private Context mContext;
    private LocationManager locationManager;
    private Location location;
    private LocationUpdateListener locationUpdateListener;
    private String locationProvider = "";
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                Log.e(TAG, "onLocationChanged() called " + location);
                UserPreference.getInstance(mContext).setHeading(location.getBearing());
                UserPreference.getInstance(mContext).setLatitude(location.getLatitude());
                UserPreference.getInstance(mContext).setLongitude(location.getLongitude());

            }

            if (locationUpdateListener != null) {
                locationUpdateListener.onLocationUpdate(location, locationProvider);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged() called with: provider = [" + provider + "], status = [" + status + "], extras = [" + extras + "]");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled() called with: provider = [" + provider + "]");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled() called with: provider = [" + provider + "]");
        }


    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        initLocation();

        return START_STICKY;
    }

    private void initLocation() {
        Log.e(TAG, "initLocation() called");

        mContext = this;

        handler = new Handler();


        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationProvider = LocationManager.GPS_PROVIDER;
       /* location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            Log.d(TAG, "Location() called "+location);
            if (locationUpdateListener != null) {
                locationUpdateListener.onLocationUpdate(location, locationProvider);
            }
        }
        else{
            Log.d(TAG, "Location() null ");
        }
        locationProvider = LocationManager.NETWORK_PROVIDER;*/
//        locationManager.clearTestProviderLocation(locationProvider);
//        locationManager.clearTestProviderEnabled(locationProvider);
//        locationManager.clearTestProviderStatus(locationProvider);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, UPDATE_LOCATION_DISTANCE,
                locationListener);
        if (locationManager != null) {

            location = locationManager.getLastKnownLocation(locationProvider);

            if (location != null) {
                Log.e(TAG, "Location() called " + location);
                UserPreference.getInstance(mContext).setHeading(location.getBearing());
                UserPreference.getInstance(mContext).setLatitude(location.getLatitude());
                UserPreference.getInstance(mContext).setLongitude(location.getLongitude());

                if (UserPreference.getInstance(mContext).getTripStatus().equals("1")) {
                    Intent intent = new Intent(mContext, LocationSendService.class);
                    startService(intent);
                }
            } else {
                Log.e(TAG, "Location() null ");
            }
        }

    }

    public interface LocationUpdateListener {
        void onLocationUpdate(Location location, String provider);
    }

}
