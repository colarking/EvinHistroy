package com.evin.util;

import java.text.SimpleDateFormat;

/**
 * Created by amayababy
 * 2015-07-21
 * 下午7:54
 */
public class TimeUtil {
    public static final long oneAllMills = 86400000;//24*3600*1000
    public static final long oneDayMills = 43200000;//24*3600*1000
    public static final long oneHourMills = 3600000;
    public static final long oneYearMills = oneAllMills * 365;//24*3600*1000
    public static final String PATTERN_1 = "yyyyMMddHHmmsss";
    public static final String PATTERN_2 = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_3 = "yyyy年MM月dd日";
    public static SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_2);

    public static String parseTime(long time) {
        sdf.applyPattern("yyyy-MM-dd HH:mm");
        return sdf.format(time);
    }

    public static String parseTime(String pattern, long time) {
        sdf.applyPattern(pattern);
        return sdf.format(time);
    }

    public static String parseTime2(long time) {
        long timeMillis = System.currentTimeMillis();
        long timeDistance = timeMillis - time;
        if (timeDistance < oneHourMills) {
            //一小时内
            if(timeDistance < 60000){
                return "刚刚";
            }else{
                return String.format("%1$d分钟前", timeDistance / 60000);
            }
        } else if (timeDistance < oneAllMills) {
            return String.format("%1$d小时前", timeDistance / oneHourMills);
        } else if (timeDistance < oneYearMills) {
            return String.format("%1$d天前", timeDistance / oneAllMills);
        } else {
            sdf.applyPattern("yyyy/MM/dd");
            return sdf.format(time);
        }
    }
}
