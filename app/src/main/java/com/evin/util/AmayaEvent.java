package com.evin.util;

import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;

/**
 * Created by amayababy
 * 2016-05-06
 * 下午4:25
 */
public class AmayaEvent {
    public static class RequestAddressEvent {
        public RegeocodeResult regeocodeResult;
        public int hashKey;
        public int rCode;
        public RequestAddressEvent(RegeocodeResult regeocodeResult, int hashKey, int rCode) {
            this.regeocodeResult = regeocodeResult;
            this.hashKey = hashKey;
            this.rCode = rCode;
        }
    }

    public static class RequestPermissionEvent {
        public int requestCode;

        public RequestPermissionEvent(int requestCode) {
            this.requestCode = requestCode;
        }
    }

    public static class ImageChooseEvent {
        public ArrayList<String> pics;
        public int requestHash;

        public ImageChooseEvent(ArrayList pics, int requestHash) {
            this.pics = pics;
            this.requestHash = requestHash;
        }
    }

    public static class PoiSelectEvent {
        public String address;
        public double lat,lng;
        public int hashKey;

        public PoiSelectEvent(String address, double lat, double lng, int hashKey) {
            this.address = address;
            this.lat = lat;
            this.lng = lng;
            this.hashKey = hashKey;
        }

    }
}
