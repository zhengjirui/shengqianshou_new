package com.lechuang.lqsq.activity;

import android.view.View;
import android.widget.TextView;

import com.lechuang.lqsq.R;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：08:55
 * 描述：
 */

public abstract class BaseNormalTitleActivity extends BaseActivity {

    public void back(View view) {
        finish();
    }

    public void setTitleName(String titleName) {
        ((TextView) findViewById(R.id.titleName)).setText(titleName);
    }

}
