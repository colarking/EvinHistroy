package com.evin.activity;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.evin.bean.DaoMaster;
import com.evin.bean.DaoSession;
import com.evin.bean.EvinPosition;
import com.evin.theme.EvinTheme;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaGPSUtil;
import com.evin.util.UIUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * Created by amayababy
 * 2016-05-04
 * 上午4:01
 */
public class XApplication extends Application {

    private static Context context;
    private static ImageLoader imageLoader;
    public static EvinPosition evin;
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initImageLoader();
        initCacheDir();
        DisplayMetrics dp = getResources().getDisplayMetrics();
        UIUtil.initAmayaParams(dp.widthPixels, dp.heightPixels);
        UIUtil.initSystemParam(dp.density, dp.scaledDensity);
        evin = new EvinPosition();
        EvinTheme.init();
        AmayaGPSUtil.initGPS();
        initDB();
    }

    private void initDB() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }

    private void initCacheDir() {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File dir = getExternalCacheDir();
            if (dir == null) {
                dir = Environment.getExternalStorageDirectory();
            }
            String path = dir.getAbsolutePath();
            if(new File(path).exists()){
                AmayaConstants.AMAYA_DIR_CACHE = path;
            }else{
                AmayaConstants.AMAYA_DIR_CACHE = getCacheDir().getAbsolutePath();
            }
        }
    }

    public static Context getContext() {
        return context;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions dio = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.defaultDisplayImageOptions(dio);
        // Initialize ImageLoader with configuration.
        imageLoader.init(config.build());
    }
}
