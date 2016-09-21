package com.evin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.evin.R;
import com.evin.presenter.IAmayaPresenter;
import com.evin.util.UIUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amayababy
 * 2016-06-05
 * 下午1:44
 */
public class EditEntranceActivity extends EvinActivity<IAmayaPresenter> {
    @Override
    protected IAmayaPresenter setIAmayaPresenter() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entrance);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.entrance_1, R.id.entrance_2, R.id.entrance_3})
    public void onClick(View v) {
        Intent intent = new Intent(this, AmayaEditsActivity.class);
        int type = 1;
        switch (v.getId()) {
            case R.id.entrance_1:
                type = 1;
                break;
            case R.id.entrance_2:
                type = 2;
                break;
            case R.id.entrance_3:
                type = 3;
                break;
        }
        intent.putExtra("type", type);
        UIUtil.startActivity(this, intent);
    }


}
