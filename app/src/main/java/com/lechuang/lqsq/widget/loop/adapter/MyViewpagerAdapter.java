package com.lechuang.lqsq.widget.loop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lechuang.lqsq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LGH
 * @since: 2018/4/27
 * @describe:
 */

public class MyViewpagerAdapter extends PagerAdapter {

    private Context context;
    private Bitmap mErCodeBitmap;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<String> mList = new ArrayList<>();



    public MyViewpagerAdapter(Context context) {
        this.context = context;
    }

    private void initBitmaps() {
        Bitmap bitmap = null;
        bitmaps.clear();
        for (int i = 0; i < mList.size(); i++) {
            bitmaps.add(bitmap);
        }

    }

    public void addAll(List<String> list, Bitmap mErCodeBitmap) {
        mList.clear();
        this.mList.addAll(list);
        this.mErCodeBitmap = mErCodeBitmap;
        initBitmaps();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_share_layout, null);
        final ImageView ivShareBg = (ImageView) inflate.findViewById(R.id.iv_share_bg);
        ImageView ivShareErErcode = (ImageView) inflate.findViewById(R.id.iv_share_ercode);
//        ivShareErErcode.setImageBitmap(mErCodeBitmap);
        Glide.with(context).load(mList.get(position % mList.size())).asBitmap()
                .override(300, 300)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivShareBg.setImageBitmap(resource);
                        bitmaps.set(position, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Bitmap bitmap = null;
                        bitmaps.set(position, bitmap);
                    }
                });
        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public Bitmap getBitMap(int position){
        if (bitmaps.size() <=0)
            return null;
        return bitmaps.get(position);
    }
}