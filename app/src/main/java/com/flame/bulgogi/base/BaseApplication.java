package com.flame.bulgogi.base;

import android.app.Application;
import android.arch.core.BuildConfig;
import android.content.Context;

import com.flame.bulgogi.api.OkClientFactory;

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
