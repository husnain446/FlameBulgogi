package com.flame.bulgogi.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


import com.flame.bulgogi.R;
import com.flame.bulgogi.base.BaseAppCompatActivity;
import com.flame.bulgogi.service.LocationSendService;
import com.flame.bulgogi.service.LocationUpdateService;
import com.flame.bulgogi.service.LocationUpdateServiceNew;
import com.flame.bulgogi.utils.Constants;
import com.flame.bulgogi.utils.UserPreference;
import com.flame.bulgogi.view.CustomTextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.R.attr.data;
import static com.flame.bulgogi.R.id.webview;

public class BrowserActivity extends BaseAppCompatActivity implements EasyPermissions.PermissionCallbacks, TabLayout.OnTabSelectedListener {

    private static final String TAG = BrowserActivity.class.getName();

    @BindView(webview)
    WebView mWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private Handler mHandler;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private String logoutUrl = "http://mymenuhub.com/driver/customer/account/logout";
    private String loginUrl = "http://mymenuhub.com/driver/customer/account/loginPost/referer";
    /*Runnable mRunnableLocationUpdate = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(mBaseAppCompatActivity, LocationUpdateService.class);
            startService(intent);

            mHandler.postDelayed(mRunnableLocationUpdate, Constants.LOCATION_UPDATE_INTERVAL);
        }
    };*/

