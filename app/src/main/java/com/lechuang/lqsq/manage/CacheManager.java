package com.lechuang.lqsq.manage;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.db.cache.CacheDBManager;
import com.lechuang.lqsq.db.cache.CacheDao;
import com.lechuang.lqsq.db.cache.CacheHelper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/6/21
 * 时间：15-36
 */

public class CacheManager {
    private static volatile CacheManager instance;
    private static CacheDao cacheDao;

    private CacheManager(Context context) {
        CacheDBManager.initializeInstance(new CacheHelper(context));
        cacheDao = new CacheDao();

    }

    public static CacheManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(context);
                }
            }
        }
        return instance;
    }


    public void put(String key, Object object) {
        put(key, new Gson().toJson(object));
    }

    public void put(String key, String value) {
        cacheDao.insertItem(key, value);
    }

    public void put(Map<String, String> keyValues) {
        cacheDao.insertItems(keyValues);
    }

    public void remove(String... keys) {
        cacheDao.removeItem(keys);
    }

    public Object get(String key, Object defValue) {
        String result = cacheDao.getValue(key);
        if (TextUtils.isEmpty(result)) {
            return defValue;
        }
        try {
            if (defValue instanceof String) {
                return result;
            } else if (defValue instanceof Integer) {
                return Integer.parseInt(result);
            } else if (defValue instanceof Boolean) {
                return Boolean.valueOf(result);
            } else if (defValue instanceof Float) {
                return Float.valueOf(result);
            } else if (defValue instanceof Long) {
                return Long.valueOf(result);
            } else if (defValue instanceof Double) {
                return Double.valueOf(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T get(String key, Class<T> classOfT) {
        return get(key, (Type) classOfT);

    }


    public <T> T getArray(String key) {
        Type t = new TypeToken<List<T>>() {
        }.getType();
        return get(key, t);
    }


    public <T> T get(String key, Type typeOfT) {
        T t = null;
        String result = cacheDao.getValue(key);
        if (TextUtils.isEmpty(result)) {
            return t;
        }
        try {
            t = new Gson().fromJson(result, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


}
