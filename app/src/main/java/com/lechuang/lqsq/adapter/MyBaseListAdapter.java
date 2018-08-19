package com.lechuang.lqsq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/1/17
 * 时间：09-59
 */

public abstract class MyBaseListAdapter<T> extends BaseAdapter {
    protected List<T> datas;
    protected Context context;

    public MyBaseListAdapter(List<T> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
