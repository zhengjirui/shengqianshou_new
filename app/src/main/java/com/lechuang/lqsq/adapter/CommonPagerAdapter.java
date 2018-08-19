package com.lechuang.lqsq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.lechuang.lqsq.bean.GetBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: LGH
 * @since: 2017/12/21
 * @describe: 可作为通用的 TabLayout与viewpager联动 适配器
 */

public class CommonPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragment = new ArrayList<>();
    private List<GetBean.TopTab> mTopTabList = new ArrayList<>();

    public void addFragment(List<Fragment> fragments, List<GetBean.TopTab> topTabs) {
        mFragment.addAll(fragments);
        mTopTabList.addAll(topTabs);
    }


    public CommonPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTopTabList.get(position).rootName;
    }
}