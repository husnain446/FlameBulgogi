package com.flame.bulgogi.cust.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.flame.bulgogi.cust.R;
import com.flame.bulgogi.cust.utils.DialogUtils;
import com.flame.bulgogi.cust.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    private static final String TAG = LogUtils.makeLogTag(BaseFragment.class);
    public Activity baseActivity;
    private Unbinder unbinder;
    private DialogUtils mDialogUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        unbinder = ButterKnife.bind(this, view);
        baseActivity = getActivity();
        return view;
    }

    protected abstract int getLayoutResource();

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    public void pushFragments(int container, FragmentActivity activity, Fragment fragment, boolean addToBackStack, boolean animate, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (animate) {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }

        if (addToBackStack) {
            ft.replace(container, fragment, tag);
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
        } else {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.replace(container, fragment, tag);
            ft.commit();
        }
    }

    public void pushAddFragments(int container, FragmentActivity activity, Fragment fragment, boolean addToBackStack, boolean animate, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (animate) {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }

        if (addToBackStack) {
            ft.add(container, fragment, tag);
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
        } else {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.add(container, fragment, tag);
            ft.commit();
        }
    }

    public void pushFragments(FragmentActivity activity, Fragment fragment, boolean addToBackStack, boolean animation, String tag) {
        pushFragments(R.id.container, activity, fragment, addToBackStack, animation, tag);
    }

    public void pushAddFragments(FragmentActivity activity, Fragment fragment, boolean addToBackStack, boolean animation, String tag) {
        pushAddFragments(R.id.container, activity, fragment, addToBackStack, animation, tag);
    }

    /*public void pushTabFragments(FragmentActivity activity, Fragment fragment, String tag) {
        pushFragments(R.id.innerContainer, activity, fragment, false, false, tag);
    }*/

    public void showToast(String message) {
        if (getActivity() != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showLongToast(String message) {
        if (getActivity() != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void showProgressDialog(boolean cancelable) {
        if (getContext() == null) {
            LogUtils.LOGE(TAG, "Context can not be null.");
            return;
        }

        if (mDialogUtils == null) {
            mDialogUtils = DialogUtils.getInstance(getContext());
        }
        mDialogUtils.showProgressDialog(cancelable);
    }

    public void showProgressDialog() {
        showProgressDialog(false);
    }

    public void dismissProgressDialog() {
        if (mDialogUtils != null) {
            mDialogUtils.dismissProgressDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    public void hideKeyboard(Context context, IBinder binder) {
        InputMethodManager inputManager =
                (InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     *
     * @param inputLayout : textInput Layout
     * @param error : error message
     * @param type : if 1 then empty string set to setError else error message provided bt user
     */
    public void setErrorWithFocus(TextInputLayout inputLayout, String error,int type) {
        inputLayout.getEditText().requestFocus();
        inputLayout.setErrorEnabled(true);
        if(type==1) {
            inputLayout.setError(" ");
        }else{
            inputLayout.setError(error);
        }
        //showToast(error);
    }
}
