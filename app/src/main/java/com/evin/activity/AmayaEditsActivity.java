package com.evin.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.evin.R;
import com.evin.fragment.MyDailyTopicFragment;
import com.evin.presenter.IAmayaPresenter;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.UIUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by colarking on 14-10-7.
 * 15:38
 */
public class AmayaEditsActivity extends EvinActivity{


    @Override
    protected IAmayaPresenter setIAmayaPresenter() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.amaya_edit);
        setContentView(frameLayout);
        Intent in = getIntent();
        if (in == null) {
            finish();
            return;
        }
        int type = in.getIntExtra("type", -1);
        if (type != -1) {
            switchToPage(type);
        } else {
            finish();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void switchToPage(int type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag = null;
        switch (type) {
            case 1:
                frag = Fragment.instantiate(this, MyDailyTopicFragment.class.getName());
                ft.add(R.id.amaya_edit, frag);
                break;
            case 2:
//                frag = Fragment.instantiate(this, MyDailyBillFragment.class.getName());
                ft.add(R.id.amaya_edit, frag);
                break;
            case 3:
//                frag = Fragment.instantiate(this, AmayaCreateNoteFragment.class.getName());
                ft.add(R.id.amaya_edit, frag);
                break;
        }
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.show(frag).commitAllowingStateLoss();
    }

    public void showCommonListFragment(boolean clear, boolean push, int fragmentIndex, Bundle bundle) {
        switch (fragmentIndex) {
//            case AmayaConstants.AMAYA_FRAGMENT_PARENT_TOPIC:
//                Intent in = new Intent(this, AmayaTopicJoinActivity.class);
//                startActivity(in);
//                break;
        }
    }

    public void hideIMEKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            hideIMEKeyboard(currentFocus.getWindowToken());
        }
    }

    public void hideIMEKeyboard(IBinder token) {
        if (token != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);//  (WidgetSearchActivity是当前的Activity)
//            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (UIUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (requestCode == Manifest.permission.READ_PHONE_STATE.hashCode()) {
                EventBus.getDefault().post(new AmayaEvent.RequestPermissionEvent(requestCode));
            } else if (requestCode == Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode()) {
                EventBus.getDefault().post(new AmayaEvent.RequestPermissionEvent(requestCode));

            }
        }
    }
}
