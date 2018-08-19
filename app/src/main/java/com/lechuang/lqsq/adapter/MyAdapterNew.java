package com.lechuang.lqsq.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.HomeTodayProductBean;
import com.lechuang.lqsq.widget.RoundedImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianren on 2017/11/3.
 */

public class MyAdapterNew extends RecyclerView.Adapter {
    private List<Integer> mHeights;
    private List<HomeTodayProductBean.adProductList> dataList;
    private RecycleViewListener recycleViewListener;

    public MyAdapterNew(List<HomeTodayProductBean.adProductList> list, RecycleViewListener recycleViewListener) {
        this.mHeights = new ArrayList<>();
        this.dataList = list;
        this.recycleViewListener = recycleViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_today, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setData(dataList.get(position).imgs, dataList.get(position).name, dataList.get(position).preferentialPrice.toString(),
                dataList.get(position).price.toString(),
                Integer.parseInt(dataList.get(position).shopType));
        myViewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleViewListener.itemClik(dataList.get(position));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return 0;

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(List<HomeTodayProductBean.adProductList> list) {
        this.dataList = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView price, oldprice;
        private TextView title;
        private ImageView shoptype;
        private RoundedImageView img;

        private View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            init(itemView);
        }

        public void init(View view) {
            title = (TextView) view.findViewById(R.id.todayTitle);
            price = (TextView) view.findViewById(R.id.todayPrice);
            oldprice = (TextView) view.findViewById(R.id.oldPrice);
            oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            img = (RoundedImageView) view.findViewById(R.id.todayImg);
            shoptype = (ImageView) view.findViewById(R.id.shoptype);


        }

        public void setData(String img, String title, String price, String oldPrice, int shopType) {
            this.title.setText(title);
            if (shopType == 1) {
                this.shoptype.setImageDrawable(view.getResources().getDrawable(R.drawable.zhuan_taobao));

            } else {
                this.shoptype.setImageDrawable(view.getResources().getDrawable(R.drawable.zhuan_tianmao));

            }
            this.price.setText("¥" + price);
            this.oldprice.setText("¥" + oldPrice);
            Glide.with(MyApplication.getContext()).load(img).placeholder(view.getResources().getDrawable(R.drawable.jinri)).into(this.img);
        }

        public View getView() {
            return this.view;
        }
    }

    public interface RecycleViewListener {
        void itemClik(HomeTodayProductBean.adProductList listBean);
    }
}

