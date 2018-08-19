package com.lechuang.lqsq.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.bean.HomeSearchResultBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YST on 2018/2/27.
 */

public class JHSFragment extends LazyBaseFragment implements OnItemClick {
    @BindView(R.id.content)
    XRecyclerView content;
    @BindView(R.id.common_loading_all)
    View loading;
    @BindView(R.id.emptyView)
    View emptyView;
    private boolean isFirst = true;
    private String classType;
    private int page = 1;
    private CommonRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private List<HomeSearchResultBean.ProductListBean> list = new ArrayList<>();
    private View header;

    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_jhs;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible && isFirst) {
            getData();
            isFirst = false;
        }
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        mAdapter = new CommonRecyclerAdapter(getContext(), R.layout.item_zuji, list) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) o;
                holder.displayImage(R.id.img, bean.imgs, R.drawable.ic_home_default);
                holder.getView(R.id.ll_jifen).setVisibility(TextUtils.isEmpty(bean.zhuanMoney) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.jiangli, bean.zhuanMoney);
                holder.setSpannelTextViewGrid(R.id.tv_name, bean.name, bean.shopType == null ? 1 : Integer.parseInt(bean.shopType));
                holder.setText(R.id.yiqiang, "已抢" + bean.nowNumber + "件");
                TextView jiage = holder.getView(R.id.jiage);
                jiage.setText("￥" + bean.price);
                jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.getView(R.id.ll_quan).setVisibility(TextUtils.isEmpty(bean.couponMoney) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.quan_num, bean.couponMoney + "元");
                holder.setText(R.id.quanhoujia, bean.preferentialPrice);
            }

        };
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mLayoutManager.setSmoothScrollbarEnabled(true);

        content.setLayoutManager(mLayoutManager);
        content.setNestedScrollingEnabled(false);
        content.setAdapter(mAdapter);
        content.addItemDecoration(new GridItemDecoration(
                new GridItemDecoration.Builder(getContext())
                        .margin(4, 4)
                        .horSize(16)
                        .verSize(16)
                        .showLastDivider(true)
        ));
        mAdapter.setOnItemClick(this);
        content.setPageSize(5);
        content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
        header = LayoutInflater.from(getActivity()).inflate(R.layout.jsh_header, null, false);
//        content.addHeaderView(header);
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
//        map.put("classTypeId", classType);
        map.put("type", "12");
//        map.put("isAppraise", "1");
        subscription = Network.getInstance().getApi(HomeApi.class)
                .searchResult(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeSearchResultBean>() {
                    @Override
                    protected void success(HomeSearchResultBean data) {
                        if (data == null || data.productList.isEmpty()) {
                            content.refreshComplete();
                            content.noMoreLoading();
                            if (page == 1) {
                                loading.setVisibility(View.GONE);
                            }
                            return;
                        }
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(data.productList);
                        mAdapter.notifyDataSetChanged();
                        page++;
                        content.refreshComplete();
                        loading.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                    }

                    @Override
                    public void error(int code, String msg) {
                        content.refreshComplete();
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        content.refreshComplete();
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                    }


                });
    }

    @Override
    public void itemClick(View v, int position) {
//        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                .putExtra(Constant.listInfo, JSON.toJSONString(list.get(position))));
        findProductInfo(list.get(position).alipayItemId,list.get(position).id);
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
                .subscribe(new NetResultHandler<LiveProductInfoBean>((BaseActivity) getActivity()) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        hideWaitDialog();
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs)));
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
