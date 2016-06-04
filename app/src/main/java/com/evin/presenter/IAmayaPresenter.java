package com.evin.presenter;

import android.app.Activity;

/**
 * Created by amayababy on 15/7/1.
 */
public interface IAmayaPresenter<T extends Activity> {
    void onCreate(T activity);

    void onStart(T activity);

    void onResume(T activity);

    void onPause(T activity);

    void onStop(T activity);
    void onDestroy();

    void showLoadingDialog(int res);

    void hideLoadingDialog();

    boolean isLoading();

}
