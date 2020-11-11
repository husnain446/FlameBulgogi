package com.flame.bulgogi.cust.utils;

import android.content.Context;
import android.text.TextUtils;

import com.flame.bulgogi.cust.api.response.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class UserPreference {

    private Context mContext;

    private SharedPreferenceUtils helperPreference;

    private UserPreference(Context context) {
        mContext = context;
        helperPreference = SharedPreferenceUtils.getInstance(context);
    }

    public static UserPreference getInstance(Context context) {
        return new UserPreference(context);
    }

    public void save(String userProfileJson) {
        helperPreference.setValue(Constants.USER_PREFERENCES.USER_PREFERENCE, userProfileJson);
    }

    public boolean isUserLogin() {
        //return getUserData() != null;
        return helperPreference.getBoolanValue(Constants.USER_PREFERENCES.IS_USER_LOGIN, false);
    }

    public void setUserLogin(boolean isUserLogin) {
        helperPreference.setValue(Constants.USER_PREFERENCES.IS_USER_LOGIN, isUserLogin);
    }

    public String getLatitude() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.LATITUDE, "0");
    }

    public void setLatitude(double latitude) {
        helperPreference.setValue(Constants.USER_PREFERENCES.LATITUDE, String.valueOf(latitude));
    }

    public String getLongitude() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.LONGITUDE, "0");
    }

    public void setLongitude(double longitude) {
        helperPreference.setValue(Constants.USER_PREFERENCES.LONGITUDE, String.valueOf(longitude));
    }

    public String getHeading() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.HEADING, "0");
    }

    public void setHeading(double heading) {
        helperPreference.setValue(Constants.USER_PREFERENCES.HEADING, String.valueOf(heading));
    }


    public void setTripStatus(int tripStatus) {
        helperPreference.setValue(Constants.USER_PREFERENCES.TRIP_STATUS, String.valueOf(tripStatus));
    }

    public String getTripStatus() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.TRIP_STATUS, "0");
    }

    public String getDeviceToken() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.CONSTANT_FCM_TOKEN, "");
    }

    public void setDeviceToken(String deviceToken) {
        helperPreference.setValue(Constants.USER_PREFERENCES.CONSTANT_FCM_TOKEN, deviceToken);
    }

    public void setLoginUrl(String loginUrl) {
        helperPreference.setValue(Constants.USER_PREFERENCES.LOGIN_URL, loginUrl);
    }

    public String getLoginUrl() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.LOGIN_URL, "");
    }

    public void setTripId(String tripId) {
        helperPreference.setValue(Constants.USER_PREFERENCES.TRIP_ID, tripId);
    }

    public String getTripId() {
        return helperPreference.getStringValue(Constants.USER_PREFERENCES.TRIP_ID, "0");
    }


    public void clear() {
        helperPreference.setValue(Constants.USER_PREFERENCES.USER_PREFERENCE, "");
        helperPreference.setValue(Constants.USER_PREFERENCES.USER_DATA, "");
        setUserLogin(false);
    }

    public void clearUserDetails() {
        helperPreference.setValue(Constants.USER_PREFERENCES.USER_DATA, "");
        setUserLogin(false);
    }

    public void setUserData(LoginResponse.Data userData) {
        Gson gson = new Gson();
        Type type = new TypeToken<LoginResponse.Data>() {
        }.getType();
        String gsonModelToString = gson.toJson(userData, type);
        helperPreference.setValue(Constants.USER_PREFERENCES.USER_DATA, gsonModelToString);
    }

    public LoginResponse.Data getUserData() {
        LoginResponse.Data loData = null;
        String gsonSwipeDetails = helperPreference.getStringValue(Constants.USER_PREFERENCES.USER_DATA, "");
        if (!TextUtils.isEmpty(gsonSwipeDetails)) {
            loData = new Gson().fromJson(gsonSwipeDetails, LoginResponse.Data.class);
        }
        return loData;
    }

}
