package com.flame.bulgogi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Pranay on 24/4/17.
 */


public class CustomTextInputLayout extends android.support.design.widget.TextInputLayout {


    public CustomTextInputLayout(Context context) {
        this(context, null);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if (enabled) {
            return;
        }
        if (getChildCount() > 1) {
            View view = getChildAt(1);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }
}