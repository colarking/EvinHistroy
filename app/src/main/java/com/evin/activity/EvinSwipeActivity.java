package com.evin.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.evin.R;
import com.evin.presenter.IAmayaPresenter;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by amayababy
 * 2016-05-04
 * 上午3:53
 */
public abstract class EvinSwipeActivity<T extends IAmayaPresenter> extends AppCompatActivity  implements SwipeBackActivityBase {

    protected T amayaPresenter;
    private SwipeBackActivityHelper mHelper;

    protected abstract T setIAmayaPresenter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        amayaPresenter = setIAmayaPresenter();
        updateColor(true);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolBar();
        if (amayaPresenter != null) amayaPresenter.onCreate(this);
    }


    @Override
    public void setContentView(View view) {
        setContentView(view, true);
        initToolBar();
        if (amayaPresenter != null) amayaPresenter.onCreate(this);
    }

    public void initToolBar(){
        Toolbar bar = (Toolbar) findViewById(R.id.evin_tool_bar);
        if(bar != null){
            bar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            setSupportActionBar(bar);
        }
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

    public void showBackIcon(boolean show){
        if(show){
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!= null){
                actionBar.setDisplayHomeAsUpEnabled(show);
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
}
