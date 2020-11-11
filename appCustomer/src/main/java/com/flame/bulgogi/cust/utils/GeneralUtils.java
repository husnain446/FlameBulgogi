package com.flame.bulgogi.cust.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

/**
 * Created by Pranay on 14/4/17.
 */

public class GeneralUtils {

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public static InputFilter doNotAllowedSmileyAndSpecialCharFilter(final Boolean canAllowSpace) {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (source.toString().matches("[a-zA-Z0-9 ,._]+")) {
                        if (!canAllowSpace && Character.isSpaceChar(source.charAt(i))) {
                            return "";
                        } else {
                            return source;
                        }
                    }
                }
                return "";
            }
        };
        return filter;
    }

    public static boolean isDigit(String text) {
        boolean isDigit = true;
        if (!TextUtils.isEmpty(text)) {
            for (int i = 0; i < text.length(); i++) {
                if (!Character.isDigit(text.charAt(i))) {
                    isDigit = false;
                    break;
                }
            }
        } else {
            isDigit = false;
        }
        return isDigit;
    }

    public static boolean isNumber(String text) {
        if (!TextUtils.isEmpty(text)) {
            Pattern pattern = Pattern.compile("^(([0-9]*)|(([0-9]*)\\.([0-9]*)))$");
            return pattern.matcher(text).matches();
        }
        return true;
    }

}
