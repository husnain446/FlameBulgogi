package com.flame.bulgogi.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.flame.bulgogi.api.ApiFactory;
import com.flame.bulgogi.api.RxJava2ApiCallHelper;
import com.flame.bulgogi.api.RxJava2ApiCallback;
import com.flame.bulgogi.api.service.UserService;
import com.flame.bulgogi.utils.NetworkUtils;
import com.flame.bulgogi.utils.UserPreference;
import com.google.android.gms.location.LocationRequest;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * Created by ln-003 on 6/7/17.
 */

public class LocationSendService extends IntentService {

    private static final String TAG = LocationSendService.class.getSimpleName();

    private Disposable disposable;
    private Context mContext;

    public LocationSendService() {
        super(LocationSendService.class.getName());
    }

    public LocationSendService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mContext = this;
        if (NetworkUtils.isInternetAvailable(LocationSendService.this)) {
            Log.d(TAG, "onHandleIntent() called with: intent = [" + intent + "]");
            callLocationUpdateAPI();
        }

    }

    private void callLocationUpdateAPI() {
        Log.d(TAG, "callLocationUpdateAPI() called");
        UserService userService = ApiFactory.getInstance(mContext).provideService(UserService.class);

        String latitude = UserPreference.getInstance(mContext).getLatitude();
        String longitude = UserPreference.getInstance(mContext).getLongitude();
        String tripId = UserPreference.getInstance(mContext).getTripId();
        String heading = UserPreference.getInstance(mContext).getHeading();

        Observable<String> observable = userService.updatelocation(latitude, longitude, heading, tripId);
        disposable = RxJava2ApiCallHelper.call(observable, new RxJava2ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                disposeCall();
                Log.d(TAG, "onSuccess() called with: response = [" + response + "]");

            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.d(TAG, "onFailed() called with: throwable = [" + throwable + "]");
                disposeCall();
            }
        });
    }

    private void disposeCall() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
