package com.evin.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.evin.R;
import com.evin.activity.XApplication;
import com.hwangjr.rxbus.annotation.Produce;
import com.hwangjr.rxbus.thread.EventThread;

import org.greenrobot.eventbus.EventBus;

public class AmayaGPSUtil {
    private static GeocodeSearch geocoderSearch;
    private static AMapLocationClient mAMapLocManager;
    private static AMapLocationListener locationListener;
    private static long lastRefreshTime;
    private static int currentHashKey;

    /**
     * 获取手机经纬度
     * <p/>
     * 上下文
     *
     * @return 可用的location 可能为空
     */

    public synchronized static void initGPS() {
        if (locationListener == null) {
            locationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if(aMapLocation != null){
                        XApplication.evin.setLatitude(aMapLocation.getLatitude());
                        XApplication.evin.setLongitude(aMapLocation.getLongitude());
                        XApplication.evin.setAddress(aMapLocation.getAddress());
                        XApplication.evin.setName(aMapLocation.getPoiName());
                        EventBus.getDefault().post(aMapLocation);
                    }
                    mAMapLocManager.stopLocation();
                }
            };
        }
        if (mAMapLocManager == null) {
            mAMapLocManager = new AMapLocationClient(XApplication.getContext());
            mAMapLocManager.setLocationListener(locationListener);
            mAMapLocManager.startLocation();
        }
        initAddressCallback(0);
    }


    public static void removeGPSListener() {
//        if (locationListener != null && locationManager != null) {
//            locationManager.removeUpdates(locationListener);
//        }
        try {
            if (mAMapLocManager != null && locationListener != null) {
                mAMapLocManager.setLocationListener(null);
                mAMapLocManager.stopLocation();
            }
            mAMapLocManager = null;
            locationListener = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateGPS() {
        updateGPS(false);

    }

    public static void updateGPS(boolean forceUpdate) {
        try {
            if (mAMapLocManager == null) {
                initGPS();
            }
            if (forceUpdate || System.currentTimeMillis() - lastRefreshTime > 6000) {
                lastRefreshTime = System.currentTimeMillis();
                mAMapLocManager.startLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initAddressCallback(int hashKey) {
        currentHashKey = hashKey;
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(XApplication.getContext());
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                    if (regeocodeResult == null) return;
                    EventBus.getDefault().post(new AmayaEvent.RequestAddressEvent(regeocodeResult, currentHashKey, rCode));
                }

                @Override
                public void onGeocodeSearched(GeocodeResult result, int rCode) {
//                EventBus.getDefault().post(new AmayaEvent.RequestAddressEvent(regeocodeResult,hashKey,rCode));
                    if (rCode == 1000) {
                        if (result != null && result.getGeocodeAddressList() != null
                                && result.getGeocodeAddressList().size() > 0) {
//                            GeocodeAddress address = result.getGeocodeAddressList().get(0);
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
//                geoMarker.setPosition(AMapUtil.convertToLatLng(address
//                        .getLatLonPoint()));
//                            String addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                                    + address.getFormatAddress();
//                            AmayaToast.show(addressName, true);
                        } else {
                            AmayaToast.show(R.string.no_result, true);
                        }
                    } else if (rCode == 27) {
                        AmayaToast.show(R.string.error_network_search, true);
                    } else if (rCode == 32) {
                        AmayaToast.show(R.string.error_key, true);
                    } else {
                        AmayaToast.show(R.string.error_other, true);
                    }
                }
            });
        }
    }

    @Produce(thread = EventThread.IMMEDIATE)
    public static void requestAddress(double latitude, double longitude, final int hashKey) {
        initAddressCallback(hashKey);
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static void onDestory() {
        if (geocoderSearch != null) {
            geocoderSearch = null;
        }

        if (mAMapLocManager != null) {
            mAMapLocManager = null;
        }

        locationListener = null;
    }

    public void ss() {
    }
}
