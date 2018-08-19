package com.lechuang.lqsq.db.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/9/26
 * 时间：10:29
 */

public class CacheHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "cache.db";
    public final static int DB_VERSION_CODE = 1;
    private final String CACHE_INFO = "CREATE TABLE "
            + CacheDao.TABLE_NAME + " ("
            + CacheDao.TABLE_COLUMN_KEY + " TEXT PRIMARY KEY, "
            + CacheDao.TABLE_COLUMN_VALUE + " TEXT);";


    public CacheHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION_CODE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CACHE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
