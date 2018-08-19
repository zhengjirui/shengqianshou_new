package com.lechuang.lqsq.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.glide.CropCircleTransformation;
import com.lechuang.lqsq.widget.views.SpannelTextView;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：08:54
 * 描述：
 */

public class CommonViewHolder {

    //存储各个控件View
    private SparseArray<View> mView;
    //记录索引位置
    private int mPosition;
    private View mConvertView;
    private Context context;

    //构造方法
    public CommonViewHolder(Context context, int position, ViewGroup parent, int layoutId) {
        mPosition = position;
        mView = new SparseArray<View>();
        mConvertView = View.inflate(context, layoutId, null);
        mConvertView.setTag(this);
        this.context = context;
    }

    //获取ViewHolder
    public static CommonViewHolder getViewHolder(Context context, int position,
                                                 View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new CommonViewHolder(context, position, parent, layoutId);
        } else {
            CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    //获取各个控件
    public <T extends View> T getView(int layoutId) {
        View view = mView.get(layoutId);
        if (view == null) {
            view = getmConvertView().findViewById(layoutId);
            mView.put(layoutId, view);
        }
        return (T) view;
    }


    //获取Layout
    public View getmConvertView() {
        return mConvertView;
    }

    //TextView设置数据
    public CommonViewHolder setText(int viewId, String txt) {
        TextView mTextView = getView(viewId);
        mTextView.setText(txt);
        return this;
    }

    public CommonViewHolder SpannelTextView(int viewId, String txt,int shopType) {
        SpannelTextView mTextView = getView(viewId);
        mTextView.setShopType(shopType);
        mTextView.setDrawText(txt);
        return this;
    }


    //ImageView设置数据
    public CommonViewHolder setImageResource(int viewId, int img) {
        ImageView mImageView = getView(viewId);
        mImageView.setImageResource(img);
        return this;
    }

    public CommonViewHolder displayImage(int viewId, String url) {
        ImageView mImageView = getView(viewId);
        Glide.with(context).load(url).into(mImageView);
        return this;
    }

    public CommonViewHolder displayRoundImage(int viewId, String url) {
        ImageView mImageView = getView(viewId);
        Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).into(mImageView);
        return this;
    }
}
