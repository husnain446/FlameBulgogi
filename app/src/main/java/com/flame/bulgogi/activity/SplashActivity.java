package com.flame.bulgogi.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;

import com.flame.bulgogi.R;
import com.flame.bulgogi.base.BaseAppCompatActivity;
import com.flame.bulgogi.utils.LogUtils;
import com.flame.bulgogi.utils.UserPreference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends BaseAppCompatActivity {

    private static final String TAG = LogUtils.makeLogTag(SplashActivity.class);

    private static final long SPLASH_TIME = 5000;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimer();

    }


    private void startTimer() {
        new CountDownTimer(SPLASH_TIME, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //Check user is logged in or not
                Intent intent = new Intent(mBaseActivity, BrowserActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
