package com.lechuang.lqsq.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: LGH
 * @since: 2018/3/29
 * @describe:
 */

public class ShareUtils {


    public static final String QQPACKAGENAME = "com.tencent.mobileqq";//QQ包名
    public static final String WXPACKAGENAME = "com.tencent.mm";//微信包名
    public static final String QQZPACKAGENAME = "com.qzone";//QQ空间包名
    public static final String SINAPACKAGENAME = "com.sina.weibo";//新浪包名
    public static final String WX = "com.tencent.mm.ui.tools.ShareImgUI";//分享到微信好友
    public static final String WXF = "com.tencent.mm.ui.tools.ShareToTimeLineUI";//分享到微信朋友圈
    public static final String QQ = "com.tencent.mobileqq.activity.JumpActivity";//分享到QQ好友
    public static final String QQZ = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";//分享到QQ空间
    public static final String SINA = "com.sina.weibo.composerinde.ComposerDispatchActivity";//分享到新浪微博


    /**
     * @param context
     * @param files   图片路径
     * @param comp    分享的类型
     */
    private static void shareSingleOrMoreImage(Context context, ComponentName comp, String packName, String imgUrl, File... files) {

        if (!isAvilible(context, packName)) {
            Utils.show(context, "请安装要分享渠道APP！");
            return;
        }

        ArrayList<Uri> arrayImageUris = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setComponent(comp);

        if (imgUrl != null && !imgUrl.equalsIgnoreCase("")) {

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, imgUrl);
            context.startActivity(Intent.createChooser(shareIntent, ""));

        } else if (files.length > 0) {
            for (File pic : files) {
                /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    arrayImageUris.add(Uri.fromFile(pic));
                } else {
                    //修复微信在7.0崩溃的问题
                    Uri uri = null;
                    try {
                        uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), pic.getName(), null));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    arrayImageUris.add(uri);
                }*/
                //修复微信在7.0崩溃的问题
                Uri uri = null;
                try {
                    uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), pic.getName(), null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                arrayImageUris.add(uri);
            }

            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType("image/*");
//            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayImageUris);
            shareIntent.putExtra(Intent.EXTRA_STREAM, arrayImageUris);
            context.startActivity(shareIntent);
        }


    }

    /**
     * @param context
     * @param files   图片路径
     * @param comp    分享的类型
     */
    private static void shareToWchartFs(Context context, ComponentName comp, String packName, String imgUrl, File... files) {

        if (!isAvilible(context, packName)) {
            Utils.show(context, "请安装要分享渠道APP！");
            return;
        }

        ArrayList<Uri> arrayImageUris = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setComponent(comp);

        for (File pic : files) {
            /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                arrayImageUris.add(Uri.fromFile(pic));
            } else {
                //修复微信在7.0崩溃的问题
                Uri uri = null;
                try {
                    uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), pic.getName(), null));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                arrayImageUris.add(uri);
            }*/
            Uri uri = null;
            try {
                uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), pic.getName(), null));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            arrayImageUris.add(uri);
        }

        if (imgUrl != null && !imgUrl.equalsIgnoreCase("")) {
            shareIntent.putExtra("Kdescription", imgUrl);
        }
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("image/*");
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayImageUris);
        shareIntent.putExtra(Intent.EXTRA_STREAM, arrayImageUris);
        context.startActivity(shareIntent);


    }

    public static void shareToWChart(Context context, File[] imageUris, String imgUrl) {
        ComponentName comp = new ComponentName(WXPACKAGENAME, WX);
        shareSingleOrMoreImage(context, comp, WXPACKAGENAME, imgUrl, imageUris);
    }

    public static void shareToWChartFs(Context context, File[] imageUris, String imgUrl) {
        ComponentName comp = new ComponentName(WXPACKAGENAME, WXF);
        shareToWchartFs(context, comp, WXPACKAGENAME, imgUrl, imageUris);
    }

    public static void shareToQQ(Context context, File[] imageUris, String imgUrl) {
        ComponentName comp = new ComponentName(QQPACKAGENAME, QQ);
        shareSingleOrMoreImage(context, comp, QQPACKAGENAME, imgUrl, imageUris);
    }

    public static void shareToQQZ(Context context, File[] imageUris, String imgUrl) {
        ComponentName comp = new ComponentName(QQZPACKAGENAME, QQZ);
        shareSingleOrMoreImage(context, comp, QQZPACKAGENAME, imgUrl, imageUris);
    }

    public static void shareToSinaWeiBo(Context context, File[] imageUris, String imgUrl) {
        ComponentName comp = new ComponentName(SINAPACKAGENAME, SINA);
        shareSingleOrMoreImage(context, comp, SINAPACKAGENAME, imgUrl, imageUris);
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if ((pinfo.get(i)).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }

        }
        return false;
    }

}
