package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.HomeSearchResultBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.widget.views.GridItemDecoration;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/06
 * 时间：11:35
 * 描述：我的足迹
 */

public class WoDeZujiActivity extends BaseNormalTitleActivity implements OnItemClick {
    @BindView(R.id.common_loading_all)
    RelativeLayout loading;
    @BindView(R.id.ll_noNet)
    LinearLayout error;
    @BindView(R.id.content)
    XRecyclerView content;
    private CommonRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private List<HomeSearchResultBean.ProductListBean> contentList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.zuji_activity;
    }

    @Override
    public void initView() {
        setTitleName("我的足迹");
        mAdapter = new CommonRecyclerAdapter(WoDeZujiActivity.this, R.layout.item_zuji, contentList) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) o;
                holder.displayImage(R.id.img, bean.imgs, R.drawable.ic_home_default);
                holder.getView(R.id.ll_jifen).setVisibility(TextUtils.isEmpty(bean.zhuanMoney) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.jiangli, bean.zhuanMoney);
                holder.setSpannelTextViewGrid(R.id.tv_name, TextUtils.isEmpty(bean.name) ? "     " : bean.name, bean.shopType == null ? 1 : Integer.parseInt(bean.shopType));
                holder.setText(R.id.yiqiang, "已抢" + bean.nowNumber + "件");
                holder.getView(R.id.ll_quan).setVisibility(TextUtils.isEmpty(bean.couponMoney) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.quan_num, bean.couponMoney + "元");
                holder.setText(R.id.quanhoujia, bean.preferentialPrice);
                TextView jiage = holder.getView(R.id.jiage);
                jiage.setText("￥" + bean.price);
                jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }

        };
        mLayoutManager = new GridLayoutManager(WoDeZujiActivity.this, 2);
        mLayoutManager.setSmoothScrollbarEnabled(true);

        content.setLayoutManager(mLayoutManager);
        content.setNestedScrollingEnabled(false);
        content.setAdapter(mAdapter);
        content.addItemDecoration(new GridItemDecoration(
                new GridItemDecoration.Builder(WoDeZujiActivity.this)
                        .margin(4, 4)
                        .horSize(16)
                        .verSize(16)
                        .showLastDivider(true)
        ));
        mAdapter.setOnItemClick(WoDeZujiActivity.this);
        mAdapter.notifyDataSetChanged();
        content.setLoadingMoreEnabled(true);
        content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                page ++ ;
                initData();

            }
        });


    }

    @Override
    public void initData() {
        subscription = Network.getInstance().getApi(OwnApi.class)
                .getZuji(page)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeSearchResultBean>() {
                    @Override
                    protected void success(HomeSearchResultBean data) {
                        content.refreshComplete();
                        if (data == null || data.productList == null || data.productList.size() == 0) {
                            content.noMoreLoading();
                            return;
                        }
                        contentList.clear();
                        contentList.addAll(data.productList);
                        mAdapter.notifyDataSetChanged();
                        content.refreshComplete();

                    }

                    @Override
                    public void error(int code, String msg) {
                        content.refreshComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loading.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                    }
                });
    }


    @OnClick(R.id.iv_tryAgain)
    public void onViewClicked() {
        error.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClick(View v, int position) {
//        Intent intent = new Intent(this, ProductDetailsActivity.class);
//        intent.putExtra(Constant.listInfo, JSON.toJSONString(contentList.get(position)));
//        intent.putExtra("isClose", true);
//        startActivity(intent);
        findProductInfo(contentList.get(position).alipayItemId,contentList.get(position).id);
    }

    /**
     * 查询商品信息，跳转到商品详情
     *
     * @param productId
     */
    private void findProductInfo(String productId,String id) {
        showWaitDialog("");
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        if (!TextUtils.isEmpty(id)){
            map.put("id", id);//后台需要添加
        }
        Network.getInstance().getApi(CommenApi.class)
                .getProductInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<LiveProductInfoBean>(WoDeZujiActivity.this) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        hideWaitDialog();
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(WoDeZujiActivity.this, ProductDetailsActivity.class)
                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
                                .putExtra("isClose", true));
                    }

                    @Override
                    public void error(int code, String msg) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }
                });

    }
}
