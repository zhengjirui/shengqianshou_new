package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lechuang.lqsq.R;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：16:55
 * 描述：
 */

public class ZQZNActivity extends BaseActivity {
    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, ZQZNActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_zqzn;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public void back(View view) {
        finish();
    }
}
