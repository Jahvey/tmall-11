package com.qyluo.tmall.utils;

/**
 * Created by qy_lu on 2017/5/1.
 */
public class DateUtil {
    public static java.sql.Timestamp d2t(java.util.Date date) {
        if (null == date) {
            return null;
        }
        return new java.sql.Timestamp(date.getTime());
    }

    public static java.util.Date t2d(java.sql.Timestamp timestamp) {
        if (null == timestamp) {
            return null;
        }
        return new java.util.Date(timestamp.getTime());
    }
}
