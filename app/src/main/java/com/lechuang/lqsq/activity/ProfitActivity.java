package com.lechuang.lqsq.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.MyIncomeBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author yrj
 * @date 2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc 积分账单
 */
public class ProfitActivity extends BaseNormalTitleActivity implements OnItemClick {
    private Context mContext = ProfitActivity.this;
    //可刷新的listView
    private XRecyclerView rvProfit;
    private RelativeLayout commonNothing;
    //我的收益实体类
    //private MyIncome myIncome;
    //最近收益列表适配器
    private CommonRecyclerAdapter mAdapter;
    //最近收益数据集合
    private List<MyIncomeBean.RecordBean.ListBean> incomeList;
    //页数
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profit_recycler;
    }

    @Override
    public void initView() {
        setTitleName("积分账单");
        rvProfit = (XRecyclerView) findViewById(R.id.rv_profit);
        commonNothing = (RelativeLayout) findViewById(R.id.common_nothing_data);
        rvProfit.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (null != incomeList) {
                    incomeList.clear();
                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();
                }
                getData();
            }

            @Override
            public void onLoadMore() {
                page += 1;
                getData();
            }
        });
    }

    @Override
    public void initData() {
        incomeList = new ArrayList<>();
        getData();
    }

    //网络请求获取数据
    private void getData() {
        if (Utils.isNetworkAvailable(mContext)) {
            Network.getInstance().getApi(CommenApi.class)
                    //这里不传page
                    .myIncome("" + page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<MyIncomeBean>(this) {
                        @Override
                        public void success(MyIncomeBean result) {
                            if (result == null) {
                                commonNothing.setVisibility(View.VISIBLE);
                                return;
                            }
                            //积分收益列表数据
                            List<MyIncomeBean.RecordBean.ListBean> list = result.record.list;
                            if (page == 1 && list.isEmpty()) {
                                commonNothing.setVisibility(View.VISIBLE);
                                return;
                            }
                            commonNothing.setVisibility(View.GONE);
                            if (page > 1 && list.isEmpty()) {
                                //数据没有了
                                rvProfit.noMoreLoading();
                                return;
                            }
                            incomeList.addAll(list);
                            if (page == 1) {
                                if (mAdapter == null) {
                                    mAdapter = new CommonRecyclerAdapter(mContext, R.layout.item_profit, incomeList) {
                                        @Override
                                        public void convert(ViewHolderRecycler viewHolder, Object data) {
                                            try {
                                                MyIncomeBean.RecordBean.ListBean bean = (MyIncomeBean.RecordBean.ListBean) data;
                                                //收益中文描述
                                                viewHolder.setText(R.id.tv_income, bean.typeStr);
                                                //时间
                                                viewHolder.setText(R.id.tv_time, bean.createTimeStr);
                                                //积分明细
                                                viewHolder.setText(R.id.tv_integral, bean.integralDetailsStr);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                                    mLayoutManager.setSmoothScrollbarEnabled(true);

                                    rvProfit.setNestedScrollingEnabled(false);
                                    rvProfit.setLayoutManager(mLayoutManager);
                                    rvProfit.setAdapter(mAdapter);
                                    mAdapter.setOnItemClick(ProfitActivity.this);
                                }
                            } else {

                            }
                            mAdapter.notifyDataSetChanged();
                            rvProfit.refreshComplete();
                        }

                        @Override
                        public void error(int code, String msg) {

                        }
                    });
        } else {
            rvProfit.refreshComplete();
            showShortToast(getString(R.string.net_error));
        }
    }

    @Override
    public void itemClick(View v, int position) {

    }
}
