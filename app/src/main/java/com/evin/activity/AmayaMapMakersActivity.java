package com.evin.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.geocoder.StreetNumber;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.evin.R;
import com.evin.adapter.AmayaPoiItemAdapter;
import com.evin.bean.AmayaPoi;
import com.evin.bean.EvinPosition;
import com.evin.presenter.IAmayaPresenter;
import com.evin.theme.EvinTheme;
import com.evin.util.AmayaDataCenter;
import com.evin.util.AmayaEvent;
import com.evin.util.AmayaGPSUtil;
import com.evin.util.AmayaSPUtil;
import com.evin.util.AmayaToast;
import com.evin.util.CommonUtil;
import com.evin.util.NetUtil;
import com.evin.util.SpeedScrollListener;
import com.evin.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created with IntelliJ IDEA.
 * User: Smith
 * Date: 14-8-7
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class AmayaMapMakersActivity extends EvinActivity implements AMapLocationListener, AMap.OnMapClickListener, PoiSearch.OnPoiSearchListener, View.OnClickListener, Animator.AnimatorListener, MenuItem.OnMenuItemClickListener, AdapterView.OnItemClickListener, LocationSource, AMap.OnMarkerClickListener, AMap.CancelableCallback, AMap.OnMarkerDragListener, SearchView.OnQueryTextListener, AdapterView.OnItemLongClickListener {
    EditText ed;
    private MapView mapView;
    private AMap aMap;
    private EvinPosition bean;
    private int currentPage;
    private int wjqStep;
    private View poiBot;
    private int lastIndex;
    private ListView poiList;
    private boolean showPoilist;
    private int mapHeight;
    private boolean scrollUp;
    private int index;
    private boolean showLoading = true;
    private int[] poiSelectIndex = new int[]{-1, -1, -1, -1, -1};
    private AmayaPoiItemAdapter<AmayaPoi> amayaPoiItemAdapter;
    private Marker marker;
    private OnLocationChangedListener mListener;
    private HashMap<Integer, MarkerOptions> amayaMakerMap = new HashMap<Integer, MarkerOptions>();
    private HashMap<Integer, ArrayList<AmayaPoi>> amayaPoiMaps = new HashMap<Integer, ArrayList<AmayaPoi>>();
    private String amayaAera;
    private SearchView searchView;
    private boolean addViewFouces;
    //    private String wjqAction;
    //    private ArrayList<AmayaPoi> aps;
    //    private PoiOverlay poiOverlay;
    private LinearLayout imgsLayout;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showLoading(false);
                    addMarkersToMap(201314, bean.getLatitude(), bean.getLongitude(), EvinTheme.instance().getLocationPin());
                    animMap();
                    break;
                case 1:

                    break;
                case 999:

                    int count = imgsLayout.getChildCount();
                    if (count > 0) {
                        int w = UIUtil.amayaWidth / 4;
                        for (int i = 1; i < count; i++) {
                            View img = imgsLayout.getChildAt(i);
//                            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) img.getLayoutParams();
//                            flp.leftMargin = i*w+5;
//                            TranslateAnimation ta = new TranslateAnimation(0f,i*w+5,0f,0f);
//                            ta.setDuration(i*500);
//                            img.startAnimation(ta);
                            img.animate().translationXBy(i * w + 5).setDuration(i * 500).start();
                        }
                    }
                    break;
                case 520:
                    animMap();
                    break;
            }
        }
    };
    private String deepType = "";// poi搜索类型
    private PoiSearch.Query query;// Poi查询条件类
    //    private int searchType = 0;// 搜索类型
    private int tsearchType = 0;// 当前选择搜索类型
    private LatLonPoint lp;// 默认西单广场
    private PoiSearch poiSearch;
    private PoiResult poiResult; // poi返回的结果
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
//    private String imageUrl;

    @Override
    protected IAmayaPresenter setIAmayaPresenter() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setThemeOnCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amaya_map_makers);
        mapView = (MapView) findViewById(R.id.amaya_map_maker);
        poiList = (ListView) findViewById(R.id.amaya_map_poi_list);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        initMapView();
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) poiList.getLayoutParams();

        flp.topMargin = UIUtil.amayaWidth / 2;
        poiList.setLayoutParams(flp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        Intent intent = getIntent();
        if (intent == null) finish();
        EvinPosition temp = intent.getParcelableExtra("bean");

//        imageUrl = intent.getStringExtra("imageUrl");
        bean = new EvinPosition();
        bean.setLatitude(intent.getDoubleExtra("lat", 0));
        bean.setLongitude(intent.getDoubleExtra("lng", 0));
        bean.setAddress(intent.getStringExtra("address"));
        if (bean.getLatitude() == 0) {
            bean.setLatitude(XApplication.evin.getLatitude());
            bean.setLongitude(XApplication.evin.getLongitude());
            bean.setAddress(XApplication.evin.getAddress());
        }
        if (temp == null) {
        } else {
            bean.setAddress(temp.getAddress());
            bean.setLatitude(temp.getLatitude());
            bean.setLongitude(temp.getLongitude());
            animMap();
        }
        index = intent.getIntExtra("index", 0);
        amayaAera = intent.getStringExtra("aera");
//        mHandler.sendEmptyMessageDelayed(0,800);
        ArrayList<AmayaPoi> aps = (ArrayList<AmayaPoi>) AmayaDataCenter.get(false, 821);
        if (aps != null && aps.size() > 0) {
            if (aps.size() > 0) setDefaultPoi(aps.get(0));
            amayaPoiMaps.put(0, aps);
            amayaPoiItemAdapter.addAll(aps);
            mHandler.sendEmptyMessageDelayed(0, 800);
        }
        if(bean.getLatitude() != 0){
            lp = new LatLonPoint(bean.getLatitude(), bean.getLongitude());
        }
        EventBus.getDefault().register(this);
//        updateGPSDatas();
        doSearchQuery();
        setTitle(R.string.amaya_title_map_maker);
    }


    private void initImgsView() {
        imgsLayout = (LinearLayout) findViewById(R.id.amaya_map_imgs);
        ImageView img1 = new ImageView(this);
        ImageView img2 = new ImageView(this);
        ImageView img3 = new ImageView(this);
        ImageView img4 = new ImageView(this);
        ImageView img5 = new ImageView(this);
        img1.setScaleType(ImageView.ScaleType.CENTER);
        img2.setScaleType(ImageView.ScaleType.CENTER);
        img3.setScaleType(ImageView.ScaleType.CENTER);
        img4.setScaleType(ImageView.ScaleType.CENTER);
        img5.setScaleType(ImageView.ScaleType.CENTER);
//        img1.setImageResource(R.drawable.img_nature1);
//        img1.setImageResource(R.drawable.img_nature2);
//        img1.setImageResource(R.drawable.img_nature3);
//        img1.setImageResource(R.drawable.img_nature4);
//        img1.setImageResource(R.drawable.img_nature5);
        int w = UIUtil.amayaWidth / 4;
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(w, w);
        HorizontalScrollView.LayoutParams hlp = (FrameLayout.LayoutParams) imgsLayout.getLayoutParams();
        hlp.width = 5 * (w + 5);
        imgsLayout.setLayoutParams(hlp);
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.amaya_map_scroll);
        FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) hsv.getLayoutParams();
        llp.height = w;
        llp.width = hlp.width;
        hsv.setLayoutParams(llp);
        img1.setLayoutParams(flp);
        img2.setLayoutParams(flp);
        img3.setLayoutParams(flp);
        img4.setLayoutParams(flp);
        img5.setLayoutParams(flp);
        imgsLayout.addView(img1);
        imgsLayout.addView(img2);
        imgsLayout.addView(img3);
        imgsLayout.addView(img4);
        imgsLayout.addView(img5);
        imgsLayout.setBackgroundColor(Color.BLUE);
        hsv.setBackgroundColor(Color.GREEN);
        for (int i = 1; i < 5; i++) {
            TranslateAnimation ta = new TranslateAnimation(0f, i * w + 5, 0f, 0f);
            ta.setDuration(i * 500);
            imgsLayout.getChildAt(i).startAnimation(ta);
        }
        mHandler.sendEmptyMessageDelayed(999, 2000);
    }

    private void setDefaultPoi(AmayaPoi ap) {
        if (bean.getLatitude() == 0) {
            if (ap.latitude != 0) {
                updateLatAndLng(ap.latitude,ap.longitude,ap.name,ap.name);
            } else if (XApplication.evin.getLatitude() != 0) {
                updateLatAndLng(XApplication.evin.getLatitude(),XApplication.evin.getLongitude(),XApplication.evin.getAddress(),XApplication.evin.getName());
                AmayaToast.show(R.string.amaya_marker_default, false);
            } else {
                // 默认武陵源区政府
                updateLatAndLng(29.345728,110.550432,getString(R.string.amaya_marker_default_wly),getString(R.string.amaya_marker_default_wly));
                AmayaToast.show(R.string.amaya_marker_default_wly, false);
            }
//        } else if (TextUtils.isEmpty(bean.gpsAddress)) {
//            bean.gpsAddress = ap.name;
        }

        updateAddress(bean.getAddress());
//        poiSelectIndex[0] = 0;
//        poiList.setSelection(0);
//        amayaPoiItemAdapter.setSelected(0);
    }


    private void updateLatAndLng(double lat,double lng,String address,String name){
        if(bean!= null){
            bean.setLatitude(lat);
            bean.setLongitude(lng);
            bean.setAddress(address);
            bean.setName(name);
        }
    }

    private void initMapView() {
        aMap = mapView.getMap();
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
//        aMap.setLocationSource(this);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        aMap.setOnMarkerClickListener(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setMyLocationRotateAngle(180);
//        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示

//        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
//        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
    }

    private void initView() {
        TextView poiHotal = (TextView) findViewById(R.id.amaya_poi_search_hotal);
        TextView poiFood = (TextView) findViewById(R.id.amaya_poi_search_food);
        TextView poiPlace = (TextView) findViewById(R.id.amaya_poi_search_place);
        TextView poiMovie = (TextView) findViewById(R.id.amaya_poi_search_movie);
        TextView poiStreet = (TextView) findViewById(R.id.amaya_poi_search_street);
        ColorStateList textSelector = getResources().getColorStateList(EvinTheme.instance().getTextColorSelector());
        int tbg = EvinTheme.instance().getTextViewBackgroundResouce();
        poiHotal.setTextColor(textSelector);
        poiFood.setTextColor(textSelector);
        poiPlace.setTextColor(textSelector);
        poiMovie.setTextColor(textSelector);
        poiStreet.setTextColor(textSelector);
        poiHotal.setBackgroundResource(tbg);
        poiFood.setBackgroundResource(tbg);
        poiPlace.setBackgroundResource(tbg);
        poiMovie.setBackgroundResource(tbg);
        poiStreet.setBackgroundResource(tbg);
        poiHotal.setOnClickListener(this);
        poiFood.setOnClickListener(this);
        poiPlace.setOnClickListener(this);
        poiMovie.setOnClickListener(this);
        poiStreet.setOnClickListener(this);
        poiBot = findViewById(R.id.amaya_map_poibot_index);
        poiBot.setBackgroundResource(EvinTheme.instance().getBotFocus());
        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) poiBot.getLayoutParams();
        llp.width = UIUtil.amayaWidth / 5;
