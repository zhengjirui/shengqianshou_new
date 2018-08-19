/**
 * @Title SystemUtils.java
 * @Desc 系统公交类
 * @author shanghl
 * @date 2014年5月15日
 * <p>
 * History:
 * 2014年5月22日 |  shanghl   |  Created
 */
package com.lechuang.lqsq.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

public class SystemUtils {
    public static String getDeviceManufacture() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取版本code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
        }
        return packInfo != null ? packInfo.versionCode : 1;
    }

    public static String getMetaString(Context context, String name, String def) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMetaInt(Context context, String name, int def) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getInt(name, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
        }
        return packInfo != null ? packInfo.versionName : "1.0.0";
    }

    public static String getStringFormManifest(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = info.metaData;
            return metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测GPS是否可用
     */
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获取设备的IMSI
     */
    public static String getIMSI(Context context) {
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getSubscriberId();
    }

    /**
     * 获取设备的手机号码
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 取决于：
        // SIM卡是否写入号码
        // 用户是否同意读取手机状态
        String number = tm.getLine1Number();
        if (TextUtils.isEmpty(number)) {
            return number.replace("+86", "");
        }
        return number;
    }

    /**
     * 获取设备的IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取设备的MacAddress
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info == null ? "" : info.getMacAddress();
    }

    /**
     * 获取设备的IMEI或者MacAddress
     */
    public static String getIMEIOrMacAddress(Context context) {
        String result = getIMEI(context);
        if (TextUtils.isEmpty(result)) {
            return getMacAddress(context);
        }
        return result;
    }

    /**
     * 该手机是否有相机app
     *
     * @param context
     * @return
     */
    public static boolean hasCameraApp(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        return list != null && list.size() > 0;
    }

    /**
     * 该手机是否有相册app
     *
     * @param context
     * @return
     */
    public static boolean hasGalleryApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        return list != null && list.size() > 0;
    }

    /**
     * 判断应用是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isTopActivity(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }

        }
        return false;
    }

    public static String topActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            return tasksInfo.get(0).topActivity.getClassName();
        }
        return null;
    }

    /**
     * 应用程序是否曾经开启过
     *
     * @param context
     * @return
     */
    public static boolean isApplicationStartBefore(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo runningTaskInfo = manager.getRunningTasks(1).get(0);
        return runningTaskInfo.numActivities > 1;
    }

    public static void shareToSms(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }



}
