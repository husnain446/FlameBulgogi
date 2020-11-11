package com.flame.bulgogi.base;

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


import com.flame.bulgogi.R;

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

    public void launchActivity(Intent intent) {
        launchActivity(intent, false);
    }

    public void launchActivity(Intent intent, boolean finishCurrent) {
        launchActivity(intent, DEFAULT_REQUEST_CODE, finishCurrent);
    }

    public void launchActivity(Intent intent, int requestCode, boolean finishCurrent) {
        if (requestCode != DEFAULT_REQUEST_CODE) {
            startActivityForResult(intent, requestCode);
        } else {
            if (finishCurrent) {
                finish();
            }
            startActivity(intent);
        }
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, false);
    }

    public void addFragment(Fragment fragment, boolean addToBackStack) {
        loadFragment(fragment, FRAGMENT_TRANSACTION_ADD, addToBackStack);
    }

    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        loadFragment(fragment, FRAGMENT_TRANSACTION_REPLACE, addToBackStack);
    }

    private void loadFragment(Fragment fragment, int transactionType) {
        loadFragment(fragment, transactionType, false);
    }

    private void loadFragment(Fragment fragment, int transactionType, boolean addToBackStack) {
        loadFragment(fragment, R.id.container, transactionType, addToBackStack);
    }

    void loadFragment(Fragment fragment, int containerId, int transactionType, boolean addToBackStack) {
        String fragmentName = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentName);
        }
        if (transactionType == FRAGMENT_TRANSACTION_ADD) {
            fragmentTransaction.add(containerId, fragment, fragmentName);
        } else {
            fragmentTransaction.replace(containerId, fragment, fragmentName);
        }
        fragmentTransaction.commit();
    }

    public void showDialogFragment(AppCompatDialogFragment appCompatDialogFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(appCompatDialogFragment, appCompatDialogFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showDialogFragment(Fragment targetFragment, AppCompatDialogFragment appCompatDialogFragment, int requestCode) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        appCompatDialogFragment.setTargetFragment(targetFragment, requestCode);
        fragmentTransaction.add(appCompatDialogFragment, appCompatDialogFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showDialogFragment(BaseDialogFragment targetFragment, AppCompatDialogFragment appCompatDialogFragment, int requestCode) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        appCompatDialogFragment.setTargetFragment(targetFragment, requestCode);
        fragmentTransaction.add(appCompatDialogFragment, appCompatDialogFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnakbar(String resource){
        Snackbar snackbar1 = Snackbar.make(mCoordinatorLayout, resource, Snackbar.LENGTH_SHORT);
        snackbar1.show();
    }

    public void showToast(int resourceId) {
        Toast.makeText(this, resourceId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String resource) {
        Toast.makeText(this, resource, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resourceId, int toastLong) {
        Toast.makeText(this, resourceId, toastLong).show();
    }

    public void showToast(String resource, int toastLong) {
        Toast.makeText(this, resource, toastLong).show();
    }

    public void startApiService(Intent intent) {
        startService(intent);
    }

    public void finishWithResultOk() {
        finishWithResultOk(null);
    }

    protected void finishWithResultOk(Intent intent) {
        if (intent == null)
            setResult(RESULT_OK);
        else
            setResult(RESULT_OK, intent);
        finish();
    }

    protected void finishWithResultCancel() {
        finishWithResultCancel(null);
    }

    protected void finishWithResultCancel(Intent intent) {
        if (intent == null)
            setResult(RESULT_CANCELED);
        else
            setResult(RESULT_CANCELED, intent);
        finish();
    }

    protected Fragment findFragment(Class fragment) {
        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(fragment.getSimpleName());
        if (fragment1 != null && fragment1.isVisible())
            return fragment1;
        else
            return null;
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

    /**
     * @param inputLayout : textInput Layout
     * @param error       : error message
     * @param type        : if 1 then empty string set to setError else error message provided bt user
     */
    public void setErrorWithFocus(TextInputLayout inputLayout, String error, int type) {
        inputLayout.getEditText().requestFocus();
        inputLayout.setErrorEnabled(true);
        if (type == 1) {
            inputLayout.setError(" ");
        } else {
            inputLayout.setError(error);
        }

        //showToast(error);
    }
}