    private ValueCallback<Uri[]> mFilePathCallback;
    private final static int FILECHOOSER_RESULTCODE=1;

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, BrowserActivity.class);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_browser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLocationUpdate();

        initComponents();

        checkLocationPermission();

        setupTabs();

        if(!UserPreference.getInstance(mBaseActivity).isUserLogin()){
//            tabLayout.setVisibility(View.GONE);
            tabLayout.getTabAt(Constants.TAB.ACCOUNT).select();
        }
    }

    private void initComponents() {

        toolbar.setVisibility(View.GONE);
      /*  setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


        
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted() = [" + url + "]");
                showProgressDialog();

                if(url.startsWith(logoutUrl)){
                    UserPreference.getInstance(mBaseActivity).setUserLogin(false);
//                    tabLayout.setVisibility(View.GONE);
                }
                else if(url.startsWith(loginUrl)){
                    UserPreference.getInstance(mBaseActivity).setLoginUrl(url);
                }
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading() = [" + url + "]");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished() = [" + url + "]");

                checkTripUrl(url);

                hideProgressDialog();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            // Need to accept permissions to use the camera
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest: ");
                request.grant(request.getResources());
            }

            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                if(mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;


                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[]  intentArray = new Intent[0];

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(this);

        loadTab(Constants.TAB.BIDS);

    }

    private void checkTripUrl(String url) {

        String afterLogin = "http://mymenuhub.com/driver/customer/account/index";
        if(url.startsWith(afterLogin)){
            UserPreference.getInstance(mBaseActivity).setUserLogin(true);
//            tabLayout.setVisibility(View.VISIBLE);
        }
        else{
            int tripstatus = Integer.parseInt(UserPreference.getInstance(mBaseActivity).getTripStatus());

            switch (tripstatus){
                case 0:
                    if(url.contains(Constants.URL.TRIP_START)){
                        String[] splitUrl = url.split("/");
                        String tripId = splitUrl[splitUrl.length-1];
                        Log.d(TAG, "splitUrl()  = [" + tripId + "]");
                        UserPreference.getInstance(mBaseActivity).setTripId(tripId);
                        UserPreference.getInstance(mBaseActivity).setTripStatus(Constants.USER_PREFERENCES.TRIP_START);

                        startLocationUpdate();

                        Intent intent = new Intent(mBaseActivity, LocationSendService.class);
                        startService(intent);

                    }
                    break;

                case 1:
                    if(url.contains(Constants.URL.TRIP_STOP)){
                        UserPreference.getInstance(mBaseActivity).setTripId("0");
                        UserPreference.getInstance(mBaseActivity).setTripStatus(Constants.USER_PREFERENCES.TRIP_STOP);
                    }
                    break;
            }
        }

    }

    private void setupTabs() {
        View viewTabBids = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgBids = (ImageView) viewTabBids.findViewById(R.id.imageMenu);
        CustomTextView textBids = (CustomTextView) viewTabBids.findViewById(R.id.textMenu);
        textBids.setText(getString(R.string.tab_bids));
        imgBids.setImageResource(R.drawable.tab_bids);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTabBids));

        View viewTabOrder = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgOrder = (ImageView) viewTabOrder.findViewById(R.id.imageMenu);
        CustomTextView textOrder = (CustomTextView) viewTabOrder.findViewById(R.id.textMenu);
        textOrder.setText(getString(R.string.tab_order));
        imgOrder.setImageResource(R.drawable.tab_order);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTabOrder));

        View viewTabEarning = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgEarning = (ImageView) viewTabEarning.findViewById(R.id.imageMenu);
        CustomTextView textEarning = (CustomTextView) viewTabEarning.findViewById(R.id.textMenu);
        textEarning.setText(getString(R.string.tab_earning));
        imgEarning.setImageResource(R.drawable.tab_earning);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTabEarning));

        View viewTabAccount = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgAccount = (ImageView) viewTabAccount.findViewById(R.id.imageMenu);
        CustomTextView textAccount = (CustomTextView) viewTabAccount.findViewById(R.id.textMenu);
        textAccount.setText(getString(R.string.tab_account));
        imgAccount.setImageResource(R.drawable.tab_account);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTabAccount));

        View viewTabNotification = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgNotification = (ImageView) viewTabNotification.findViewById(R.id.imageMenu);
        CustomTextView textNotification = (CustomTextView) viewTabNotification.findViewById(R.id.textMenu);
        textNotification.setText(getString(R.string.tab_notification));
        imgNotification.setImageResource(R.drawable.tab_notification);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTabNotification));

    }

    private void loadTab(int tab) {
        switch (tab){
            case Constants.TAB.BIDS:
                mWebView.loadUrl(Constants.URL.BIDS);
                break;

            case Constants.TAB.MY_ORDERS:
                mWebView.loadUrl(Constants.URL.MY_ORDERS);
                break;

            case Constants.TAB.EARNINGS:
                mWebView.loadUrl(Constants.URL.EARNINGS);
                break;

            case Constants.TAB.ACCOUNT:
                if(UserPreference.getInstance(mBaseActivity).isUserLogin()){
                    mWebView.loadUrl(UserPreference.getInstance(mBaseActivity).getLoginUrl());
                }
                else{
                    mWebView.loadUrl(Constants.URL.ACCOUNT);
                }

                break;

            case Constants.TAB.NOTIFICATION:
                mWebView.loadUrl(Constants.URL.NOTIFICATION);
                break;

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        loadTab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /*
   LOCATION PERMISSION
    */
    public void checkLocationPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_message_permission),
                    Constants.REQUEST_FOR_LOCATION_PERMISSIONS, permissions);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == Constants.REQUEST_FOR_LOCATION_PERMISSIONS) {
            if (EasyPermissions.hasPermissions(this, permissions)) {
                Intent intent = new Intent(mBaseAppCompatActivity, LocationUpdateService.class);
                startService(intent);
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == Constants.REQUEST_FOR_LOCATION_PERMISSIONS) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setRationale(getString(R.string.rationale_message_permission))
                        .setTitle(getString(R.string.title_settings_dialog))
                        .setPositiveButton(getString(R.string.setting))
                        .setNegativeButton(getString(R.string.cancel))
                        .setRequestCode(Constants.REQUEST_FOR_LOCATION_PERMISSIONS)
                        .build()
                        .show();
            }
        }
    }

    /*
    Start location runnable and fetch location as set interval and upload in server if user is logged in
     */
    public void startLocationUpdate() {
        Log.d(TAG, "startLocationUpdate() called");
       /* if (mHandler == null) {
            mHandler = new Handler();
            mHandler.post(mRunnableLocationUpdate);
        }
*/
       if(!isMyServiceRunning(LocationUpdateService.class)){
           Intent intent = new Intent(mBaseAppCompatActivity, LocationUpdateService.class);
           startService(intent);
       }

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*
    Stop location runnable when user is logged out
     */
    public void stopLocationUpdate() {
        Log.d(TAG, "stopLocationUpdate() called");
        if (UserPreference.getInstance(mBaseActivity).getTripStatus().equals("0")) {
            Intent intent = new Intent(mBaseAppCompatActivity, LocationUpdateService.class);
            stopService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_settings:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdate();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode != FILECHOOSER_RESULTCODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if(resultCode == Activity.RESULT_OK) {
            String dataString = data.getDataString();
            if (dataString != null) {
                results = new Uri[]{Uri.parse(dataString)};
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;

    }
}
