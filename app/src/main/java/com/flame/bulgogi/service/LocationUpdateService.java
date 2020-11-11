package com.flame.bulgogi.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;


import com.flame.bulgogi.api.ApiFactory;
import com.flame.bulgogi.api.RxJava2ApiCallHelper;
import com.flame.bulgogi.api.service.UserService;
import com.flame.bulgogi.utils.NetworkUtils;
import com.flame.bulgogi.utils.UserPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * Created by ln-003 on 6/7/17.
 */

public class LocationUpdateService extends Service {

    private static final String TAG = LocationUpdateService.class.getSimpleName();

    private static final long UPDATE_LOCATION_DISTANCE = 10;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Service Running()");
            handler.postDelayed(runnable, 5000);
        }
    };
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        initLocation();

        return START_STICKY;
    }

    private void initLocation() {
        Log.d(TAG, "initLocation() called");

        mContext = this;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationCallback();
        createLocationRequest();
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d(TAG, "onSuccess() called with: location = [" + location + "]");
                    Location mCurrentLocation = location;
                    UserPreference.getInstance(mContext).setHeading(mCurrentLocation.getBearing());
                    UserPreference.getInstance(mContext).setLatitude(mCurrentLocation.getLatitude());
                    UserPreference.getInstance(mContext).setLongitude(mCurrentLocation.getLongitude());
                    Log.d(TAG, "onSuccess() called with: location = [" + mCurrentLocation + "]");

                    if (UserPreference.getInstance(mContext).getTripStatus().equals("1")) {
                        Intent intent = new Intent(mContext, LocationSendService.class);
                        startService(intent);
                    }
                }
                else{
                    Log.d(TAG, "onSuccess() called with: location = [ null ]");
                }
            }
        });

        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());


        handler = new Handler();
        // handler.postDelayed(runnable, 5000);

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(UPDATE_LOCATION_DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location mCurrentLocation = locationResult.getLastLocation();
                UserPreference.getInstance(mContext).setHeading(mCurrentLocation.getBearing());
                UserPreference.getInstance(mContext).setLatitude(mCurrentLocation.getLatitude());
                UserPreference.getInstance(mContext).setLongitude(mCurrentLocation.getLongitude());
                Log.d(TAG, "onSuccess() called with: location = [" + mCurrentLocation + "]");

                if (UserPreference.getInstance(mContext).getTripStatus().equals("1")) {
                    Intent intent = new Intent(mContext, LocationSendService.class);
                    startService(intent);
                }
            }
        };
    }
}
