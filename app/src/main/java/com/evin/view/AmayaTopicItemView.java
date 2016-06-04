package com.evin.view;

import android.content.Context;
import android.graphics.Color;
import android.media.ExifInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.TimeUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.evin.R;
import com.evin.activity.XApplication;
import com.evin.bean.AmayaPoi;
import com.evin.bean.EvinImage;
import com.evin.bean.EvinTime;
import com.evin.theme.EvinTheme;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.AmayaGPSUtil;
import com.evin.util.NetUtil;
import com.evin.util.TimeUtil;
import com.evin.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
/**
 * Created with IntelliJ IDEA.
 * User: Smith
 * Date: 14-3-13
 * Time: 上午9:20
 * To change this template use File | Settings | File Templates.
 */
public class AmayaTopicItemView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener, TextWatcher, CalendarDatePickerDialogFragment.OnDateSetListener {
    private static int width = UIUtil.amayaWidth / 3;
    private static int height = UIUtil.amayaWidth / 2;
    private static HashMap<String, AmayaPoi> amayaPois;
    private ImageView img;
    private AmayaGPSView gpsView;
    private EditText descView;
//    private int index;
    private EvinImage bean;
    private int index;
    private TextView yearView;
    private Calendar calendar = Calendar.getInstance();
    public AmayaTopicItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        yearView  = new TextView(context);
        yearView.setTextColor(Color.BLACK);
        yearView.setGravity(Gravity.CENTER);
        yearView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        yearView.setBackgroundResource(R.drawable.trans_gray_bg_selector);
        yearView.setText(R.string.select_year);
        int dp10 = UIUtil.dip2px(10);
        yearView.setPadding(0,dp10,0,dp10);
        addView(yearView);
        yearView.setId(R.id.id_text_year_show);
        yearView.setOnClickListener(this);