//        TextView empty = new TextView(this);
//        empty.setText(R.string.no_result);
//        poiList.setEmptyView(empty);
        amayaPoiItemAdapter = new AmayaPoiItemAdapter<AmayaPoi>(this, new SpeedScrollListener());
        poiList.setAdapter(amayaPoiItemAdapter);
        poiList.setDividerHeight(0);
        poiList.setBackgroundColor(Color.DKGRAY);
//        poiList.setSelector(UIUtil.getPoiItemBgSelect());
        poiList.setOnItemClickListener(this);
        poiList.setOnItemLongClickListener(this);
        FrameLayout.LayoutParams plp = (FrameLayout.LayoutParams) poiList.getLayoutParams();
        plp.height = UIUtil.amayaWidth / 2;
    }

    private void showLoading(boolean show) {
        showLoading = show;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showLoading) {
            addActionBarForRefreshing(menu);
        } else {
            initSearchView();
//            menu.add(1, 1, 1, R.string.actionbar_add_txt).setIcon(R.drawable.appbar_location).setOnMenuItemClickListener(this).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
//                    | MenuItem.SHOW_AS_ACTION_IF_ROOM);
            MenuItem mi = menu.add(1, 2, 1, R.string.add)
                    .setOnMenuItemClickListener(this);
            mi.setActionView(searchView);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menu.add(1, 1, 2, R.string.ok).setOnMenuItemClickListener(this).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void addActionBarForRefreshing(Menu menu) {
        ProgressBar bar = new ProgressBar(this);
        bar.setIndeterminateDrawable(getResources().getDrawable(
                EvinTheme.instance().getProgressBarRes()));

        menu.add(0, 0, 0, R.string.refresh)
                .setActionView(bar)
//                    .setIcon(R.drawable.actionbar_compose)
//                    .setOnMenuItemClickListener(this)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    private void initSearchView() {
        if (searchView == null) {
            searchView = new SearchView(getSupportActionBar().getThemedContext());
            searchView.setIconifiedByDefault(false);
//            searchView.setSearchHintColor(UIUtil.getTextHintSearchColor());
            searchView.setQueryHint(getString(R.string.amaya_marker_add));
            searchView.setOnQueryTextListener(this);
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    addViewFouces = hasFocus;
//                    mHandler.sendEmptyMessageDelayed(1,500);
                    if (addViewFouces && scrollUp) {
                        updateUI();
                    }
                    if(!hasFocus){
//                        getActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                }
            });
        } else if (searchView.getParent() != null) {
            ((FrameLayout) searchView.getParent()).removeAllViews();
        }
    }

    private void setThemeOnCreate() {
        int theme = AmayaSPUtil.getTheme();
        EvinTheme.instance(theme);
        switch (theme) {
            case EvinTheme.THEME_BLUE:
                setTheme(R.style.AppTheme);
                break;
            case 1:
//                setTheme(R.style.CustomDarkTheme);
                break;
            case 2:
                // setTheme(R.style.DefaultDarkTheme);
                // break;
            case 3:
                // setTheme(R.style.DefaultLightTheme);
                // break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        try {
            mapView.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        amayaMakerMap.clear();
        amayaPoiMaps.clear();
        amayaPoiItemAdapter.clear();
        aMap = null;
        mHandler = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (searchView != null) {
            searchView.setFocusable(false);
            searchView.clearFocus();
        }
        updateUI();
    }

    private void updateUI() {
        if (mapHeight == 0) {
            mapHeight = findViewById(R.id.amaya_map_parent).getHeight();
        }
        if (poiList.getVisibility() == View.VISIBLE) {
            poiList.setVisibility(View.GONE);
            poiList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_out));
            if (imgsLayout != null) {
                imgsLayout.setVisibility(View.GONE);
                imgsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
            }
        } else {
            poiList.setVisibility(View.VISIBLE);
            poiList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
            if (imgsLayout != null) {
                imgsLayout.setVisibility(View.VISIBLE);
                imgsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
            }
        }
        scrollUp = !scrollUp;
    }

    private void animMapParent(final View mapParent, float from, float to) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mapParent, "y",
                from, to).setDuration(500);
        if (mapParent == mapView) {
            anim1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animMap();

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        anim1.setTarget(mapParent);
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(anim1);
        animation.start();
    }

