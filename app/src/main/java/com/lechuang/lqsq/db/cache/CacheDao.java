package com.lechuang.lqsq.db.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/9/26
 * 时间：10:29
 */

public class CacheDao {
    public final static String TABLE_NAME = "cache_info";
    public final static String TABLE_COLUMN_KEY = "key";
    public final static String TABLE_COLUMN_VALUE = "value";

    public void insertItem(String key, String value) {
        SQLiteDatabase db = CacheDBManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMN_KEY, key);
        values.put(TABLE_COLUMN_VALUE, value);
        db.replace(TABLE_NAME, null, values);
        CacheDBManager.getInstance().closeDatabase();
    }

    public void insertItems(Map<String, String> keyValues) {
        SQLiteDatabase db = CacheDBManager.getInstance().openDatabase();
        for (String key : keyValues.keySet()) {
            String value = keyValues.get(key);
            ContentValues values = new ContentValues();
            values.put(TABLE_COLUMN_KEY, key);
            values.put(TABLE_COLUMN_VALUE, value);
            db.replace(TABLE_NAME, null, values);
        }
        CacheDBManager.getInstance().closeDatabase();
    }

    public void removeItem(String... keys) {
        SQLiteDatabase db = CacheDBManager.getInstance().openDatabase();
        for (String key : keys) {
            db.delete(TABLE_NAME, "key = ?", new String[]{key});
        }
        CacheDBManager.getInstance().closeDatabase();
    }

    public String getValue(String key) {
        String result = null;
        SQLiteDatabase db = CacheDBManager.getInstance().getReadDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{TABLE_COLUMN_VALUE}, "key = ?", new String[]{key}, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

}