        View item = LayoutInflater.from(context).inflate(R.layout.edit_image, null);
        img = (ImageView) item.findViewById(R.id.amaya_tpitem_img);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
        lp.width = UIUtil.amayaWidth/3;
        gpsView = (AmayaGPSView) item.findViewById(R.id.amaya_tpitem_gps);
        gpsView.setTextSize(16);
        descView = (EditText) item.findViewById(R.id.amaya_tpitem_desc);
        descView.setOnLongClickListener(this);
        descView.addTextChangedListener(this);
        item.findViewById(R.id.amaya_tpitem_gps).setBackgroundResource(EvinTheme.instance().getEditTextBg());
        descView.setBackgroundResource(EvinTheme.instance().getEditTextBg());
        if (amayaPois == null)
            amayaPois = new HashMap<String, AmayaPoi>();
        addView(item);
    }

    private void updateView() {
        setImageView(bean.getPath());
        if (TextUtils.isEmpty(bean.getAddress())) {
            updateGPSDatas();
        } else {
            gpsView.updateTextView(bean.getLatitude(), bean.getLongitude(), bean.getAddress());
        }
        if (!TextUtils.isEmpty(bean.getInfo())) {
            descView.setText(bean.getInfo());
        }
    }

    private void updateGPSDatas() {
        if (NetUtil.isNetworkAvailable() && !TextUtils.isEmpty(bean.getPath())) {
            try {
                ExifInterface eif = new ExifInterface(bean.getPath());
                float[] gps = new float[2];
                boolean geted = eif.getLatLong(gps);
                bean.setLatitude(gps[0]);
                bean.setLongitude(gps[1]);
                if (geted && gps[0] != 0 && gps[1] != 0) {
                    gpsView.setLatitude(gps[0]);
                    gpsView.setLongitude(gps[1]);
                    gpsView.showLoading();
                    EventBus.getDefault().register(this);
                    AmayaGPSUtil.requestAddress(gps[0], gps[1], hashCode());
                } else {
                    showGPSLoading(false);
                    showHideBtn();
//                    hideBtn.setOnClickListener(null);
                    gpsView.setTextHint(R.string.amaya_gps_hide_false);
                    gpsView.update();
//                    gpsView.setEnabled(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            gpsView.hideLoading();
        }
    }

    private void showGPSLoading(boolean show) {
        if (show) {
            gpsView.showLoading();
        } else {
            gpsView.hideLoading();
        }
        gpsView.setTextHint(R.string.amaya_gps_finding);
    }

    private void showHideBtn() {
//        hideBtn.setImageResource(hideGps ? R.drawable.amaya_gps_status_off :R.drawable.amaya_gps_status_on );
    }

    private void setImageView(String path) {

        XApplication.getImageLoader().displayImage(path, img);
    }


    public EditText getDescView() {
        return descView;
    }

    public ImageView getImg() {
        return img;
    }

    public void clear() {
        if (amayaPois != null) {
            amayaPois.clear();
            amayaPois = null;
        }
        img = null;
        gpsView = null;
        descView = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_text_year_show:
                CalendarDatePickerDialogFragment datePicker = new CalendarDatePickerDialogFragment().setOnDateSetListener(this).setPreselectedDate(calendar.get(Calendar.ERA),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "FRAG_TAG_DATE_PICKER");
                break;
//            case R.id.amaya_tpitem_hidegps:
//                if (gpsView.isEnabled()) gpsAddress = gpsView.getText().toString();
//                gpsView.setEnabled(hideGps);
//                hideGps = !hideGps;
//                showHideBtn();
//                gpsView.setText(hideGps ? "" : gpsAddress);
////                    if(!hideGps&&gpsAddress.length()>0){
////                        gpsView.setSelection(gpsAddress.length());
////                    }
//                if (TextUtils.isEmpty(gpsHint)) {
//                    gpsView.setHint(hideGps ? R.string.amaya_gps_hide_true : R.string.amaya_gps_hide_false);
//                } else {
//                    gpsView.setHint(hideGps ? getResources().getString(R.string.amaya_gps_hide_true) : gpsHint);
//                }
//                break;
//            case R.id.amaya_tpitem_autotext:
//                if (AmayaNetUtil.isNetworkAvailable()) {
//                    Intent intent = new Intent(getContext(), AmayaMapMakersActivity.class);
//                    intent.putExtra("bean", bean);
//                    intent.putExtra("index", index);
//                    intent.putExtra("aera", aera);
//                    intent.putExtra("action", AmayaConstants.AMAYA_ACTION_GPS_CHOOSE);
////                    if (amayaPois != null && amayaPois.size() > 0) {
////                        Iterator<AmayaPoi> iterator = amayaPois.values().iterator();
////                        values = new ArrayList<AmayaPoi>(amayaPois.size());
////                        while (iterator.hasNext()) {
////                            values.add(iterator.next());
////                        }
////                        Collections.sort(values);
////                        AmayaDataCenter.put(values.hashCode(), values);
////                    }
//                    if (values == null) {
//                        valuesInited = true;
//                        checkPois(true);
//                    } else if (amayaPois.size() != values.size()) {
//                        checkPois(true);
//                    }
//                    if (values != null) {
//                        AmayaDataCenter.put(values.hashCode(), values);
//                        intent.putExtra("amaya", values.hashCode());
//                    }
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getContext().startActivity(intent);
////                    MatrixActivity ma = ((MatrixActivity) getContext());
////                    ma.openActionbarForRefreshing(true);
////                    ma.refreshMenu();
////                    ma.overridePendingTransition(R.anim.slide_holder, R.anim.slide_in_right);
//                } else {
////                    ((MatrixActivity) getContext()).showNetErrorDialog();
//                }
//                break;
        }

    }

    public void updateAddress(double latitude, double longitude, String address) {
        gpsView.updateTextView(latitude, longitude, address);
    }

    public void onDestroy() {
        if (amayaPois != null) {
            amayaPois.clear();
            amayaPois = null;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        showOtherTopicDialog();
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void showOtherTopicDialog() {
//        final MatrixActivity ma = (MatrixActivity) getContext();
//        if (ma == null) return;
//        SimpleDialogFragment.createBuilder(ma, ma.getSupportFragmentManager())
//                .setTitle(R.string.amaya_insert)
//                .setPositiveButtonText(R.string.amaya_daily_title)
//                .setNegativeButtonText(R.string.amaya_title_daily_note)
//                .setClickListener(new ISimpleDialogListener() {
//                    @Override
//                    public void onPositiveButtonClicked(int requestCode) {
//                        MyDailyTopicFragment.inserIndex = index;
//                        AmayaConstants.AMAYA_BOOLEAN_INSERT_BILL = true;
//                        ma.showCommonListFragment(false, true, AmayaConstants.AMAYA_FRAGMENT_BILLS, null);
//                    }
//
//                    @Override
//                    public void onNeutralButtonClicked(int requestCode) {
//                    }
//
//                    @Override
//                    public void onNegativeButtonClicked(int requestCode) {
//                        AmayaConstants.AMAYA_BOOLEAN_INSERT_NOTE = true;
//                        MyDailyTopicFragment.inserIndex = index;
//                        ma.showCommonListFragment(false, true, AmayaConstants.AMAYA_FRAGMENT_PARENT_NOTES, null);
//
//                    }
//                }, 0)
//                .show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int after) {
//        if (before == 1) {
//            lastChar = charSequence.charAt(start);
//        }
//        if(charIndexs != null){
//            for(int i=0;i<charIndexs.size();i++){
//                AmayaLog.e("amaya","beforeTextChanged()...i="+i+"--charIndex="+charIndexs.get(i));
//            }
//        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }



    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        img.setTag(index);
        gpsView.setTopicIndex(index);
        gpsView.setTag(index);
//        if (index == 0) {
//            descView.setHint(R.string.amaya_daily_topic_item_hint);
//            gpsView.setTextHint(R.string.amaya_gps_hint);
//            if (AmayaGPSUtil.getLastResult() != null) {
//                localHash = AmayaGPSUtil.getLastResult().hashCode();
//                gpsRegeocodeSearched(AmayaGPSUtil.getLastResult(), 0);
//            }
//        }
    }


    public EvinImage getBean(){
        return bean;
    }

    @Subscribe
    public void onEventMainThread(AMapLocation location) {
        EventBus.getDefault().unregister(this);
        String address = location.getStreet();
        if(TextUtils.isEmpty(address)){
            if(!TextUtils.isEmpty(location.getPoiName())) {
                address = location.getPoiName();
            }else if(!TextUtils.isEmpty(location.getDistrict())) {
                address = location.getDistrict();
            }else if(!TextUtils.isEmpty(location.getCity())) {
                address = location.getCity();
            }else if(!TextUtils.isEmpty(location.getProvince())) {
                address = location.getProvince();
            }
        }
        bean.setLatitude(location.getLatitude());
        bean.setLongitude(location.getLongitude());
        bean.setAddress(address);
        gpsView.updateTextView(location.getLatitude(), location.getLongitude(), address);
    }
    public void onEventMainThread(AmayaEvent.RequestAddressEvent event) {
        if (event.hashKey != hashCode()) return;
        EventBus.getDefault().unregister(this);
        gpsView.gpsRegeocodeSearched(event.regeocodeResult, event.rCode);
    }

    public void setBean(EvinImage bean) {
        this.bean = bean;
        setImageView(AmayaConstants.PREFIX_FILE+bean.getPath());
        EventBus.getDefault().register(this);
        if(bean.getLatitude() != 0 && bean.getLongitude() != 0 && !TextUtils.isEmpty(bean.getAddress())){
            gpsView.setText(bean.getAddress());
        }else{
            gpsView.update();
        }
        if(bean.getTime() != null){
            if(bean.getTime().getBcTime()){
                yearView.setText(getContext().getString(R.string.year_bc)+TimeUtil.parseTime(TimeUtil.PATTERN_3,bean.getTime().getTimeStamp()).replaceAll("^(0+)",""));
            }else{

                yearView.setText(TimeUtil.parseTime(TimeUtil.PATTERN_3,bean.getTime().getTimeStamp()).replaceAll("^(0+)",""));
            }
            calendar.setTimeInMillis(bean.getTime().getTimeStamp());
        }

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int ADorBC, int year, int monthOfYear, int dayOfMonth) {
        EvinTime time = bean.getTime();
        if( time == null){
            time= new EvinTime();
        }
        time.setBcTime(ADorBC == GregorianCalendar.BC);
        calendar.set(Calendar.ERA,ADorBC);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        time.setTimeStamp(calendar.getTimeInMillis());
        time.setTimeYear((ADorBC == GregorianCalendar.BC?-1:1)*year);
        if(time.getBcTime()){
            yearView.setText(getContext().getString(R.string.year_bc)+TimeUtil.parseTime(TimeUtil.PATTERN_3,time.getTimeStamp()).replaceAll("^(0+)",""));
        }else{

            yearView.setText(TimeUtil.parseTime(TimeUtil.PATTERN_3,time.getTimeStamp()).replaceAll("^(0+)",""));
        }
        long insert = XApplication.getDaoSession().getEvinTimeDao().insertOrReplace(time);
        time.setId(insert);
        bean.setTime(time);
    }
}
