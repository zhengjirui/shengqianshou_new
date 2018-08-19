package com.lechuang.lqsq.activity;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.widget.views.BigPicMap;

import java.util.ArrayList;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：14:39
 * 描述：
 */

public class SunBigPicActivity extends BaseActivity {
    private BigPicMap iv_sun_map;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sun_big_pic;
    }

    @Override
    public void initView() {
        iv_sun_map = (BigPicMap) findViewById(R.id.iv_sun_map);
    }

    @Override
    public void initData() {
        if (getIntent().getIntExtra("live", 0) == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add(getIntent().getStringExtra("bigImg"));
            iv_sun_map.setAdapter(list);
        } else {
            ArrayList<String> list = getIntent().getStringArrayListExtra("list");
            iv_sun_map.setAdapter(getIntent().getStringArrayListExtra("list"));
            iv_sun_map.setCurrentItem(getIntent().getIntExtra("current", 0));
        }
    }
}