//    private void updateGPSDatas() {
//        if (NetUtil.isNetworkAvailable()) {
//            try {
//                if (bean.getLatitude() != 0) {
//                    showLoading(true);
//                    AmayaGPSUtil.requestAddress(bean.getLatitude(), bean.getLongitude(),hashCode());
//                } else if (TextUtils.isEmpty(imageUrl)) {
//
//                } else {
//                    ExifInterface eif = new ExifInterface(imageUrl);
//                    float[] gps = new float[2];
//                    boolean geted = eif.getLatLong(gps);
//                    bean.setLatitude(gps[0]);
//                    bean.setLongitude(gps[1]);
//                    wjqStep = 0;
//                    if (geted && gps[0] != 0 && gps[1] != 0) {
//                        wjqStep = 1;
//                        lp = new LatLonPoint(bean.getLatitude(), bean.getLongitude());
//                        AmayaGPSUtil.requestAddress(gps[0], gps[1],hashCode());
//                    } else if (XApplication.evin.getLongitude() != 0) {
//                        lp.setLatitude(XApplication.evin.getLatitude());
//                        lp.setLongitude(XApplication.evin.getLongitude());
//                        bean.setLatitude(lp.getLatitude());
//                        bean.setLongitude(lp.getLongitude());
//                        AmayaGPSUtil.requestAddress(XApplication.evin.getLatitude(), XApplication.evin.getLongitude(),hashCode());
//                    } else {
////                        float latitude = AmayaSPUtil.getFloat(AmayaConstants.AMAYA_SPKEY_LAST_LATITUDE);
////                        float longitude = AmayaSPUtil.getFloat(AmayaConstants.AMAYA_SPKEY_LAST_LONGITUDE);
////                        lp = new LatLonPoint(latitude, longitude);
////                        AmayaGPSUtil.requestAddress(latitude, longitude);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//        }
//
//    }



    public void ob(){
        AmayaGPSUtil.updateGPS();
        Observable.create(new Observable.OnSubscribe<RegeocodeResult>() {
            @Override
            public void call(Subscriber<? super RegeocodeResult> subscriber) {

            }
        }).subscribe(new Action1<RegeocodeResult>() {
            @Override
            public void call(RegeocodeResult regeocodeResult) {

            }
        });
    }



    public void gpsRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                ArrayList<AmayaPoi> temp = new ArrayList<AmayaPoi>();
                RegeocodeAddress addr = result.getRegeocodeAddress();
                String province = addr.getProvince();
                String district = addr.getDistrict();
                String city = addr.getCity();
                String neighborhood = addr.getNeighborhood();
                StreetNumber sn = addr.getStreetNumber();
                String street = sn.getStreet();
                String township = addr.getTownship();
                if (lp == null) {
                    lp = sn.getLatLonPoint();
                }
                if (!TextUtils.isEmpty(province)) {
                    AmayaPoi ap = new AmayaPoi(province, lp);
                    temp.add(ap);
                }
                if (!TextUtils.isEmpty(district)) {
                    AmayaPoi ap = new AmayaPoi(district, lp);
                    temp.add(ap);
                }
                if (!TextUtils.isEmpty(township)) {
                    AmayaPoi ap = new AmayaPoi(township, lp);
                    temp.add(ap);
                }
                if (!TextUtils.isEmpty(city)) {
                    AmayaPoi ap = new AmayaPoi(city, lp);
                    temp.add(ap);
                }
                if (!TextUtils.isEmpty(street)) {
                    AmayaPoi ap = new AmayaPoi(street, lp);
                    temp.add(ap);
                }
                if (!TextUtils.isEmpty(neighborhood)) {
                    AmayaPoi ap = new AmayaPoi(neighborhood, lp);
                    temp.add(ap);
                }
                List<RegeocodeRoad> roads = addr.getRoads();
                if (roads != null && roads.size() > 0) {
                    for (int i = 0; i < roads.size(); i++) {
                        RegeocodeRoad road = roads.get(i);
                        temp.add(new AmayaPoi(road.getName(), road.getLatLngPoint()));
                    }
                }
                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                if (pois != null && pois.size() > 0) {
                    for (int i = 0; i < pois.size(); i++) {
                        PoiItem poi = pois.get(i);
                        AmayaPoi ap = new AmayaPoi(poi.toString(), poi.getLatLonPoint());
                        temp.add(ap);
                    }
                }
                if (temp.size() > 0) {
                    Collections.sort(temp);
                    amayaPoiMaps.put(0, temp);
                    amayaPoiItemAdapter.addAll(temp);
                    setDefaultPoi(temp.get(0));
                    if (!scrollUp) {
                        updateUI();
                        scrollUp = true;
                    }
                }
                addMarkersToMap(201314, bean.getLatitude(), bean.getLongitude(), EvinTheme.instance().getLocationPin());
                mHandler.sendEmptyMessageDelayed(520, 500);
                showLoading(false);
            } else {
                showLoading(false);
                AmayaToast.show(R.string.no_result, true);
            }
        } else {
            showLoading(false);
            AmayaToast.show(R.string.no_result, true);
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showLoading(true);
        currentPage = 0;
        query = new PoiSearch.Query("", deepType, amayaAera);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 2000, true));//
            // 设置搜索区域为以lp点为圆心，其周围2000米范围
            /*
             * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// 设置多边形poi搜索范围
			 */
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    /**
     * 点击下一页poi搜索
     */
    public void nextSearch() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                AmayaToast.show(R.string.no_result, true);
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        showLoading(false);
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    ArrayList<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        resetMap();
//                        poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
                        ArrayList<AmayaPoi> beans = new ArrayList<AmayaPoi>(poiItems.size());
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem poiItem = poiItems.get(i);
                            beans.add(new AmayaPoi(poiItem.toString(), poiItem.getLatLonPoint()));
                        }
                        Collections.sort(beans);
                        if (poiBot.getTag() == null) {
                            poiBot.setTag(0);
                        }
                        int tagIndex = (Integer) poiBot.getTag();
                        amayaPoiItemAdapter.addAll(beans, true, poiSelectIndex[tagIndex]);
                        amayaPoiMaps.put(tagIndex, beans);
                        if (poiSelectIndex[tagIndex] == -1) {
                            poiList.setSelection(0);
                        }
                        if (!scrollUp) updateUI();
