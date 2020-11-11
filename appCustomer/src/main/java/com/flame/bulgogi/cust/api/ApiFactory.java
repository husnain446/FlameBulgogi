package com.flame.bulgogi.cust.api;

import android.content.Context;

import com.flame.bulgogi.cust.base.BaseApplication;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String HOST = "http://45.55.240.2/";
    private static final String BASE_URL = HOST + "flamebulgogi/";

    //    private static final String BASE_URL = "https://staging.bangableapp.com/api/web/";

    private Context context;

    private ApiFactory(Context context) {
        this.context = context;
    }

    public static ApiFactory getInstance(Context context) {
        return new ApiFactory(context);
    }

    private Retrofit provideRestAdapter() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(BaseApplication.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit provideRestAdapterEncryptDecrypt() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new EncryptDecryptConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create())))
                .client(BaseApplication.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <S> S provideService(Class<S> serviceClass) {
        return provideRestAdapter().create(serviceClass);
    }

    public <S> S provideEncryptDecryptService(Class<S> serviceClass) {
        return provideRestAdapterEncryptDecrypt().create(serviceClass);
    }

}