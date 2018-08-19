package com.lechuang.lqsq.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.manage.FolderManager;
import com.lechuang.lqsq.utils.LogUtils;


/**
 * Created by YST on 2016/7/14.
 */
public class InitModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        LogUtils.e("InitModule", "applyOptions");
        int imageDiskCacheSize = 1024 * 1024 * 50;
        int bitmapLruCacheSize = (int) (Runtime.getRuntime().maxMemory() / 4);
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        builder.setBitmapPool(new LruBitmapPool(bitmapLruCacheSize));
        builder.setDiskCache(new DiskLruCacheFactory(FolderManager.getDiskCacheDir(MyApplication.getContext()), "imageCache", imageDiskCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
