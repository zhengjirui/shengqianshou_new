package com.lechuang.lqsq.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnIncomeBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的收益 底部(今日统计 ...)
 */
public class IncomeBaseFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_income_self_value)
    TextView tvSelfValue;
    @BindView(R.id.tv_income_self_rate)
    TextView tvSelfRate;
    @BindView(R.id.tv_income_agency_value)
    TextView tvAgencyValue;
    @BindView(R.id.tv_income_agency_rate)
    TextView tvAgencyRate;

    // 我的收益类型
    private int mIncomeType;

    //是否可见
    protected boolean isVisible;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;


    @Override
    protected void initData() {
        // 加载数据判断是否可见，进行懒加载
//        if (!isPrepared || !isVisible) {
//            return;
//        }

        mIncomeType = getArguments().getInt("type", 1);
        Network.getInstance().getApi(OwnApi.class)
                .ownIncome(mIncomeType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnIncomeBean>(IncomeBaseFragment.this) {
                    @Override
                    public void success(OwnIncomeBean result) {
                        Log.i(mIncomeType + " ResultBack_successed", JSON.toJSONString(result));
                        tvSelfValue.setText("¥" + result.record.ownIncome);
                        tvSelfRate.setText(result.record.ownIncomeNum);
                        tvAgencyValue.setText("¥" + result.record.agencyIncome);
                        tvAgencyRate.setText(result.record.agencyIncomeNum);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            //初始化完成，切处于可见状态
            onVisible();
        } else {
            isVisible = false;
            onInVisible();
        }
    }

    protected void onInVisible() {

    }

    protected void onVisible() {
        initData();
    }

    @Override
    public void onDestroyView() {
        isPrepared = false;
        super.onDestroyView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_income_base;
    }

}
