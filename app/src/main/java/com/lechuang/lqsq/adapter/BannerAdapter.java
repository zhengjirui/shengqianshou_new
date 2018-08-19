package com.lechuang.lqsq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lechuang.lqsq.R;

import java.util.List;

/**
 * @author yrj
 * @date 2017/9/29
 * @E-mail 1422947831@qq.com
 * @desc 轮播图适配器
 */
public class BannerAdapter extends StaticPagerAdapter {
    private List<String> imgs;
    private Context mContext;

    public BannerAdapter(Context mContext, List<String> imgs) {
        this.imgs = imgs;
        this.mContext = mContext;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        //view.setImageResource(imgs.get(position));
        Glide.with(mContext).load(imgs.get(position)).placeholder(mContext.getResources().getDrawable(R.drawable.rvbaner)).into(view);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }


    @Override
    public int getCount() {
        return imgs.size();
    }
}
