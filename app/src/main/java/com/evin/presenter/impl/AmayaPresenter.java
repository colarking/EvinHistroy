package com.evin.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.evin.activity.XApplication;
import com.evin.presenter.IAmayaPresenter;
import com.evin.util.AmayaConstants;
import com.evin.util.UIUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by amayababy on 15/7/1.
 */
public abstract class AmayaPresenter<T extends Activity> implements IAmayaPresenter<T> {

    protected T mActivity;
    private MaterialDialog loadingDialog;

    @Override
    public void onCreate(T activity) {
        mActivity = activity;
        Log.e("amaya", "onCreate()...titlePresenter = null-->this=" + this);
    }

    @Override
    public void onStart(T activity) {
    }

    @Override
    public void onResume(T activity) {
        Log.e("amaya", "onResume()...titlePresenter = null-->this=" + this);
    }

    @Override
    public void onPause(T activity) {
    }

    @Override
    public void onStop(T activity) {
    }

    @Override
    public void onDestroy() {
        Log.e("amaya", "onDestroy()...titlePresenter = null-->this=" + this);
        mActivity = null;
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) loadingDialog.dismiss();
            loadingDialog = null;
        }
        finish();
    }


    protected View findViewById(int id){
        if (mActivity instanceof Activity) {
            return ((Activity) mActivity).findViewById(id);
        }
        return  null;
    }

    @Override
    public boolean isLoading() {
        if (loadingDialog == null) {
            return false;
        } else {
            return loadingDialog.isShowing();
        }
    }

    @Override
    public void showLoadingDialog(int res) {
        if (loadingDialog == null) {
            loadingDialog = UIUtil.showLoadiingDialog(mActivity, res);
        } else {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    protected String getString(int res) {
        if (mActivity == null) {
            return XApplication.getContext().getResources().getString(res);
        } else {
            return mActivity.getResources().getString(res);
        }
    }

//    public boolean checkErrorCode(int code) {
//        if (code == AmayaConstants.CODE_EMPTY_TOKEN) {
//            Intent intent;
//            if (mActivity != null) {
//                intent = new Intent(mActivity, LoginEntranceActivity.class);
//                UIUtil.startActivity(mActivity, intent);
//                mActivity.finish();
//            } else {
//                intent = new Intent(XApplication.getContext(), LoginEntranceActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                UIUtil.startActivity(XApplication.getContext(), intent);
//            }
//            EventBus.getDefault().post(new AmayaEvent.UserQuitEvent());
//            return false;
//        }
//        return true;
//    }

    protected void finish() {
        loadingDialog = null;
        if (mActivity != null) {
            mActivity = null;
        }
    }
}
