package com.flame.bulgogi.cust.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;

import com.flame.bulgogi.cust.R;
import com.flame.bulgogi.cust.base.BaseAppCompatActivity;
import com.flame.bulgogi.cust.utils.LogUtils;
import com.flame.bulgogi.cust.utils.UserPreference;

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

//        getAppKeyHash();

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
                boolean boolLogin = UserPreference.getInstance(mBaseActivity).isUserLogin();
                Intent intent = new Intent(mBaseActivity, BrowserActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        }.start();
    }

    /*
    Generate facebook sh1 key
     */
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("HASH  " + something);


            }
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            LogUtils.LOGE("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {

            LogUtils.LOGE("no such an algorithm", e.toString());
        } catch (Exception e) {
            LogUtils.LOGE("exception", e.toString());
        }
    }
}