//                        nextButton.setClickable(true);// 设置下一页可点
//                    } else if (suggestionCities != null
//                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
                    } else {
                        AmayaToast.show(R.string.no_result, true);
                    }
                }
            } else {
                AmayaToast.show(R.string.no_result, true);
            }
        } else if (rCode == 27) {
            AmayaToast.show(R.string.error_network_search, true);
        } else if (rCode == 32) {
            AmayaToast.show(R.string.error_key, true);
        } else {
            AmayaToast.show(getString(R.string.error_other) + rCode, true);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

//    @Override
//    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.amaya_poi_search_street:
                poiBot.setTag(0);
                updatePoiList(0);
                animPoibot(0);
                break;
            case R.id.amaya_poi_search_hotal:
                deepType = "酒店";
                poiBot.setTag(1);
                updatePoiList(1);
                prepareSearchPoi(1);
                break;
            case R.id.amaya_poi_search_food:
                deepType = "餐饮";
                poiBot.setTag(2);
                updatePoiList(2);
                prepareSearchPoi(2);
                break;
            case R.id.amaya_poi_search_place:
                deepType = "风景名胜";
                poiBot.setTag(3);
                updatePoiList(3);
                prepareSearchPoi(3);
                break;
            case R.id.amaya_poi_search_movie:
                deepType = "影剧院";
                poiBot.setTag(4);
                updatePoiList(4);
                prepareSearchPoi(4);
                break;
        }
    }

    private void updatePoiList(int index) {
        if (lastIndex == index) return;
        ArrayList<AmayaPoi> amayaPois = amayaPoiMaps.get(index);
        if (amayaPois == null) {
            doSearchQuery();
            if (scrollUp) updateUI();
        } else {
            amayaPoiItemAdapter.addAll(amayaPois, true, poiSelectIndex[index]);
            if (poiSelectIndex[index] == -1) {
                poiList.setSelection(0);
            } else {
                poiList.setSelection(poiSelectIndex[index]);
                updateCenterMarker(poiSelectIndex[index]);
            }
            if (!scrollUp) updateUI();
        }
    }

    private void prepareSearchPoi(int index) {
        if (lp == null) {
            AmayaToast.show(R.string.amaya_poi_search_nogps, true);
            return;
        } else if (!NetUtil.isNetworkAvailable()) {
            AmayaToast.show(R.string.amaya_net_error,true);
            return;
        }
        animPoibot(index);
    }


    private void animPoibot(int index) {
        if (lastIndex == index) return;
        int i = lastIndex - index;
        poiBot.animate().translationXBy(-1 * i * UIUtil.amayaWidth / 5).start();
        lastIndex = index;
    }

    @Override
    public void onAnimationStart(Animator animator) {
    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            if (addViewFouces) {
                String s = searchView.getQuery().toString();
                if (TextUtils.isEmpty(s)) {
                    AmayaToast.show(R.string.amaya_address_empty, true);
                    return true;
                } else {
                    addNewPoi(s);
                }
            } else {
                EventBus.getDefault().post(new AmayaEvent.MapMakerChooseEvent(bean, getIntent().getIntExtra("hashKey", 0)));
                finish();
//                HashMap map = AmayaCommonUtil.put("ok_address", bean.gpsAddress);
//                map.put("id", String.valueOf(MatrixApplication.mAccount.userId));
//                MobclickAgent.onEvent(this,"MapGPS",map);
//                Intent intent = new Intent(wjqAction);
//                intent.putExtra("index", index);
//                intent.putExtra("longitude", bean.getLongitude());
//                intent.putExtra("latitude", bean.getLatitude());
//                intent.putExtra("address", bean.getAddress());
            }
        } else {
//            LatLng ll = aMap.getCameraPosition().target;
//            MarkerOptions markerOption = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory
//                    .fromResource(UIUtil.getLocationPin())).draggable(true).perspective(true);
//            MarkerOptions markerOption = new MarkerOptions();
//            markerOption.position(ll);
//            AmayaLog.e("amaya", "onMenuItemClick()...ll=" + ll);
//            markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//            markerOption.perspective(true);
//            markerOption.draggable(true);
//            markerOption.icon(BitmapDescriptorFactory
//                    .fromResource(UIUtil.getLocationPin()));
//            Marker marker1 = aMap.addMarker(markerOption);
//            jumpPoint(marker1);
//            CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                    new LatLng(ll.latitude, ll.longitude), 18, 30, 0));
//            aMap.animateCamera(update, 1000, this);
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        poiSelectIndex[lastIndex] = i;
        updateCenterMarker(i);
    }

    private void updateCenterMarker(int i) {
        resetMap();
        AmayaPoi item = amayaPoiItemAdapter.getItem(i);
        amayaPoiItemAdapter.setSelected(i);
        bean.setLatitude( item.latitude);
        bean.setLongitude( item.longitude);
        bean.setAddress(item.name);
        Marker jumpMaker = addMarkersToMap(item.hashCode(), item.latitude, item.longitude, EvinTheme.instance().getLocationPin());
        if (XApplication.evin.getLatitude()!= 0)
            addMarkersToMap(hashCode(), XApplication.evin.getLatitude(), XApplication.evin.getLongitude(), R.mipmap.amaya_location_self);
        jumpMaker.setObject(i);
        animMap();
        updateAddress(item.toString());
    }

    private void resetMap() {
        aMap.clear();
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerDragListener(this);
    }

    private void animMap() {
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(bean.getLatitude(), bean.getLongitude()), 18, 30, 0));
        aMap.animateCamera(update, 1000, this);
    }

    private void updateAddress(String address) {
        bean.setAddress(address);
        getSupportActionBar().setSubtitle(address);

    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {

            }
        }
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * marker.getPosition().longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * marker.getPosition().latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
//        jumpPoint(marker);
        Integer index = (Integer) marker.getObject();
