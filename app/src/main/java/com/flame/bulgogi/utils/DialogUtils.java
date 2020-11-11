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

    private static final String TAG = makeLogTag(DialogUtils.class);
    private static final int GPS_SETTING_DIALOG = 65;
    static ProgressDialog mProgressDialog;
    private static AlertDialog alertDialog;
    private Context context;

    private DialogUtils(Context context) {
        this.context = context;
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     */
    public static void showProgressDialog(Context ctx, String title, String body, boolean isCancellable) {
        showProgressDialog(ctx, title, body, null, isCancellable);
    }

    /**
     * Shows a progress dialog with a spinning animation in it. This method must preferably called
     * from a UI thread.
     *
     * @param ctx           Activity context
     * @param title         Title of the progress dialog
     * @param body          Body/Message to be shown in the progress dialog
     * @param icon          Icon to show in the progress dialog. It can be null.
     * @param isCancellable True if the dialog can be cancelled on back button press, false otherwise
     */
    public static void showProgressDialog(Context ctx, String title, String body, Drawable icon, boolean isCancellable) {

        if (ctx instanceof Activity) {
            if (!((Activity) ctx).isFinishing()) {
                mProgressDialog = ProgressDialog.show(ctx, title, body, true);
                mProgressDialog.setIcon(icon);
                mProgressDialog.setCancelable(isCancellable);
            }
        }
    }

    /**
     * Check if the {@link ProgressDialog} is visible in the UI.
     */
    public static boolean isProgressDialogVisible() {
        return (mProgressDialog != null);
    }

    /**
     * Dismiss the progress dialog if it is visible.
     */
    public static void dismissProgressDialog() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = null;
    }

    /**
     * Shows an alert dialog with the OK button. When the user presses OK button, the dialog
     * dismisses.
     */
    public static void showAlertDialog(Context ctx, String title, String body) {
        showAlertDialog(ctx, title, body, null);
    }

    /**
     * Shows an alert dialog with OK button
     */
    public static void showAlertDialog(Context ctx, String title, String body, DialogInterface.OnClickListener okListener) {

        if (okListener == null) {
            okListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.MyAlertDialogStyle).setMessage(body).setPositiveButton("OK", okListener);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Shows an alert dialog with OK button
     */
    public static void showAlertCustomeDialog(Context ctx, String title, String body) {

       /* alertDialog = new AlertDialog.Builder(ctx).create();

        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_ok_dialog, null);
        alertDialog.setView(view);
        CustomAppCompatButton btnOK = ButterKnife.findById(view, R.id.btnOk);
        CustomTextView tvDialogMessage = ButterKnife.findById(view, R.id.tvDialogMessage);
        CustomTextView tvDialogTitle = ButterKnife.findById(view, R.id.tvDialogTitle);
        tvDialogTitle.setText(title);
        tvDialogMessage.setText(body);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //alertDialog.setMessage(message);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();*/
    }

    /**
     * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param ctx
     * @param message     Message to be shown in the dialog.
     * @param yesListener Yes click handler
     * @param noListener  **
     */
    public static void showConfirmDialog(Context ctx, String message, View.OnClickListener yesListener, View.OnClickListener noListener) {
        showConfirmDialog(ctx, message, yesListener, noListener, "Yes", "No", true);
    }

    public static void showConfirmDialog(Context ctx, String message, String labelPositive, View.OnClickListener yesListener, String labelNegative, View.OnClickListener noListener) {
        showConfirmDialog(ctx, message, yesListener, noListener, labelPositive, labelNegative, true);
    }


    /**
     * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
     * dialog.
     *
     * @param ctx
     * @param message     Message to be shown in the dialog.
     * @param yesListener Yes click handler
     * @param noListener
     * @param yesLabel    Label for yes button
     * @param noLabel     Label for no button
     */
    public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel, boolean isCancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.MyAlertDialogStyle);

        if (yesListener == null) {
            yesListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }


        if (noListener == null) {
            noListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }

        builder.setCancelable(isCancelable);
        builder.setMessage(message).show();
        /*.setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).show();*/
    }

    public static void showConfirmDialog(Context ctx, String message, View.OnClickListener yesListener, View.OnClickListener noListener, String yesLabel, String noLabel, boolean isCancelable) {
       /* alertDialog = new AlertDialog.Builder(ctx).create();
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_ok_cancel_dialog, null);
        alertDialog.setView(view);
        CustomAppCompatButton btnCancel = ButterKnife.findById(view, R.id.btnCancel);
        btnCancel.setText(noLabel);
        CustomAppCompatButton btnOK = ButterKnife.findById(view, R.id.btnOk);
        btnOK.setText(yesLabel);
        CustomTextView tvDialogMessage = ButterKnife.findById(view, R.id.tvDialogMessage);
        tvDialogMessage.setText(message);
        btnCancel.setOnClickListener(noListener);
        btnOK.setOnClickListener(yesListener);
        alertDialog.setCancelable(isCancelable);
        //alertDialog.setMessage(message);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();*/
    }

    public static void dismissConfirmDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /**
     * Creates a confirmation dialog that show a pop-up with button labeled as parameters labels.
     *
     * @param ctx                 {@link Activity} {@link Context}
     * @param message             Message to be shown in the dialog.
     * @param dialogClickListener For e.g.
     *                            <p/>
     * @param positiveBtnLabel    For e.g. "Yes"
     * @param negativeBtnLabel    For e.g. "No"
     */
    public static void showDialog(Context ctx, String message, String positiveBtnLabel, String negativeBtnLabel, DialogInterface.OnClickListener dialogClickListener) {

        if (dialogClickListener == null) {
            throw new NullPointerException("Action listener cannot be null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(message).setPositiveButton(positiveBtnLabel, dialogClickListener).setNegativeButton(negativeBtnLabel, dialogClickListener).show();
    }

    public static boolean isGpsEnable(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static DialogUtils getInstance(Context context) {
        return new DialogUtils(context);
    }

    public void showProgressDialog(boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
        }
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.layout_progress_dialog);
    }


}
