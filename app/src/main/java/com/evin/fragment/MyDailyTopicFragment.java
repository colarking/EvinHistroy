package com.evin.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.evin.R;
import com.evin.activity.EvinActivity;
import com.evin.activity.ImgChooserActivity;
import com.evin.activity.XApplication;
import com.evin.adapter.AmayaImgsAdapter;
import com.evin.bean.EvinImage;
import com.evin.bean.EvinImageDao;
import com.evin.bean.EvinTime;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.AmayaGPSUtil;
import com.evin.util.AmayaSPUtil;
import com.evin.util.AmayaToast;
import com.evin.util.ToastUtil;
import com.evin.util.UIUtil;
import com.evin.view.AmayaTopicItemView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyDailyTopicFragment extends BaseFragment implements  OnClickListener, OnTouchListener, OnItemClickListener, MenuItem.OnMenuItemClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private LinearLayout picsList;
    private String tempPath;//,topicId,topicName;
    private SimpleDateFormat sd;
    private int currentPos = -1;
    private boolean fromCamera;
    @SuppressLint("HandlerLeak")
    private EvinActivity mActivity;
    private String topicId;
    private String topicName;
    private View addBtn;
    private Uri imageUri;
    private Integer updatePos;
    private Uri tempUri;
    private int MAX_COUNT = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (EvinActivity) getActivity();
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (EvinActivity) getActivity();
        View view = inflater.inflate(R.layout.amaya_daily_topic_old, null);
//        gpsView = (AmayaGPSView) view.findViewById(R.id.daily_topic_gps);
//        gpsView.update();
        picsList = (LinearLayout) view.findViewById(R.id.daily_topic_pics);
        sd = new SimpleDateFormat("yyyyMMdd_HHmmsss");
        addBtn = view.findViewById(R.id.amaya_topic_add_pic);
        addBtn.setOnClickListener(this);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) amayaScroll.getLaayoutParams();
