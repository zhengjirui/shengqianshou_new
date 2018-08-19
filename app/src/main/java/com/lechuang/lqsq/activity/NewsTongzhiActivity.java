package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lechuang.lqsq.R;

import butterknife.OnClick;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：16:43
 * 描述：
 */

public class NewsTongzhiActivity extends BaseNormalTitleActivity {
    public synchronized static void launchActivity(Context context) {
        context.startActivity(new Intent(context, NewsTongzhiActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_center;
    }

    @Override
    public void initView() {
        setTitleName("消息通知");
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.tv_jifen, R.id.ll_tz, R.id.ll_kf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_jifen:
                break;
            case R.id.ll_tz:
                break;
            case R.id.ll_kf:
                break;
        }
    }
}
