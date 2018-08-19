package com.lechuang.lqsq.utils;

import android.util.Log;


public class LogUtils {
    private static final boolean LOG_SWITCH = true;

    public static void v(String tag, String msg) {
        if (LOG_SWITCH)
            Log.v(tag, msg);
    }

    public static void v(Object tag, String msg) {
        if (LOG_SWITCH)
            Log.v(tag.getClass().getSimpleName(), msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_SWITCH)
            Log.i(tag, msg);
    }

    public static void i(Object tag, String msg) {
        if (LOG_SWITCH)
            Log.i(tag.getClass().getSimpleName(), msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_SWITCH)
            Log.d(tag, msg);
    }

    public static void d(Object tag, String msg) {
        if (LOG_SWITCH)
            Log.d(tag.getClass().getSimpleName(), msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_SWITCH)
            Log.w(tag, msg);
    }

    public static void w(Object tag, String msg) {
        if (LOG_SWITCH)
            Log.w(tag.getClass().getSimpleName(), msg);
    }

    public static void e(String tag, Object obj) {
        if (LOG_SWITCH) {
            Log.e(tag, obj + "");
        }
    }

    public static void e(Object tag, Object obj) {
        if (LOG_SWITCH) {
            Log.e(tag.getClass().getSimpleName(), obj + "");
        }
    }

}
