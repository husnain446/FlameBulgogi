package com.flame.bulgogi.cust.utils;

import android.util.Log;

import com.flame.bulgogi.cust.BuildConfig;


/**
 * Created by LN on 5/1/17.
 */

public class LogUtils {
    private static final String LOG_PREFIX = "Bangable_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;
    private static final String DEBUG_BUILD_TYPE = "debug";
    private static boolean LOGGING_ENABLED = false;

    static {
        if (BuildConfig.BUILD_TYPE.equals(DEBUG_BUILD_TYPE)) {
            LOGGING_ENABLED = true;
        }
    }

    private LogUtils() {
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

}