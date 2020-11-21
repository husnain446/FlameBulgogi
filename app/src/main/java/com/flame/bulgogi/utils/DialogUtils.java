package com.flame.bulgogi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.View;


import com.flame.bulgogi.R;

import static com.flame.bulgogi.utils.LogUtils.makeLogTag;

/**
 * Created by LN on 19/7/16.
 */

public class DialogUtils {

    private Context context;

    private DialogUtils(Context context) {
        this.context = context;
    }


    public static DialogUtils getInstance(Context context) {
        return new DialogUtils(context);
    }


}
