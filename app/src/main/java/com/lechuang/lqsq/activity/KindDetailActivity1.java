package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.HomeSearchResultBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.glide.RoundedCornersTransformation;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.utils.UtilsDpAndPx;
import com.lechuang.lqsq.widget.views.GridItemDecoration;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：09:15
 * 描述：
 */

public class KindDetailActivity1 extends BaseNormalTitleActivity implements View.OnClickListener, OnItemClick {
    //轮播图
    private XRecyclerView rvProduct;
    //没有网络状态
    private LinearLayout ll_noNet;
    //刷新重试按钮
    private ImageView iv_tryAgain;
    //分页页数
    private int page = 1;
    //参数map
    private HashMap<String, String> allParamMap;
    private CommonRecyclerAdapter mAdapter;
    //商品集合
    private List<HomeSearchResultBean.ProductListBean> mProductList;
    //轮播图链接
    private List<String> linkList;
    //图片集合
    private List<String> imgList;
    private GridLayoutManager mLayoutManager;
    private ImageView ivTop;
    private long mTime;
    public boolean isBottom = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_program_detail_recycler;
    }

    @Override
    public void initView() {
        setTitleName(getIntent().getStringExtra("name"));
        mProductList = new ArrayList<>();
        linkList = new ArrayList<>();
        imgList = new ArrayList<>();
        allParamMap = new HashMap<>();
        //没有网络时的默认图片
        ll_noNet = (LinearLayout) findViewById(R.id.ll_noNet);
        //刷新重试
        iv_tryAgain = (ImageView) findViewById(R.id.iv_tryAgain);
        iv_tryAgain.setOnClickListener(this);
        rvProduct = (XRecyclerView) findViewById(R.id.gv_product);
        ivTop = (ImageView) findViewById(R.id.iv_top);
        ivTop.setOnClickListener(this);
    }

    @Override
    public void initData() {
        rvProduct.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isBottom = false;
                page = 1;
                if (imgList != null)
                    imgList.clear();
                if (linkList != null)
                    linkList.clear();
                if (mProductList != null)
                    mProductList.clear();
                getData();

            }

            @Override
            public void onLoadMore() {
                page += 1;
                getData();
            }
        });

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
                        getData();
                    }
                }
            }
        });
        getData();
    }

    private void getData() {
        mTime = System.currentTimeMillis();
        allParamMap.put("page", page + "");
        allParamMap.put("programaId", getIntent().getIntExtra("programaId", 1) + "");
        if (Utils.isNetworkAvailable(this)) {
            //网络畅通 隐藏无网络状态
            ll_noNet.setVisibility(View.GONE);
            Network.getInstance().getApi(HomeApi.class)
                    .getProgramaShowAll(allParamMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<HomeSearchResultBean>(KindDetailActivity1.this) {
                        @Override
                        public void success(HomeSearchResultBean result) {
                            kindData(result);
                        }

                        @Override
                        public void error(int code, String msg) {

                        }
                    });
        } else {
            //网络不通 展示无网络状态
            ll_noNet.setVisibility(View.VISIBLE);
        }


    }

    //商品头图片 淘宝或天猫
    private int headImg;

    private void kindData(HomeSearchResultBean result) {
        if (result == null)
            return;
        ll_noNet.setVisibility(View.GONE);
        //商品集合
        List<HomeSearchResultBean.ProductListBean> list = result.productList;
        for (int i = 0; i < list.size(); i++) {
            mProductList.add(list.get(i));
        }
        if (page > 1 && list.isEmpty()) {
            //数据没有了
//            Utils.show(mContext, "亲!已经到底了");
            rvProduct.noMoreLoading();
            isBottom = true;
            return;
        }
        if (page == 1) {
            if (mAdapter == null) {
                mAdapter = new CommonRecyclerAdapter(KindDetailActivity1.this, R.layout.item_program_product, mProductList) {
                    @Override
                    public void convert(ViewHolderRecycler viewHolder, Object data) {
                        try {
                            HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) data;
                            viewHolder.setText(R.id.tv_xiaoliang, "销量: " + bean.nowNumber + "件");
                            viewHolder.displayImage(R.id.iv_img, bean.imgs, R.drawable.ic_home_default);
                            ImageView img = viewHolder.getView(R.id.iv_img);
                            Glide.with(KindDetailActivity1.this).load(bean.imgs)
                                    .placeholder(R.drawable.ic_home_default)
                                    .bitmapTransform(new RoundedCornersTransformation(KindDetailActivity1.this, UtilsDpAndPx.dip2px(KindDetailActivity1.this, 5), 0)).into(img);
                            //原件
                            TextView tvOldPrice = viewHolder.getView(R.id.tv_oldprice);
                            tvOldPrice.setText("¥" + bean.price);
                            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            viewHolder.setSpannelTextViewGrid(R.id.tv_name, bean.name, bean.shopType == null ? 1 : Integer.parseInt(bean.shopType));
                            viewHolder.setText(R.id.tv_nowprice, "券后价 ¥" + bean.preferentialPrice);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                mLayoutManager = new GridLayoutManager(KindDetailActivity1.this, 2);
                mLayoutManager.setSmoothScrollbarEnabled(true);

                rvProduct.addItemDecoration(new GridItemDecoration(
                        new GridItemDecoration.Builder(KindDetailActivity1.this)
                                .margin(4, 4)
                                .horSize(16)
                                .verSize(16)
                                .showLastDivider(true)
                ));

                rvProduct.setNestedScrollingEnabled(false);
                rvProduct.setLayoutManager(mLayoutManager);
                rvProduct.setAdapter(mAdapter);
                mAdapter.setOnItemClick(KindDetailActivity1.this);
            }
        } else {

        }
        mAdapter.notifyDataSetChanged();
        rvProduct.refreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tryAgain://刷新重试
                page = 1;
                if (imgList != null)
                    imgList.clear();
                if (linkList != null)
                    linkList.clear();
                if (mProductList != null)
                    mProductList.clear();
                getData();
                break;
            // 回到顶部
            case R.id.iv_top:
                rvProduct.scrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void itemClick(View v, int position) {
//        startActivity(new Intent(this, ProductDetailsActivity.class)
//                .putExtra(Constant.listInfo, JSON.toJSONString(mProductList.get(position))));
        findProductInfo(mProductList.get(position).alipayItemId,mProductList.get(position).id);
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
                .subscribe(new NetResultHandler<LiveProductInfoBean>(KindDetailActivity1.this) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        hideWaitDialog();
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(KindDetailActivity1.this, ProductDetailsActivity.class)
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
