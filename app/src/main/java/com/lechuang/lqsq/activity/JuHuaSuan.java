package com.lechuang.lqsq.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.fragments.JHSFragment;

import butterknife.BindView;

/**
 * Created by YST on 2018/2/27.
 */

public class JuHuaSuan extends BaseActivity {
    @BindView(R.id.tablayout_home_top)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String[] title = {"女装", "内衣", "男装", "母婴", "美妆", "居家", "鞋包服饰", "美食", "文体车品", "数码家电", "其他"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_jhs;
    }

    @Override
    public void initView() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                JHSFragment fragment = new JHSFragment();
                fragment.setClassType(++position + "");
                return fragment;
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tab.setupWithViewPager(viewPager);
        tab.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    public void back(View view) {
        finish();
    }
}
