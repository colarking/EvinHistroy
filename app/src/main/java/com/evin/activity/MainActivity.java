package com.evin.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.evin.R;
import com.evin.activity.edit.EditImageActivity;
import com.evin.bean.EvinImage;
import com.evin.presenter.IAmayaPresenter;
import com.evin.presenter.impl.ImageChooserPresenter;
import com.evin.util.UIUtil;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends EvinActivity implements MenuItem.OnMenuItemClickListener {
    @Override
    protected IAmayaPresenter setIAmayaPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean permission = UIUtil.grantPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode());
//        boolean permission2 = UIUtil.grantPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION.hashCode());
//        boolean permission3 = UIUtil.grantPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION.hashCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem add = menu.add(R.string.add);
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();

        List<EvinImage> evinImages = XApplication.getDaoSession().getEvinImageDao().loadAll();
        if(evinImages != null && evinImages.size() > 0){
            for(int i=0;i<evinImages.size();i++){

                Log.e("amaya","onResume()...i="+i+"--"+evinImages.get(i).toString());
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        Intent intent = new Intent(this,ImgChooserActivity.class);
//        Intent intent = new Intent(this,GeocoderActivity.class);
//        Intent intent = new Intent(this,PoiAroundSearchActivity.class);
        Intent intent = new Intent(this,AmayaEditsActivity.class);
        intent.putExtra("type",1);
        UIUtil.startActivity(this,intent);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UIUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
