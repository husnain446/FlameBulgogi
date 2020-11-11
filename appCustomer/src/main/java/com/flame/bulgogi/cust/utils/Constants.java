package com.flame.bulgogi.cust.utils;


public class Constants {

    public static final int REQUEST_FOR_LOCATION_PERMISSIONS = 100;
    public static final int LOCATION_UPDATE_INTERVAL = 1000 * 60;
    public static final String SUCCESS = "1";
    public static final String HOST = "http://mymenuhub.com/";

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
        String LOGIN = HOST + "pickup/customer/account/login/";
        String AFTER_LOGIN = HOST + "pickup/sales/order/history/";
        String LOGOUT = HOST + "pickup/customer/account/logout/";
        String TRIP_START = HOST + "pickup/sales/order/view/order_id/";
        String TRIP_STOP = HOST + "pickup/customerpickup/index/stoptrip/order_id/";

    }

}
