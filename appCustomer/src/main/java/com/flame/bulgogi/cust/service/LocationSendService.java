package com.flame.bulgogi.cust.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.flame.bulgogi.cust.api.ApiFactory;
import com.flame.bulgogi.cust.api.RxJava2ApiCallHelper;
import com.flame.bulgogi.cust.api.RxJava2ApiCallback;
import com.flame.bulgogi.cust.api.service.UserService;
import com.flame.bulgogi.cust.utils.Constants;
import com.flame.bulgogi.cust.utils.NetworkUtils;
import com.flame.bulgogi.cust.utils.UserPreference;
import com.google.gson.JsonObject;

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

        Observable<JsonObject> observable = userService.updatelocation(latitude, longitude, heading, tripId);
        disposable = RxJava2ApiCallHelper.call(observable, new RxJava2ApiCallback<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {
                disposeCall();
                Log.d(TAG, "onSuccess() called with: response = [" + response + "]");
                if (response != null && response.has("status")) {
                    String status = response.get("status").getAsString();
                    if (status.equalsIgnoreCase("continue")) {

                    } else if (status.equalsIgnoreCase("stop")) {
                        UserPreference.getInstance(mContext).setTripId("0");
                        UserPreference.getInstance(mContext).setTripStatus(Constants.USER_PREFERENCES.TRIP_STOP);

                    }
                }

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
