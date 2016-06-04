package com.evin.activity.edit;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.evin.R;
import com.evin.activity.EvinActivity;
import com.evin.presenter.IAmayaPresenter;
import com.evin.util.UIUtil;

/**
 * Created by amayababy
 * 2016-05-04
 * 上午3:53
 */
public class EditImageActivity extends EvinActivity{

    @Override
    protected IAmayaPresenter setIAmayaPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
    }
}
