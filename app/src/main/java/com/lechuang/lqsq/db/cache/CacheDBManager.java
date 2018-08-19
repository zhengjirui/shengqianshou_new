package com.lechuang.lqsq.db.cache;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/9/26
 * 时间：10:28
 */


public class CacheDBManager {
    private SQLiteOpenHelper mDatabaseHelper;
    private static CacheDBManager instance;
    private SQLiteDatabase mDatabase;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    private CacheDBManager(SQLiteOpenHelper helper) {
        mDatabaseHelper = helper;
    }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new CacheDBManager(helper);
        }
    }

    public static synchronized CacheDBManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(CacheDBManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (!atomicBoolean.get()) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
            try {
                mDatabase.enableWriteAheadLogging();
                atomicBoolean.set(true);
            } catch (Exception e) {
                Log.d("enableWriteAheadLogging", e.getMessage());
            }
        }
        return mDatabase;
    }

    public SQLiteDatabase getReadDatabase() {
        return mDatabaseHelper.getReadableDatabase();
    }


    public synchronized void closeDatabase() {

    }


}
