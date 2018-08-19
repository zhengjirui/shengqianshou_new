package com.lechuang.lqsq.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：17:17
 * 描述：
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> datas;
    protected Context context;
    private int layoutId;

    public CommonAdapter(List<T> datas, Context context, @LayoutRes int layoutId) {
        this.datas = datas;
        this.context = context;
        this.layoutId = layoutId;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDataItem(T item) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (datas != null) {
            datas.clear();
        }
    }

    public void addDataItems(List<T> items) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        }
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(context, position, convertView, parent, layoutId);
        setData(viewHolder, datas.get(position));
        return viewHolder.getmConvertView();
    }

    public abstract void setData(CommonViewHolder viewHolder, Object item);
}