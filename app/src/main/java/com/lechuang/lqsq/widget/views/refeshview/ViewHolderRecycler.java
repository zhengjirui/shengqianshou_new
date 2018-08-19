package com.lechuang.lqsq.widget.views.refeshview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.glide.CropCircleTransformation;
import com.lechuang.lqsq.glide.RoundedCornersTransformation;
import com.lechuang.lqsq.utils.ImageUtils;
import com.lechuang.lqsq.widget.RoundedImageView;
import com.lechuang.lqsq.widget.views.SpannelTextView;
import com.lechuang.lqsq.widget.views.SpannelTextViewGrid;

/**
 * @author: LGH
 * @since: 2017/12/18
 * @describe: 通用 Recycler 的 ViewHolder
 */

public class ViewHolderRecycler extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolderRecycler(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    public static ViewHolderRecycler get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolderRecycler holder = new ViewHolderRecycler(context, itemView, parent);
        return holder;
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    //获取Layout
    public View getmConvertView() {
        return mConvertView;
    }

    //TextView设置数据
    public ViewHolderRecycler setText(int viewId, String txt) {
        if (TextUtils.isEmpty(txt)) {
            return this;
        }
        TextView mTextView = getView(viewId);
        mTextView.setText(txt);
        return this;
    }

    //TextView设置数据
    public ViewHolderRecycler setSpannelTextViewGrid(int viewId, String txt, int shopType) {
        if (TextUtils.isEmpty(txt)) {
            return this;
        }
        SpannelTextViewGrid mTextView = getView(viewId);
        mTextView.setDrawText(txt);
        mTextView.setShopType(shopType);
        return this;
    }

    //TextView设置数据
    public ViewHolderRecycler setSpannelTextView(int viewId, String txt, int shopType) {
        if (TextUtils.isEmpty(txt)) {
            return this;
        }
        SpannelTextView mTextView = getView(viewId);
        mTextView.setDrawText(txt);
        mTextView.setShopType(shopType);
        return this;
    }

    //ImageView设置数据
    public ViewHolderRecycler setImageResource(int viewId, int img) {
        ImageView mImageView = getView(viewId);
        mImageView.setImageResource(img);
        return this;
    }

    public ViewHolderRecycler displayImage(int viewId, String url) {
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        ImageView mImageView = getView(viewId);
        Glide.with(mContext).load(url).into(mImageView);
        return this;
    }

    private ImageView mImageView;

    public ViewHolderRecycler displayImageScal(int viewId, String url) {
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        mImageView = getView(viewId);
        Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap scaleBitmap = ImageUtils.getScaleBitmap(resource, MyApplication.getContext().getResources().getDisplayMetrics().widthPixels);
                ViewGroup.LayoutParams params = mImageView.getLayoutParams();
                params.height = scaleBitmap.getHeight();
                params.width = scaleBitmap.getWidth();
                mImageView.setLayoutParams(params);
                scaleBitmap.recycle();
                mImageView.setImageBitmap(resource);
            }
        });
        return this;
    }

    public ViewHolderRecycler displayImage(int viewId, String url, int defaultView) {
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        ImageView mImageView = getView(viewId);
        Glide.with(mContext).load(url).placeholder(defaultView).into(mImageView);
        return this;
    }

    public ViewHolderRecycler displayImageCircle(int viewId, String url, int defaultView) {
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        ImageView mImageView = getView(viewId);
        Glide.with(mContext).load(url).placeholder(defaultView)
                .bitmapTransform(new CropCircleTransformation(MyApplication.getContext()))
                .into(mImageView);
        return this;
    }

    public ViewHolderRecycler displayRoundImage(int viewId, String url) {
        if (TextUtils.isEmpty(url)) {
            return this;
        }
        RoundedImageView mImageView = getView(viewId);
        Glide.with(mContext).load(url).into(mImageView);
        return this;
    }

}
