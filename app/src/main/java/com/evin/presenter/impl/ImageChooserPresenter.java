package com.evin.presenter.impl;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.evin.R;
import com.evin.adapter.AmayaImgsAdapter;
import com.evin.bean.ImageFloder;
import com.evin.presenter.IImageChooserPresenter;
import com.evin.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by amayababy
 * 2015-07-16
 * 下午6:13
 */
public class ImageChooserPresenter extends AmayaPresenter implements IImageChooserPresenter {


    private static final int MSG_LOAD_FINISHED = 0;
    private static final String TAG = ImageChooserPresenter.class.getSimpleName();
    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    ArrayList<String> allFiles = new ArrayList<>();
    private AmayaImgsAdapter imgAdapter;
    private MaterialDialog mProgressDialog;
    private int totalCount;         //文件总数
    ;     //文件夹集合
    private ArrayList<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    private int mPicsSize;      //记录最大文件夹的文件个数
    private File mImgDir;       //记录最大文件夹路径
    private ImageFloder firstImgFolder;
    private ImageFloder allFolder;

    @Override
    public void onCreate(Activity activity) {
        super.onCreate(activity);
        imgAdapter = new AmayaImgsAdapter(activity);
        imgAdapter.add("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void loadImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtil.show(R.string.sdcard_unmounted, true);
            return;
        }

        allFolder = new ImageFloder();
        allFolder.setDir(null);
        allFolder.setFirstImagePath(null);
        allFolder.setName("全部图片");
        mImageFloders.add(allFolder);
        // 显示进度条
        showLoadingDialog(R.string.loading_load_pics);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 临时的辅助类，用于防止同一个文件夹的多次扫描
                 */
                HashSet<String> mDirPaths = new HashSet<String>();
                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mActivity.getContentResolver();

//                if(Build.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
//                    mContentResolver.takePersistableUriPermission(mImageUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpg", "image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_ADDED + " desc");
                allFolder.setCount(mCursor.getCount());
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    allFiles.add(path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    String[] list = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    });
                    if (list == null || list.length == 0) continue;
                    int picSize = list.length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);

                    mImageFloders.add(imageFloder);

//                    if (picSize > mPicsSize) {
//                        mPicsSize = picSize;
//                        mImgDir = parentFile;
//                        firstImgFolder = imageFloder;
//                    }
//                    AmayaLog.e(TAG, "loadImages()...mImgDir=" + mImgDir);
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                EventBus.getDefault().post(ImageChooserPresenter.this);

            }
        }).start();

    }


    @Override
    public AmayaImgsAdapter getAdapter() {
        return imgAdapter;
    }

    @Override
    public void setMaxCount(int maxCount) {
        imgAdapter.setMaxCount(maxCount);
    }

    @Override
    public void notifyDataSetChanged(String folder) {
        if (TextUtils.isEmpty(folder)) {
            imgAdapter.addAll(allFiles, true);
            imgAdapter.add(0, "");
        } else {
            imgAdapter.clearSelectedList();
            mImgDir = new File(folder);

            final ArrayList<String> mImgs = new ArrayList<>();
            for (int i = 0; i < allFiles.size(); i++) {
                if (allFiles.get(i).startsWith(folder)) {
                    mImgs.add(allFiles.get(i));
                }
            }
            mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png")
                            || filename.endsWith(".jpeg")) {
                        if (mImgs.contains(filename)) {
                            mImgs.add(dir.getAbsolutePath() + "/" + filename);
                            return false;
                        } else {
                            return true;
                        }
                    }
                    return false;
                }
            });
            /**
             * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
             */
            imgAdapter.setFileDir(folder);
            mImgs.add(0, "");
            imgAdapter.addAll(mImgs, true);
        }

    }

    @Override
    public ImageFloder getFirstFolder() {
        return firstImgFolder;
    }

    public ArrayList<ImageFloder> getmImageFloders() {
        return mImageFloders;
    }

    @Override
    public void insertNewPicture(String path) {
        allFiles.add(0, path);
    }
}
