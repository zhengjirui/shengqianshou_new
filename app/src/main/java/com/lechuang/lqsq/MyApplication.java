package com.lechuang.lqsq;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;


import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.manage.FolderManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.utils.CrashExceptionHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/9/4
 * 时间：14:08
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getContext() {
        return instance;
    }

    public static ArrayList<BaseActivity> activityStack = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, BuildConfig.umengKey, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");

        PlatformConfig.setWeixin(BuildConfig.wxAppid, BuildConfig.wxqianming);
        PlatformConfig.setQQZone(BuildConfig.qqAppid, BuildConfig.qqAppkey);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                //初始化成功，设置相关的全局配置参数
                // 是否使用支付宝
                AlibcTradeSDK.setShouldUseAlipay(true);
                // 设置是否使用同步淘客打点
                AlibcTradeSDK.setSyncForTaoke(true);
                // 是否走强制H5的逻辑，为true时全部页面均为H5打开
                AlibcTradeSDK.setForceH5(true);

            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明
            }
        });
        SysUtil.setApplication(this);
        //极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            YWAPI.init(this, Constant.APP_KEY);
        }
        UserHelper.init(this);
        Network.init();
//        configCollectCrashInfo();
    }

    private void configCollectCrashInfo() {
        CrashExceptionHandler crashExceptionHandler = new CrashExceptionHandler(this, FolderManager.getCrashLogFolder());
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }

    public void exit() {
        if (activityStack.isEmpty()) return;
        int count = activityStack.size() - 1;
        for (int i = count; i >= 0; i--) {
            activityStack.get(i).finish();
        }
    }

}
