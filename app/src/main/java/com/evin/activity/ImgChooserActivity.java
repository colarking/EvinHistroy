package com.evin.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.evin.R;
import com.evin.adapter.AmayaAdapter;
import com.evin.adapter.AmayaImgsAdapter;
import com.evin.bean.ImageFloder;
import com.evin.presenter.IImageChooserPresenter;
import com.evin.presenter.impl.ImageChooserPresenter;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.ToastUtil;
import com.evin.util.UIUtil;
import com.evin.view.ListImageDirPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ImgChooserActivity extends EvinSwipeActivity<IImageChooserPresenter> implements ListImageDirPopupWindow.OnImageDirSelected, OnClickListener, AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    private static final String TAG = ImgChooserActivity.class.getSimpleName();
    private static final int AMAYA_ACTION_REQUEST_FORM_CAMERA = 925;

    private GridView mGirdView;


    private RelativeLayout mBottomLy;

    private TextView mChooseDir;
    private TextView mImageCount;


    private ListImageDirPopupWindow mListImageDirPopupWindow;
    private String tempPath;
    private int max;
    private int hashKey;

    /**
     * 初始化展示文件夹的popupWindw
     *
     * @param imageFloders
     */
    private void initListDirPopupWindw(ArrayList<ImageFloder> imageFloders) {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (UIUtil.amayaHeight * 0.7),
                imageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));
        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    protected IImageChooserPresenter setIAmayaPresenter() {
        return new ImageChooserPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_chooser);
        setTitle(R.string.title_choose_img);
        initView();
        showBackIcon(true);
        max = getIntent().getIntExtra("max", 1);
        hashKey = getIntent().getIntExtra("hash", 0);
        amayaPresenter.setMaxCount(max);
        EventBus.getDefault().register(this);

        boolean grantPermission = UIUtil.grantPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode());
        if(grantPermission){
            amayaPresenter.loadImages();
        }
        mGirdView.setOnItemClickListener(this);
    }









    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mListImageDirPopupWindow = null;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ImageChooserPresenter presenter) {
        amayaPresenter.hideLoadingDialog();
        AmayaAdapter adapter = presenter.getAdapter();
        mGirdView.setAdapter(adapter);
        // 为View绑定数据
        mImageCount.setText(getString(R.string.images_count, adapter.getCount()));
        // 初始化展示文件夹的popupWindw
        initListDirPopupWindw(amayaPresenter.getmImageFloders());
        initEvent();
        selected(null);
    }


    /**
     * 初始化View
     */
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.PopupAnimation);
                mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {
        if (floder == null || floder.getDir() == null) {
            amayaPresenter.notifyDataSetChanged(null);
            mChooseDir.setText("所有图片");
        } else {
            amayaPresenter.notifyDataSetChanged(floder.getDir());
            mChooseDir.setText(floder.getName());

        }
        mImageCount.setText(getString(R.string.images_count, amayaPresenter.getAdapter().getCount() - 1));
        mListImageDirPopupWindow.dismiss();

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            if (amayaPresenter.getAdapter().getSelectedImage().size() < max) {
                if(UIUtil.grantPermission(this, Manifest.permission.CAMERA,Manifest.permission.CAMERA.hashCode())){
                    getPhotoFromCamera();
                }
            } else {
                ToastUtil.show(R.string.images_count_max, true);
            }
        }
    }

    private void getPhotoFromCamera() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
        // 这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
        String s = sd.format(new Date(System.currentTimeMillis()));
        tempPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/IMG_"
                + s
                + ".jpg";
        File f = new File(tempPath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, AMAYA_ACTION_REQUEST_FORM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AMAYA_ACTION_REQUEST_FORM_CAMERA == requestCode) {
            File file = new File(tempPath);
            if (file.exists() && file.length() > 0) {
                amayaPresenter.getAdapter().getSelectedImage().add(tempPath);
                amayaPresenter.getAdapter().add(1, tempPath);
                amayaPresenter.insertNewPicture(tempPath);
                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(UIUtil.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if(requestCode == Manifest.permission.CAMERA.hashCode()){
                getPhotoFromCamera();
            }else if(requestCode == Manifest.permission.WRITE_EXTERNAL_STORAGE.hashCode()){
                amayaPresenter.loadImages();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem add = menu.add(R.string.ok);
        add.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setOnMenuItemClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        AmayaImgsAdapter adapter = amayaPresenter.getAdapter();

        String dirPath = adapter.getDirPath();
        List<String> imgs = adapter.getSelectedImage();
        if (imgs != null && imgs.size() > 0) {
            ArrayList<String> newPaths = new ArrayList<>(imgs.size());
            for (int i = 0; i < imgs.size(); i++) {
                newPaths.add(imgs.get(i));
            }
            EventBus.getDefault().post(new AmayaEvent.ImageChooseEvent(newPaths,hashKey));
            finish();
        }
        return false;
    }
}
