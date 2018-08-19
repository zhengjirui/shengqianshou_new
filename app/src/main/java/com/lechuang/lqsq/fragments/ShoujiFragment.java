package com.lechuang.lqsq.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author: zhengjr
 * @since: 2018/6/28
 * @describe:
 */

public class ShoujiFragment extends BaseFragment {
    @BindView(R.id.tl_nav_shouji_header)
    XTabLayout mTlNavShoujiHeader;
    @BindView(R.id.vp_shouji_content)
    ViewPager mVpShoujiContent;
    private String [] title = new String[]{"每日必推","推广物料"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shouji;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        mVpShoujiContent.setOffscreenPageLimit(1);
        setupViewPager();
        mTlNavShoujiHeader.addTab(mTlNavShoujiHeader.newTab().setText(title[0]));
        mTlNavShoujiHeader.addTab(mTlNavShoujiHeader.newTab().setText(title[1]));
        mTlNavShoujiHeader.setupWithViewPager(mVpShoujiContent);
}


    private void setupViewPager() {

        List<Fragment> mFragments = initFragment();
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(mFragments);
        mVpShoujiContent.setAdapter(adapter);
    }

    private List<Fragment> initFragment() {

        List<Fragment> fragments = new ArrayList<>();

        Bundle bituiBundle = new Bundle();
        bituiBundle.putInt("type",1);
        ShouJiVpContentFragment bituiFragment = ShouJiVpContentFragment.newInstance(bituiBundle);

        Bundle tuiguangBundle = new Bundle();
        tuiguangBundle.putInt("type",2);
        ShouJiVpContentFragment tuiguangFragment = ShouJiVpContentFragment.newInstance(tuiguangBundle);

        fragments.add(bituiFragment);
        fragments.add(tuiguangFragment);

        return fragments;

    }

    @Override
    protected void initData() {
        super.initData();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(List<Fragment> fragments) {
            mFragments.clear();
            mFragments.addAll(fragments);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

}
