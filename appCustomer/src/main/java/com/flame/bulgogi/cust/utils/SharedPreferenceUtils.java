package com.flame.bulgogi.cust.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceUtils {
    private static final String TAG = LogUtils.makeLogTag(SharedPreferenceUtils.class);
    private static SharedPreferenceUtils sInstance;
    protected Context mContext;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private SharedPreferenceUtils(Context context) {
        mContext = context;
        int stringId = context.getApplicationInfo().labelRes;
        pref = context.getSharedPreferences(context.getString(stringId) + "_SharedPreferences", 0);
        editor = pref.edit();
    }

    public static synchronized SharedPreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceUtils(context.getApplicationContext());
        }
        return sInstance;
    }

    public void setValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void setValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }


    public String getStringValue(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        return pref.getBoolean(keyFlag, defaultValue);
    }
}


