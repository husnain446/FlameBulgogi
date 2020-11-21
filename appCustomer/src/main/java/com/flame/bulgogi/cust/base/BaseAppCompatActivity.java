package com.flame.bulgogi.cust.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flame.bulgogi.cust.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pranay on 14/4/17.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    public Activity mBaseActivity;
    private int FRAGMENT_TRANSACTION_ADD = 200;
    private int FRAGMENT_TRANSACTION_REPLACE = 300;
    public int DEFAULT_REQUEST_CODE = 100;
    protected BaseAppCompatActivity mBaseAppCompatActivity;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.rl_progress_dialog)
    RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mBaseActivity = this;
        ButterKnife.bind(this);

        mBaseAppCompatActivity = this;
    }

    protected abstract int getLayoutResource();


    public void showToast(String resource) {
        Toast.makeText(this, resource, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mRelativeLayout != null) {
            mRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressDialog() {
        if (mRelativeLayout != null) {
            mRelativeLayout.setVisibility(View.GONE);
        }
    }
}
