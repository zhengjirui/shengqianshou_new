package com.lechuang.lqsq.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lechuang.lqsq.MyApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author: zhengjr
 * @since: 2018/7/2
 * @describe:
 */

public class LoadMoreImage {

    private List<String> mDetailImages;
    private List<Integer> mPosition = new ArrayList<>();
    private List<File> mFiles = new ArrayList<>();
    private LoadListenter mLoadListenter;
    private int conntNum = 0;
    private boolean mCancal = false;


    private static class LazLoad {
        private static LoadMoreImage instance = new LoadMoreImage();
    }

    public static LoadMoreImage getInstance(){
        return LazLoad.instance;
    }



    private LoadMoreImage() {
    }

    public List<String> getDetailImages() {
        return mDetailImages;
    }

    public void setDetailImages(List<String> detailImages) {
        if (mDetailImages != null){
            mDetailImages.clear();
        }
        mDetailImages = detailImages;
        mCancal = false;
        conntNum = 0;
        if (mFiles != null)
            mFiles.clear();
        if (mPosition != null)
            mPosition.clear();
        Glide.with(MyApplication.getContext()).onStart();
    }


    public void loadImages(){
        for (int i=0;i<mDetailImages.size();i++){
            final int finalI = i;
            Glide.with(MyApplication.getContext()).load(mDetailImages.get(i)).asBitmap().into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int position = finalI;
                    File file = null;
                    try {
                        file = ImageUtils.saveImage(MyApplication.getContext(), resource, getRandomFileName(), 100);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mLoadListenter != null){
                            mLoadListenter.loadAllError();
                        }
                    }

                    if (file != null){
                        Log.e("------",finalI + file.getPath().toString());
                        mFiles.add(file);
                    }else {
                        mPosition.add(position);
                    }
                    upDataLoad();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    int position = finalI;
                    mPosition.add(position);
                    upDataLoad();
                    Log.e("------",finalI + "");

                }
            });
        }

    }

    private void upDataLoad(){
        if (mCancal){
            if (mLoadListenter != null){
                mLoadListenter.loadAllError();
            }
            return;
        }

        conntNum ++;
        if (conntNum >= mDetailImages.size() && mLoadListenter != null){

            File[] files = new File[mFiles.size()];
            for (int i=0;i<files.length;i++){
                files[i] = mFiles.get(i);
            }

            if (mPosition.size() > 0){
                mLoadListenter.loadPartSuccess(files,mPosition);
            }else {
                mLoadListenter.loadAllSuccess(files);
            }
        }


        if (conntNum >= mDetailImages.size() && mLoadListenter != null && mFiles.size() <= 0){
            mLoadListenter.loadAllError();
        }

    }

    public void clearLoad(){
        Glide.with(MyApplication.getContext()).onStop();
        conntNum = 0;
        mCancal = true;
        if (mDetailImages != null)
        mDetailImages.clear();
        if (mFiles != null)
        mFiles.clear();
        if (mPosition != null)
        mPosition.clear();
//        if (mLoadListenter != null){
//            mLoadListenter.loadAllError();
//        }
    }

    private String getRandomFileName(){
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        Random random = new Random();
        random.nextInt(99999999);
        return fileName + random.nextInt();
    }

    public void setLoadLisenter(LoadListenter loadListenter){
        this.mLoadListenter = loadListenter;
    }

    public interface LoadListenter{
        void loadAllSuccess(File[] files);//返回全部加载完成的数据
        void loadPartSuccess(File[] files,List<Integer> position);//返回加载完成的数据和未加载完成的索引
        void loadAllError();
    }
}
