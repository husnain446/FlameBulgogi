package com.flame.bulgogi.utils;


public class Constants {

    public static final int REQUEST_FOR_LOCATION_PERMISSIONS = 100;
    public static final int LOCATION_UPDATE_INTERVAL = 1000 * 60;
    public static final String SUCCESS = "1";
    public static final String HOST = "http://mymenuhub.com";

    public interface USER_PREFERENCES {
        String USER_PREFERENCE = "user_preference";
        String IS_USER_LOGIN = "is_user_login";
        String CONSTANT_FCM_TOKEN = "fcm_token";
        String LOGIN_URL = "login_url";
        String USER_DATA = "user_data";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String HEADING = "heading";
        String TRIP_STATUS = "trip_status";
        String TRIP_ID = "trip_id";
        int TRIP_START = 1;
        int TRIP_STOP = 0;
    }

    public interface EXTRA {
        String DATA = "data";
    }

    public interface TAB {
        int BIDS = 0;
        int MY_ORDERS = 1;
        int EARNINGS = 2;
        int ACCOUNT = 3;
        int NOTIFICATION = 4;
    }

    public interface URL {
        String BIDS = HOST + "/driver/bid/";
        String MY_ORDERS = HOST + "/driver/bid/index/order/";
        String EARNINGS = HOST + "/driver/bid/index/earnings/";
        String ACCOUNT = HOST + "/driver/customer/account/";
        String NOTIFICATION = HOST + "/driver/bid/index/notification/";

        String TRIP_START = HOST + "/driver/bid/index/trip/trip_id/";
        String TRIP_STOP = HOST + "/driver/bid/index/saveTrip/";

    }

}
