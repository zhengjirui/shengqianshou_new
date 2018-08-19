package com.lechuang.lqsq.manage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.net.Constant;


/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/21
 * 时间：15:01
 */

public class UserHelper {
    private static final String USER_CACHE_KEY = "userInfo";
    private static String token;
    private static UserInfo userInfo;

    public static void init(Context context) {
        userInfo = CacheManager.getInstance(context).get(USER_CACHE_KEY, UserInfo.class);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.safeToken)) {
            token = userInfo.safeToken;
        }
    }

    public static void saveUserInfo(@NonNull Context context, @NonNull UserInfo info) {
        userInfo = info;
        token = userInfo.safeToken;
        CacheManager.getInstance(context).put(USER_CACHE_KEY, userInfo);
    }

    public static UserInfo getUserInfo(Context context) {
        if (userInfo != null)
            return userInfo;
        userInfo = CacheManager.getInstance(context).get(USER_CACHE_KEY, UserInfo.class);
        if (userInfo == null)
            return null;
        token = userInfo.safeToken;
        return userInfo;
    }


    public static void clearUserToken(Context context) {
        userInfo = null;
        token = null;
        CacheManager.getInstance(context).remove(USER_CACHE_KEY);
    }

    public static boolean isLogin() {
        if (TextUtils.isEmpty(token)) {
            return false;
        }
        return true;
    }

    public static String getToken() {
        if (token == null)
            return "";
        return token;
    }
}
