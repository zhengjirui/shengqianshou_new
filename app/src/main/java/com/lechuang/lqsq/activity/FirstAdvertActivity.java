package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.BannerAdapterNew;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 作者：li on 2017/10/13 18:22
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class FirstAdvertActivity extends AppCompatActivity {
    private Context mContext = FirstAdvertActivity.this;
    private RollPagerView  mRollViewPager;
    private View button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_advert);
        button = findViewById(R.id.button);
        mRollViewPager= (RollPagerView) findViewById(R.id.advmap);
        final ArrayList<String> arrayList = getIntent().getStringArrayListExtra("arraylist");
        if (arrayList != null && arrayList.size() != 0) {
            //设置播放时间间隔
            mRollViewPager.setPlayDelay(3000);
            //设置透明度
            mRollViewPager.setAnimationDurtion(500);
            //设置适配器
            mRollViewPager.setAdapter(new BannerAdapterNew(mContext,arrayList));
            //设置指示器（顺序依次）
            //自定义指示器图片
            //设置圆点指示器颜色
            //设置文字指示器
            //隐藏指示器
            //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
            mRollViewPager.setHintView(new ColorPointHintView(mContext, Color.YELLOW, Color.WHITE));
        } else {
            finish();
        }
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position==arrayList.size()-1){
                    //点击跳转主界面
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
