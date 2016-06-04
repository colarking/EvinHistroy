package com.evin.bean;

import com.amap.api.services.core.LatLonPoint;
import com.evin.util.CommonUtil;

/**
 * Created with IntelliJ IDEA.
 * User: evin
 * Date: 14-3-13
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
public class AmayaPoi implements Comparable<AmayaPoi>{
    public String name,distance;
    public double latitude,longitude,dis;

    public AmayaPoi(String title, LatLonPoint latLonPoint) {
        name = title;
        if(name == null) name = "";
        latitude =latLonPoint.getLatitude();
        longitude =  latLonPoint.getLongitude();
        dis = CommonUtil.getDistance(longitude,latitude);
        distance = CommonUtil.parseDistance(dis).toString();
    }

    public void updateDistance( double lat,double lon){
        latitude =  lat;
        longitude = lon;
        dis = CommonUtil.getDistance(longitude,latitude);
        distance = CommonUtil.parseDistance(dis).toString();
    }

    @Override
    public String toString() {
        return name;
    }


    public String toFullString() {
        return "AmayaPoi{" +
                "name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int compareTo(AmayaPoi another) {
        return this.dis > another.dis ? 1:-1;
    }
}
