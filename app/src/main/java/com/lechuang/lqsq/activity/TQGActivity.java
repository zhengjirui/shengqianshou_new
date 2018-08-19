package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.HourTime;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.fragments.TQGFragment;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YST on 2018/3/1.
 */

public class TQGActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.time1)
    TextView time1;
    @BindView(R.id.ll_time1)
    LinearLayout llTime1;
    @BindView(R.id.time2)
    TextView time2;
    @BindView(R.id.ll_time2)
    LinearLayout llTime2;
    @BindView(R.id.time3)
    TextView time3;
    @BindView(R.id.ll_time3)
    LinearLayout llTime3;
    @BindView(R.id.time4)
    TextView time4;
    @BindView(R.id.ll_time4)
    LinearLayout llTime4;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv1)
    View iv1;
    @BindView(R.id.iv2)
    View iv2;
    @BindView(R.id.iv3)
    View iv3;
    @BindView(R.id.iv4)
    View iv4;
    @BindView(R.id.common_loading_all)
    View loading;
    private View[] views;
    private HourTime hourTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_tqg;
    }

    @Override
    public void initView() {
        views = new View[]{llTime1, llTime2, llTime3, llTime4, iv1, iv2, iv3, iv4};
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        subscription = Network.getInstance().getApi(HomeApi.class)
                .getHourTime()
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HourTime>() {
                    @Override
                    protected void success(HourTime data) {
                        hourTime = data;
                        switchTime(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String currentTime = sdf.format(hourTime.hourOfTime);
                        time2.setText(currentTime);
                        time1.setText(sdf.format(hourTime.hourOfTime - 60 * 60 * 1000));
                        time3.setText(sdf.format(hourTime.hourOfTime + 60 * 60 * 1000));
                        time4.setText(sdf.format(hourTime.hourOfTime + 2 * 60 * 60 * 1000));
                        setViewPagerAdapter();
                        switchTime(1);
                        viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCompleted() {
                        loading.setVisibility(View.GONE);
                        super.onCompleted();
                    }
                });

    }

    private void setViewPagerAdapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                TQGFragment fragment = new TQGFragment();
                switch (position) {
                    case 0:
                        fragment.setTime(hourTime.hourOfTime - 60 * 60 * 1000);
                        break;
                    case 1:
                        fragment.setTime(hourTime.hourOfTime);
                        break;
                    case 2:
                        fragment.setTime(hourTime.hourOfTime + 60 * 60 * 1000);
                        break;
                    case 3:
                        fragment.setTime(hourTime.hourOfTime + 2 * 60 * 60 * 1000);
                        break;
                }

                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }


    @OnClick({R.id.back, R.id.ll_time1, R.id.ll_time2, R.id.ll_time3, R.id.ll_time4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_time1:
                switchTime(0);
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_time2:
                switchTime(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_time3:
                switchTime(2);
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_time4:
                switchTime(3);
                viewPager.setCurrentItem(3);
                break;
        }
    }

    private int currentIndex = 0;

    private void switchTime(int index) {
        if (currentIndex == index) return;
        views[index].setBackgroundColor(ActivityCompat.getColor(this, R.color.main));
        views[index + 4].setVisibility(View.VISIBLE);
        views[currentIndex].setBackgroundColor(Color.TRANSPARENT);
        views[currentIndex + 4].setVisibility(View.INVISIBLE);
        currentIndex = index;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switchTime(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
