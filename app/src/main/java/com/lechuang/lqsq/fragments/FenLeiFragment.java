package com.lechuang.lqsq.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.SearchActivity;
import com.lechuang.lqsq.bean.FenLeiBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.HomeApi;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/30
 * 时间：14:00
 * 描述：
 */

public class FenLeiFragment extends BaseFragment {
    @BindView(R.id.tablayout_home_top)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private FenLeiBean data;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fenlei_fragment;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        viewPager.setOffscreenPageLimit(3);
        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                FenLeiErFragment fragment = new FenLeiErFragment();
                fragment.setData(data.classTypeList.get(position).rootId, data.classTypeList.get(position).tbClassTypeList);
                return fragment;
            }

            @Override
            public int getCount() {
                if (data == null)
                    return 0;
                return data.classTypeList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return data.classTypeList.get(position).rootName;
            }
        };
        viewPager.setAdapter(mAdapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {
        super.initData();
        Network.getInstance().getApi(HomeApi.class)
                .getClassify()
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<FenLeiBean>() {
                    @Override
                    protected void success(FenLeiBean result) {
                        if (result == null) return;
                        data = result;
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        SearchActivity.lanuchActivity(getActivity(), 1);
    }
}
