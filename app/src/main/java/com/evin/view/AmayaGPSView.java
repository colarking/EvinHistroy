package com.evin.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.geocoder.StreetNumber;
import com.evin.R;
import com.evin.activity.AmayaMapMakersActivity;
import com.evin.activity.PoiAroundSearchActivity;
import com.evin.theme.EvinTheme;
import com.evin.util.AmayaConstants;
import com.evin.util.AmayaEvent;
import com.evin.util.AmayaGPSUtil;
import com.evin.util.AmayaToast;
import com.evin.util.NetUtil;
import com.evin.util.ToastUtil;
import com.evin.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AmayaGPSView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = AmayaGPSView.class.getSimpleName();

    private int padding;
    private ProgressBar amayaBar;
    private TextView amayaAddress;
    private boolean requestAddress;
    private String gpsAddress;
    private double latitude, longitude;
    private ImageView amayaHide;
    private int hideRes = R.mipmap.amaya_gps_status_on;
    private int topicIndex;

    public AmayaGPSView(Context context) {
        super(context);
        initView(context);
    }

    public AmayaGPSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AmayaGPSView);
        requestAddress = a.getBoolean(R.styleable.AmayaGPSView_needGPSAddress, false);
        padding = a.getDimensionPixelSize(R.styleable.AmayaGPSView_amaya_padding, UIUtil.dip2px(5));
        a.recycle();
        initView(context);
    }

    public AmayaGPSView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AmayaGPSView);
        a.recycle();
        requestAddress = a.getBoolean(R.styleable.AmayaGPSView_needGPSAddress, false);
        a.recycle();
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        amayaAddress = new TextView(context);
        amayaAddress.setId(R.id.amaya_gps_address);
        amayaAddress.setTextColor(getResources().getColorStateList(EvinTheme.instance().getTextColorSelector()));
        amayaAddress.setHintTextColor(getResources().getColor(R.color.amaya_text_bill_hint));
        amayaAddress.setHint(R.string.gps_loading_desc);

        amayaAddress.setPadding(padding, padding, padding, padding);
        amayaAddress.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        addView(amayaAddress, lp);

        FrameLayout frame = new FrameLayout(context);
        frame.setForegroundGravity(Gravity.CENTER_VERTICAL);

        amayaBar = new ProgressBar(context);
        int dp25 = UIUtil.dip2px(25);
        LayoutParams lp2 = new LayoutParams(dp25, dp25);
        lp2.gravity = Gravity.CENTER;

        amayaBar.setIndeterminateDrawable(getResources().getDrawable(
                EvinTheme.instance().getProgressBarRes()));
        amayaBar.setId(R.id.amaproya_gps_bar);

        RelativeLayout hideLayout = new RelativeLayout(context);
        hideLayout.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        hideLayout.setId(R.id.amaya_gps_hidegps);

        amayaHide = new ImageView(context);
        amayaHide.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        hideLayout.addView(amayaHide);
        hideLayout.addView(amayaBar, lp2);
        hideLayout.setPadding(padding, padding, padding, padding);
        frame.addView(hideLayout);
        amayaBar.setVisibility(INVISIBLE);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(frame, layoutParams);
        hideLayout.setOnClickListener(this);
        amayaAddress.setOnClickListener(this);
        if (requestAddress) update();
    }

    public void update() {
        if (NetUtil.isNetworkAvailable()) {
            showLoading();
            if(UIUtil.grantPermission((Activity) getContext(),Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION.hashCode())){
                AmayaGPSUtil.updateGPS(true);
            }
        } else {
            hideLoading();
            setText(R.string.amaya_net_error);
            setSwith(R.mipmap.amaya_gps_status_on);
        }
    }

    public void setText(int resId) {
        if (amayaAddress != null)
            amayaAddress.setText(resId);
    }

    public void setText(String text) {
        if (amayaAddress != null)
            amayaAddress.setText(text);
    }

    public void setTextSize(int size) {
        if (amayaAddress != null)
            amayaAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }


    public void showLoading() {
        if (amayaBar != null) {
            amayaBar.setVisibility(View.VISIBLE);
        }
        hideSwith();
    }

    private void showAddress(String text) {
        if (!TextUtils.isEmpty(text)) {
            amayaAddress.setText(text);
        }
    }

    public void hideLoading() {
        if (amayaBar != null)
            amayaBar.setVisibility(View.GONE);
        showSwith();
    }

    public void showSwith() {
        if (amayaHide != null)
            amayaHide.setVisibility(View.VISIBLE);
    }

    public void setSwith(int res) {
        if (amayaHide != null)
            amayaHide.setVisibility(View.VISIBLE);
        amayaHide.setImageResource(res);
    }

    public void hideSwith() {
        if (amayaHide != null)
            amayaHide.setVisibility(View.GONE);
    }

    public void toggleSwith() {
        if (hideRes == R.mipmap.amaya_gps_status_on) {
            hideRes = R.mipmap.amaya_gps_status_off;
        } else {
            hideRes = R.mipmap.amaya_gps_status_on;
        }
        amayaHide.setImageResource(hideRes);
    }

    private void updateTextView(AMapLocation location) {
        hideLoading();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        if (!TextUtils.isEmpty(location.getStreet())) {
            gpsAddress = location.getStreet();
        } else if (!TextUtils.isEmpty(location.getRoad())) {
            gpsAddress = location.getRoad();
        } else if (!TextUtils.isEmpty(location.getDistrict())) {
            gpsAddress = location.getDistrict();
        } else if (!TextUtils.isEmpty(location.getCity())) {
            gpsAddress = location.getCity();
        } else if (!TextUtils.isEmpty(location.getProvince())) {
            gpsAddress = location.getProvince();
        } else if (!TextUtils.isEmpty(location.getCountry())) {
            gpsAddress = location.getCountry();
        }
        showAddress(gpsAddress);
        setSwith(R.mipmap.amaya_gps_status_on);
    }

    public void updateTextView(double la, double lo, String addr) {
        hideLoading();
        longitude = lo;
        latitude = la;
        gpsAddress = addr;
        showAddress(addr);
        hideLoading();
        setSwith(R.mipmap.amaya_gps_status_on);
    }

    private void showAddress(int res) {
        amayaAddress.setText(res);
        amayaAddress.setVisibility(View.VISIBLE);
    }

    public void reset() {
        if (isLoading()) return;
        update();
    }

    private boolean isLoading() {
        if (amayaBar == null) return false;
        return amayaBar.getVisibility() == View.VISIBLE;
    }

    public void onDestroy() {
        amayaBar = null;
    }


    public void gpsRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                hideLoading();
                RegeocodeAddress addr = result.getRegeocodeAddress();
                StreetNumber sn = addr.getStreetNumber();
                if (addr.getPois() != null && addr.getPois().size() > 0) {
                    PoiItem poiItem = addr.getPois().get(0);
                    longitude = poiItem.getLatLonPoint().getLongitude();
                    latitude = poiItem.getLatLonPoint().getLatitude();
                    if (!TextUtils.isEmpty(poiItem.getTitle())) {
                        gpsAddress = poiItem.getTitle();
                    } else if (!TextUtils.isEmpty(poiItem.getSnippet())) {
                        gpsAddress = poiItem.getSnippet();
                    }
                } else if (sn != null && !TextUtils.isEmpty(sn.getStreet())) {
                    latitude = sn.getLatLonPoint().getLatitude();
                    longitude = sn.getLatLonPoint().getLongitude();
                    if (!TextUtils.isEmpty(addr.getNeighborhood())) {
                        gpsAddress = sn.getStreet() + addr.getNeighborhood();
                    } else {
                        gpsAddress = sn.getStreet();
                    }
                } else if (!TextUtils.isEmpty(addr.getBuilding())) {
                    gpsAddress = addr.getBuilding();
                } else if (addr.getRoads() != null && addr.getRoads().size() > 0) {
                    RegeocodeRoad road = addr.getRoads().get(0);
                    gpsAddress = road.getName();
                } else if (!TextUtils.isEmpty(addr.getDistrict())) {
                    gpsAddress = addr.getDistrict();
                } else if (!TextUtils.isEmpty(addr.getCity())) {
                    gpsAddress = addr.getCity();
                } else if (!TextUtils.isEmpty(addr.getTownship())) {
                    gpsAddress = addr.getTownship();
                } else if (!TextUtils.isEmpty(addr.getProvince())) {
                    gpsAddress = addr.getProvince();
                }
                showAddress(gpsAddress);
                setSwith(R.mipmap.amaya_gps_status_on);
            } else {
                AmayaToast.show(R.string.no_result, true);
            }
        } else if (rCode == 27) {
            AmayaToast.show(R.string.amaya_net_error, true);
        } else if (rCode == 32) {
            AmayaToast.show(R.string.error_key, true);
        } else {
            AmayaToast.show(R.string.error_other, true);
        }


    }


    public String getAddress() {
        return gpsAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amaya_gps_hidegps:
                toggleSwith();
                if (hideRes == R.mipmap.amaya_gps_status_on) {
                    if (TextUtils.isEmpty(gpsAddress))
                        update();
                    else {
                        amayaAddress.setText(gpsAddress);
                    }
                } else {
                    amayaAddress.setText("");
                    amayaAddress.setHint(R.string.amaya_gps_hide_true);

                }
                break;
            case R.id.amaya_gps_address:
                if (hideRes == R.mipmap.amaya_gps_status_off) return;

                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
//                boolean permission = AmayaUIUtil.grantPermission((Activity) getContext(), Manifest.permission.ACCESS_FINE_LOCATION, AmayaConstants.PERMISSION_ACCESS_READ_PHONE_STATE + hashCode());
                boolean permission = UIUtil.grantPermission((Activity) getContext(), Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE.hashCode() + hashCode());
                if (permission) jumpToMap();
                break;
        }
    }

    private void jumpToMap() {
        if (NetUtil.isNetworkAvailable()) {
            Intent intent = new Intent(getContext(), PoiAroundSearchActivity.class);
            intent.putExtra("lng", longitude);
            intent.putExtra("lat", latitude);
            intent.putExtra("address", gpsAddress);
            intent.putExtra("hashKey", hashCode());
            getContext().startActivity(intent);
//                builder.setTitle(R.string.dialog_gps_reget_title)
//                        .setPositiveButtonText(R.string.ok)
//                        .setNegativeButtonText(R.string.cancel)
////                        .setNeutralButtonText(R.string.amaya_gps_reset)
////                        .setView(resetView)
//                        .setTargetFragment(this, 0).show();
        } else {
            ToastUtil.show(R.string.amaya_net_error,true);
        }
    }

    public boolean isHideGps() {
        return hideRes == R.mipmap.amaya_gps_status_off;
    }

    public void setTextHint(int hint) {
        amayaAddress.setHint(hint);
    }

    public void setTopicIndex(int index) {
        this.topicIndex = index;
    }

    @Subscribe
    public void onEventMainThread(AmayaEvent.PoiSelectEvent event) {
        if(hashCode() != event.hashKey){
            return;
        }
        updateTextView(event.lat,event.lng,event.address);

    }
    @Subscribe
    public void onEventMainThread(AmayaEvent.RequestPermissionEvent event) {
        if (event.requestCode != Manifest.permission.READ_PHONE_STATE.hashCode() + hashCode())
            return;
        EventBus.getDefault().unregister(this);
        jumpToMap();
    }
}


