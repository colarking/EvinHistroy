package com.evin.util;

import com.evin.activity.XApplication;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amayababy
 * 2016-05-05
 * 下午5:04
 */
public class CommonUtil {
    private static final double EARTH_RADIUS = 6371004;
    public static final String DIS_50_STR = "<50m";
    public static final String DIS_100_STR = "<100m";
    public static DecimalFormat df = new DecimalFormat("#########.##");
    public static double getDistance(double lng, double lat) {
        double distance = getDistance(XApplication.evin.getLongitude(), XApplication.evin.getLatitude(), lng, lat);
//        AmayaLog.e("amaya","getDistance()...distance="+distance+"--MatrixApplication.mAccount.longitude="+MatrixApplication.mAccount.longitude);
        return distance;
//        String dis = parseDistance(distance).toString();
//        return  dis==null?"":dis;
    }

    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static CharSequence parseDistance(double meters) {

//        return df.format(meters);
        String distance = null;
        if (meters < 50) {
            distance = DIS_50_STR;
        } else if (meters < 100) {
            distance = DIS_100_STR;
        } else if (meters < 1000) {
            distance = df.format(meters) + "m";
        } else {
            distance = df.format(meters / 1000) + "km";
        }
        return distance;
    }

    public static String parseIllegalStr(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        m.find();
        return m.replaceAll("").trim();
    }
}
