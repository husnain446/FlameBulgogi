package com.flame.bulgogi.cust.base;

import android.app.Application;
import android.content.Context;

import com.flame.bulgogi.cust.BuildConfig;
import com.flame.bulgogi.cust.api.OkClientFactory;

import okhttp3.OkHttpClient;


public class BaseApplication extends Application {

    private static OkHttpClient mOkHttpClient;
    private static BaseApplication mInstance;

    public static BaseApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //TODO: Set false for getting crash Report in release build
        // Fabric crashalytics
        if (BuildConfig.DEBUG) {
            //Fabric.with(this, new Crashlytics());
        }

        // Initialize application instance
        mInstance = this;

        // Initialize initializeOkHttpClient
        mOkHttpClient = OkClientFactory.provideOkHttpClient(this);


    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