//        lp.height = AmayaUIUtil.dip2px(500);
//        amayaScroll.setLayoutParams(lp);
//        View bottomLayout = view.findViewById(R.id.daily_topic_button_parent);
//        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) bottomLayout.getLayoutParams();
//        llp.height = AmayaUIUtil.getCommonHeight(48);
//        startSearchForRJPs();
        getAll();
        setHasOptionsMenu(true);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (EvinActivity) getActivity();
        AmayaGPSUtil.updateGPS();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        MenuItem add = menu.add(R.string.ok);
        add.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setOnMenuItemClickListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        doWork();
        return false;
    }

    private void doWork() {
        String desc = null;
        if (picsList.getChildCount() == 0 && TextUtils.isEmpty(desc)) {
            AmayaToast.show(R.string.pic_empty_tip_topic, true);
            return;
        }
        ArrayList<EvinImage> alps = new ArrayList<EvinImage>(8);
        long start = System.currentTimeMillis();
        for (int i = 0; i < picsList.getChildCount(); i++) {
            AmayaTopicItemView aiv = (AmayaTopicItemView) picsList.getChildAt(i);
            EvinImage item = aiv.getBean();
            JSONObject jo = new JSONObject();
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int rotation = 0;
            if (!TextUtils.isEmpty(item.getPath())) {
                BitmapFactory.decodeFile(item.getPath(), o);
                rotation = defineExifOrientation(item.getPath());
            }

            if (rotation == 90 || rotation == 180) {
                item.setHeight(o.outHeight);
                item.setWidth(o.outWidth);
            } else {
                item.setWidth(o.outWidth);
                item.setHeight(o.outHeight);
            }
            alps.add(item);
            XApplication.getDaoSession().getEvinImageDao().insert(item);
        }
        clearAll();
        hideAddBtn(false);

//        HashMap<String, String> map = new HashMap<>();
//        MobclickAgent.onEvent(getContext(), "DailyTopic", map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amaya_topic_add_pic:
                if (picsList.getChildCount() < 8) {
                    jumpToChooser();
                } else {
                    AmayaToast.show(getString(R.string.pic_choose_full_tip), true);
                }
                break;
            default:
                break;
        }
    }

    public void showDialog(){
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this);
        cdp.show(getActivity().getSupportFragmentManager(), "FRAG_TAG_DATE_PICKER");
    }

    private void jumpToChooser() {
        Intent intent = new Intent(mActivity, ImgChooserActivity.class);
        intent.putExtra("max", MAX_COUNT - picsList.getChildCount());
        intent.putExtra("hash",hashCode());
        UIUtil.startActivity(mActivity, intent);
    }

    protected int defineExifOrientation(String imageUri) {
        int rotation = 0;
        boolean flip = false;
        try {
            ExifInterface exif = new ExifInterface(imageUri);
//                fileSize = exif.getAttributeInt(ExifInterfaceExtended.TAG_JPEG_FILESIZE, 0);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    flip = true;
                case ExifInterface.ORIENTATION_NORMAL:
                    rotation = 0;
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    flip = true;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    flip = true;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    flip = true;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        } catch (IOException e) {
        }
        return rotation;
    }

    private void clearAll() {
        currentPos = -1;
        picsList.removeAllViews();
        int count = picsList.getChildCount();
//        if(count>1){
//
//            for(int i=1;i<count;i++){
//                AmayaTopicItemView at = (AmayaTopicItemView) picsList.getChildAt(i);
//                AmayaLog.e("amaya","clearAll()...i="+i+"--atv="+at);
//                if(at != null){
//                    at.clear();
//                    picsList.removeView(at);
//                }
//            }
//
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return true;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        saveAll();
        mActivity = null;
        EventBus.getDefault().unregister(this);
        sd = null;
        tempPath = null;
    }




    private void saveAll() {
        int childCount = picsList.getChildCount();
        try {
            StringBuilder sb = new StringBuilder();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    AmayaTopicItemView ativ = (AmayaTopicItemView) picsList.getChildAt(i);
                    EvinImage bean = ativ.getBean();
                    long insert = XApplication.getDaoSession().getEvinImageDao().insertOrReplace(bean);
                    sb.append(insert);
                    if(i != childCount -1){
                        sb.append(",");
                    }
                }
                AmayaSPUtil.save("amaya_image_ids",sb.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAll() {
        String string = AmayaSPUtil.getString("amaya_image_ids", null);
        if (!TextUtils.isEmpty(string)) {
            try {
                String[] split = string.split(",");
                if(split!=null && split.length > 0){
                    for(int i=0;i<split.length;i++){
                        EvinImage image = XApplication.getDaoSession().getEvinImageDao().load(Long.parseLong(split[i]));
                        if(image.getTime() == null){
                            image.setTime(new EvinTime());
                        }
                        updateAlps(image);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void updateAlps(EvinImage alp) {
        if (mActivity == null) {
            mActivity = (EvinActivity) getActivity();
        }
        AmayaTopicItemView aiv = new AmayaTopicItemView(mActivity);
        aiv.setIndex(picsList.getChildCount());
        aiv.setBean(alp);
        ImageView img = aiv.getImg();
        if (img != null) {
            img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer p = (Integer) view.getTag();
                    if (p == null) return;
                    updatePos = p;
                    AmayaTopicItemView aiv = (AmayaTopicItemView) picsList.getChildAt(updatePos);
                    if (aiv == null) return;
                }
            });
        }
        picsList.addView(aiv);
        if (picsList.getChildCount() == 8) {
            hideAddBtn(true);
        }
    }

    private void hideAddBtn(boolean hide) {
        addBtn.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    private void getPhotoFromPictures() {
//        if (Build.VERSION.SDK_INT < 19) {
////            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////            // The MIME data type filter
////            intent.setType("image/*");
////            // Only return URIs that can be opened with ContentResolver
////            startActivityForResult(intent, AMAYA_ACTION_REQUEST_FORM_PICTURES);
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, AmayaConstants.AMAYA_ACTION_REQUEST_FORM_PICTURES);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/jpeg");
//            startActivityForResult(intent, AmayaConstants.AMAYA_ACTION_REQUEST_FORM_PICTURES_KITKAT);
//        }


        Intent intent = new Intent(getActivity(), ImgChooserActivity.class);
        intent.putExtra("max", 8 - picsList.getChildCount());
        startActivity(intent);
    }

    public void onEventMainThread(ArrayList<String> datas) {
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                EvinImage alp = initAlp(datas.get(i));
                updateAlps(alp);
            }
        }
    }


    public void onPositiveButtonClicked(Dialog amayaDialog, int requestCode) {
        if (amayaDialog != null) amayaDialog.dismiss();
        switch (requestCode) {
            case 3:

                //删除选中图片
                AmayaTopicItemView aiv = (AmayaTopicItemView) picsList.getChildAt(updatePos);
                if (aiv == null) return;
                picsList.removeViewAt(updatePos);
                for (int i = 0; i < picsList.getChildCount(); i++) {
                    aiv = (AmayaTopicItemView) picsList.getChildAt(i);
                    if (aiv != null) aiv.setIndex(i);
                }
                if (addBtn.getVisibility() == View.GONE) hideAddBtn(false);
                //美化选中图片
//                AmayaTopicItemView aiv = (AmayaTopicItemView) picsList.getChildAt(updatePos);
//                currentPos = aiv.getIndex();
//                if (aiv == null) return;
//                String featherPath = aiv.getFeatherPath();
//                fromCamera = false;
//                startFeather(Uri.parse(featherPath));

                break;
            default:
                break;
        }
    }


    @NonNull
    private EvinImage initAlp(String path) {
        EvinImage alp = new EvinImage();
        alp.setPath(path);
        ExifInterface eif = null;
        try {
            eif = new ExifInterface(alp.getPath());
            float[] gps = new float[2];
            boolean geted = eif.getLatLong(gps);
            if (geted && gps[0] != 0 && gps[1] != 0) {
                alp.setLatitude(gps[0]);
                alp.setLongitude(gps[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long insert = XApplication.getDaoSession().getEvinImageDao().insert(alp);
        EvinTime t = new EvinTime();
        alp.setTime(t);
        XApplication.getDaoSession().getEvinTimeDao().insert(t);
        return alp;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Subscribe
    public void onEventMainThread(AmayaEvent.ImageChooseEvent event) {
        if (event.requestHash != hashCode()) return;
        for (int i = 0; i < event.pics.size(); i++) {
            String path = event.pics.get(i);
            EvinImage alp = initAlp(path);
            updateAlps(alp);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog,int ADorBC, int year, int monthOfYear, int dayOfMonth) {


    }


}