//        if (index != null && index < amayaPoiItemAdapter.getCount()) {
//            updateCenterMarker(index);
//        }
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                marker.getPosition(), 18, 30, 0));
        aMap.animateCamera(update, 1000, this);
//        jumpPoint(marker);

        return true;
    }

    /**
     * 在地图上添加marker
     */
    private Marker addMarkersToMap(int key, double lat, double lon, int pin) {
        Marker marker1 = null;
        if (amayaMakerMap.containsKey(key)) {
            MarkerOptions mo = amayaMakerMap.get(key);
            marker1 = aMap.addMarker(mo);
        } else {
            LatLng ll = new LatLng(lat, lon);
            MarkerOptions markerOption = new MarkerOptions().position(ll).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory
                    .fromResource(pin)).draggable(true);
            marker1 = aMap.addMarker(markerOption);
            amayaMakerMap.put(key, markerOption);
            if (pin != R.mipmap.amaya_location_self) {
                bean.setLatitude(lat);
                bean.setLongitude(lon);
            }
        }
        return marker1;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (scrollUp) {
            searchView.clearFocus();
            searchView.setFocusable(false);
            updateUI();
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;
        if (poiSelectIndex[lastIndex] == -1) {
            bean.setLatitude(latitude);
            bean.setLongitude(longitude);
        } else {
            AmayaPoi ap = amayaPoiItemAdapter.getItem(poiSelectIndex[lastIndex]);
            ap.latitude = latitude;
            ap.longitude = longitude;
            bean.setLatitude(ap.latitude);
            bean.setLongitude(ap.longitude);
            ap.updateDistance(latitude, longitude);
            amayaPoiItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (scrollUp) {
                updateUI();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            AmayaToast.show(R.string.amaya_address_empty, true);
            return true;
        } else {
            addNewPoi(query);
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void addNewPoi(String query) {
        searchView.clearFocus();
        aMap.clear();
        aMap.setMyLocationEnabled(true);
        query = CommonUtil.parseIllegalStr(query);
        double lat = aMap.getCameraPosition().target.latitude;
        double lon = aMap.getCameraPosition().target.longitude;
        bean.setLatitude(lat);
        bean.setLongitude(lon);
        AmayaPoi addPoi = new AmayaPoi(query, new LatLonPoint(lat, lon));
        ArrayList<AmayaPoi> beans = amayaPoiItemAdapter.getBeans();
        ArrayList<AmayaPoi> amayaPois = amayaPoiMaps.get(lastIndex);
        if (amayaPois == null) {
            amayaPois = new ArrayList<AmayaPoi>();
            amayaPoiMaps.put(lastIndex, amayaPois);
        }
        poiSelectIndex[lastIndex] = 0;
        amayaPoiItemAdapter.setSelected(poiSelectIndex[lastIndex]);
        beans.add(0, addPoi);
        amayaPois.add(0, addPoi);
        ArrayList<AmayaPoi> aps = (ArrayList<AmayaPoi>) AmayaDataCenter.get(false, 821);
        if (aps == null) {
            aps = new ArrayList<AmayaPoi>();
        }
        aps.add(addPoi);
        AmayaDataCenter.put(821, aps);
        updateCenterMarker(poiSelectIndex[lastIndex]);
        if (!scrollUp) {
            updateUI();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int pos = i;
        if (ed == null) {
            ed = new EditText(this);
        } else if (ed.getParent() != null) ((FrameLayout) ed.getParent()).removeAllViews();
        String name = amayaPoiItemAdapter.getItem(i).name;
        ed.setText(name);
        ed.setSelection(name.length());

        new MaterialDialog.Builder(this)
                .title(R.string.amaya_marker_rename)
                .positiveText(R.string.ok)
                .customView(ed,true)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        AmayaPoi item = amayaPoiItemAdapter.getItem(pos);
                        item.name = ed.getText().toString();
                        amayaPoiItemAdapter.notifyDataSetChanged();
                        poiSelectIndex[lastIndex] = pos;
                        updateCenterMarker(pos);
                    }
                }).build().show();

        return true;
    }

    @Subscribe
    public void onEventMainThread(AmayaEvent.RequestAddressEvent event){
        if(event.hashKey == hashCode()){
            showLoading(false);
            gpsRegeocodeSearched(event.regeocodeResult,event.rCode);
        }
    }
    @Subscribe
    public void onEventMainThread(AMapLocation location){

        updateLatAndLng(location.getLatitude(),location.getLongitude(),location.getAddress(),location.getPoiName());

        if (location.getLatitude() == 0) return;
        bean.setLatitude(location.getLatitude());
        bean.setLongitude(location.getLongitude());
        bean.setAddress(location.getCity() + location.getDistrict());
        if (lp == null) {
            lp = new LatLonPoint(bean.getLatitude(), bean.getLongitude());
        }
        AmayaGPSUtil.requestAddress(bean.getLatitude(), bean.getLongitude(), hashCode());
        float bearing = aMap.getCameraPosition().bearing;
        aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
        aMap.setMyLocationEnabled(false);

    }


}
