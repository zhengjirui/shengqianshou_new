package com.lechuang.lqsq.activity.getmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.LoginActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.activity.SearchResultNewActivity;
import com.lechuang.lqsq.bean.GetBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.GetApi;
import com.lechuang.lqsq.widget.views.SpannelTextView;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetBaseFragment extends ViewPagerFragment implements OnItemClick, XRecyclerView.LoadingListener {

    private CommonRecyclerAdapter mAdapter;
    private XRecyclerView rvProduct;
    private ImageView ivTop;
    private int page = 1;
    private int pageFlag = 0;
    private int rootId;//当前页面的分类id
    private List<GetBean.ListInfo> mProductList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout commonLoading;
    private long mTime;
    private boolean isBottom;

    public GetBaseFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_get_base, container, false);
        }
        rootId = getArguments().getInt(Constant.KEY_ROOT_ID);
        rvProduct = (XRecyclerView) rootView.findViewById(R.id.get_product);
        ivTop = (ImageView) rootView.findViewById(R.id.iv_top);
        commonLoading = (RelativeLayout) rootView.findViewById(R.id.common_loading_all);

        initView();

        return rootView;
    }


    private void initView() {
        rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastVisibleItemPosition() > 15) {
                    ivTop.setVisibility(View.VISIBLE);
                } else {
                    ivTop.setVisibility(View.GONE);
                }

                if (mProductList.size() - mLayoutManager.findLastVisibleItemPosition() < 5) {
                    if (System.currentTimeMillis() - mTime > 1000 && !isBottom) {
                        page += 1;
                        getInfoList();
                    }
                }
            }

        });
        ivTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvProduct.scrollToPosition(0);
            }
        });
        rvProduct.setLoadingListener(GetBaseFragment.this);
    }


    private void getInfoList() {
        mTime = System.currentTimeMillis();
        if ((1 == page && mAdapter == null) || !TextUtils.isEmpty(searchContent)) {
            commonLoading.setVisibility(View.VISIBLE);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        if (!TextUtils.isEmpty(searchContent)) {
            map.put("name", searchContent);
        }
        if (rootId != -1) {
            map.put("classTypeId", rootId);
        }

        Network.getInstance().getApi(GetApi.class)
                .listInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<GetBean>() {
                    @Override
                    public void success(GetBean result) {
                        commonLoading.setVisibility(View.GONE);
                        if (result == null) return;
                        final List<GetBean.ListInfo> list = result.productList;
                        if (page > 1 && list.isEmpty()) {
                            //数据没有了
//                            Utils.show(getActivity(), "亲,已经到底了~");
                            rvProduct.noMoreLoading();
                            isBottom = true;
                            return;
                        }
                        if (!TextUtils.isEmpty(searchContent)) {
                            mProductList.clear();
                        }
                        if (pageFlag != page) {
                            mProductList.addAll(list);
                        }

                        if (page == 1) {
                            if (mAdapter == null) {
                                mAdapter = new CommonRecyclerAdapter(getActivity(), R.layout.item_get, mProductList) {
                                    @Override
                                    public void convert(ViewHolderRecycler helper, Object data) {
                                        try {
                                            GetBean.ListInfo item = (GetBean.ListInfo) data;

                                            Glide.with(getActivity()).load(item.imgs).placeholder(R.drawable.ic_home_default).into((ImageView) helper.getView(R.id.img));
                                            Glide.get(getActivity()).setMemoryCategory(MemoryCategory.LOW);

                                            helper.setText(R.id.price, item.price);
                                            ((TextView) helper.getView(R.id.price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                            helper.setText(R.id.preferentialPrice, "¥ " + item.preferentialPrice);
                                            helper.setText(R.id.nowNumber, "已售出 " + item.nowNumber + " 件");
                                            helper.setText(R.id.couponMoney, item.couponMoney + "元");
                                            //helper.setText(R.id.zhuanMoney, String.format("赚:%s", listInfo.zhuanMoney));
                                            if (UserHelper.isLogin() && UserHelper.getUserInfo(getActivity()).isAgencyStatus == 1) {
                                                helper.setText(R.id.zhuanMoney, item.zhuanMoney);
                                            } else {
                                                //不是代理 不显示赚
                                                helper.setText(R.id.zhuanMoney, "");
                                            }
                                            SpannelTextView productName = helper.getView(R.id.spannelTextView);
                                            productName.setShopType(item.shopType == null ? 1 : Integer.parseInt(item.shopType));
                                            productName.setDrawText(item.productName);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                mLayoutManager.setSmoothScrollbarEnabled(true);

                                rvProduct.setNestedScrollingEnabled(false);
                                rvProduct.setLayoutManager(mLayoutManager);
                                rvProduct.setAdapter(mAdapter);
                                mAdapter.setOnItemClick(GetBaseFragment.this);
                            }
                        } else {

                        }
                        rvProduct.refreshComplete();
                        if (!TextUtils.isEmpty(searchContent)) {
                            rvProduct.scrollToPosition(0);
                            searchContent = null;
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @Override
    public void itemClick(View v, int position) {

        if (!UserHelper.isLogin()) {
            LoginActivity.launchActivity(getActivity());
        } else {

            if (UserHelper.getUserInfo(getActivity()).isAgencyStatus == 0) {
//                startActivity(new Intent(getActivity(), ApplyAgentActivity.class));
            } else {
//                startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                        .putExtra(Constant.listInfo, JSON.toJSONString(mProductList.get(position))));
                findProductInfo(mProductList.get(position).alipayItemId,mProductList.get(position).id);
            }
        }
    }

    @Override
    public void onRefresh() {
        searchContent = null;
        isBottom = false;
        page = 1;
        if (mProductList != null) {
            mProductList.clear();
        }
        getInfoList();
    }

    @Override
    public void onLoadMore() {
        page += 1;
        getInfoList();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible && mAdapter == null) {
            getInfoList();
        } else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String searchContent;

    public void search(String str) {
        searchContent = str;
        getInfoList();
    }

    /**
     * 查询商品信息，跳转到商品详情
     *
     * @param productId
     */
    private void findProductInfo(String productId,String id) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        if (!TextUtils.isEmpty(id)){
            map.put("id", id);//后台需要添加
        }
        Network.getInstance().getApi(CommenApi.class)
                .getProductInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<LiveProductInfoBean>((BaseActivity)getActivity()) {
                    @Override
                    public void success(LiveProductInfoBean result) {
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                });

    }
}
