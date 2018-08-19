package com.lechuang.lqsq.widget.views.refeshview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author: LGH
 * @since: 2017/12/18
 * @describe: 通用 Adapter
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolderRecycler> implements View.OnClickListener {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClick onItemClick;


    public CommonRecyclerAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolderRecycler onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        ViewHolderRecycler viewHolder = ViewHolderRecycler.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderRecycler holder, int position)
    {
        if (onItemClick != null){
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
        }
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(ViewHolderRecycler holder, T t);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        onItemClick.itemClick(v, position);
    }
}
