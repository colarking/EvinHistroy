package com.evin.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.evin.R;
import com.evin.presenter.IAmayaPresenter;
import com.evin.util.AmayaConstants;
import com.evin.util.ToastUtil;
import com.evin.util.UIUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by amayababy
 * 2016-04-30
 * 下午6:39
 */
public abstract class EvinActivity<T extends IAmayaPresenter>extends AppCompatActivity{
    private MaterialDialog loadingDialog;
    protected T amayaPresenter;

    protected abstract T setIAmayaPresenter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amayaPresenter = setIAmayaPresenter();
        updateColor(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (amayaPresenter != null) amayaPresenter.onCreate(this);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, true);
    }


    public void setContentView(View view, boolean paddingTop) {
        super.setContentView(view);
        if(amayaPresenter != null) amayaPresenter.onCreate(this);
    }
    public T getAmayaPresenter() {
        return amayaPresenter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(amayaPresenter != null) amayaPresenter.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(amayaPresenter != null) amayaPresenter.onDestroy();
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(amayaPresenter != null) amayaPresenter.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(amayaPresenter != null) amayaPresenter.onStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(amayaPresenter != null) amayaPresenter.onResume(this);
        MobclickAgent.onResume(this);
    }

    protected void showLoadingDialog(int res) {
        if (loadingDialog == null) {
            loadingDialog = UIUtil.showLoadiingDialog(this, res);
        } else {
            loadingDialog.setContent(res);
            loadingDialog.show();
        }
    }

    public boolean isLoading() {
        if (loadingDialog == null) {
            return false;
        } else {
            return loadingDialog.isShowing();
        }
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    public void updateColor(boolean fitSystem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (fitSystem) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            } else {
                getWindow().setStatusBarColor(getResources().getColor(R.color.black));
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //对用户按home icon的处理，本例只需关闭activity，就可返回上一activity，即主activity。
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showBackIcon(boolean show){
        if(show){
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!= null){
                actionBar.setDisplayHomeAsUpEnabled(show);
            }
        }
    }
}
