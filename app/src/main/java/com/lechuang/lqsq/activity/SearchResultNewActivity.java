package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.HomeSearchResultBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.views.ConditionScreenView;
import com.lechuang.lqsq.widget.views.MGridView;
import com.lechuang.lqsq.widget.views.SpannelTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yrj on 2017/8/17.
 * 搜索结果页面
 */


public class SearchResultNewActivity extends BaseActivity implements ConditionScreenView.IConditionScreenLisenter, View.OnClickListener {

    //搜索结果展示的方式  1:综合排序  2.券后价 0.销量  3.按新品展示  4.优惠券面值从高到低  5.优惠券面值从低到高  6.预估佣金由高到低
    private int showStyle = 1;
    private ImageView iv_price;
    private Context mContext = SearchResultNewActivity.this;
    //展示在搜索框上的搜索内容
    private String rootName;
    //入参的搜索内容
    private String productName;
    /**
     * 入参的排序方式
     * isVolume 1代表按销量排序从高到底
     * isAppraise 1好评从高到底
     * isPrice  1价格从低到高排序
     * isPrice  2价格从高到低排序
     * isNew    1新品商品冲最近的往后排序
     */
    private String style = "isVolume=1";
    //入参 页数
    private int page = 1;
    //
    //上个界面传递过来的值,用来判断是从分类还是搜索跳过来的 1:分类 2:搜索界面
    private int productstyle = 1;
    //可以刷新的gridview
    private PullToRefreshScrollView refreshScrollView;
    private MGridView gv_search;
    //无网络状态
    private LinearLayout ll_notNet;
    //拼接完的参数
    private String allParameter;
    //刷新重试按钮
    private ImageView iv_tryAgain;
    // 没有商品展示默认图片
    private RelativeLayout nothingData;
    //商品头图片 淘宝或天猫
    private int headImg;
    //参数map
    private HashMap<String, String> allParamMap;
    //是否人工筛选
    private boolean isPeopleChange = false;

    //保存用户登录信息的sp
    private ConditionScreenView mConditionSceeeview;
    private int numColumns = 2;
    private TextView mTvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //商品集合
        productList = new ArrayList<>();
        allParamMap = new HashMap<>();
        //保存用户登录信息的sp
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_search_result;
    }



    @Override
    public void initView() {
        mConditionSceeeview = (ConditionScreenView) findViewById(R.id.condition_screeview);
        ll_notNet = (LinearLayout) findViewById(R.id.ll_noNet);
        //刷新重试
        iv_tryAgain = (ImageView) findViewById(R.id.iv_tryAgain);
        iv_tryAgain.setOnClickListener(this);
        nothingData = (RelativeLayout) findViewById(R.id.common_nothing_data);
        nothingData.setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
        iv_price = (ImageView) findViewById(R.id.iv_price);
        gv_search = (MGridView) findViewById(R.id.gv_search);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.ptrsv);
        refreshScrollView.onRefreshComplete();

        mConditionSceeeview.setConditionScreenLayout(this);
//        findViewById(R.id.ll_type).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        productstyle = getIntent().getIntExtra("type", 1);
        if (productstyle == 1) {//1:分类
            productName = "&classTypeId=" + getIntent().getStringExtra("rootId");
        } else {  //2 搜索
            productName = "&name=" + getIntent().getStringExtra("rootId");
        }
        rootName = getIntent().getStringExtra("rootName");
        if (productstyle == 2) {
            ((TextView) findViewById(R.id.tv_search)).setText(rootName);

        }
        refreshScrollView.setOnRefreshListener(refresh);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != productList) {
            productList.clear();
        }
        getData();
    }


    private List<HomeSearchResultBean.ProductListBean> productList;
    //适配器
    private CommonAdapter<HomeSearchResultBean.ProductListBean> mAdapter;

    //网络请求获取数据
    private void getData() {
        gv_search.setNumColumns(numColumns);
        showWaitDialog("");
        if (allParamMap != null) {
            allParamMap.clear();
        }
        if (Utils.isNetworkAvailable(mContext)) {
            ll_notNet.setVisibility(View.GONE);
            refreshScrollView.setVisibility(View.VISIBLE);
            //区分搜索还是分类跳转
            productstyle = getIntent().getIntExtra("type", 1);
            //拼接之后的参数
            //allParameter = "?page=" + page + productName + "&" + style;
            allParamMap.put("page", page + "");
            if (productstyle == 1) {
                //分类
                allParamMap.put("classTypeId", getIntent().getStringExtra("rootId"));
            } else {
                //搜索
                allParamMap.put("name", getIntent().getStringExtra("rootId"));
            }
            //是否人工筛选
            allParamMap.put("flag", isPeopleChange ? 1 + "" : 0 + "");

            if (showStyle == 0) {
                //按销量
                if(xiaoliangIsHighToDown){//从高到低
                    allParamMap.put("isVolume", 1 + "");
                }else{
                    allParamMap.put("isVolume", 2 + "");
                }

            } else if (showStyle == 1) {
                //按好评排序
                allParamMap.put("isAppraise", 1 + "");
            } else if (showStyle == 2) {
                //按价格排序
                /**
                 * isPrice 1 价格从低到高排序
                 * isPrice 2 价格从高到低排序
                 */
                if (priceIsHighToDown) {
                    //价格从高到低
                    allParamMap.put("isPrice", 2 + "");
                } else {
                    allParamMap.put("isPrice", 1 + "");
                }
            } else if (showStyle == 3) {
                //按新品排序
                allParamMap.put("isNew", 1 + "");
            }else if(showStyle == 6){//预估佣金
                allParamMap.put("isZhuan", 1 + "");
            }
            Network.getInstance().getApi(HomeApi.class)
                    .searchResult(allParamMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<HomeSearchResultBean>() {
                        @Override
                        protected void success(HomeSearchResultBean result) {
                            hideWaitDialog();
                            if (result == null) {
                                nothingData.setVisibility(View.VISIBLE);
                                if (mAdapter != null)
                                    mAdapter.notifyDataSetChanged();
                                refreshScrollView.onRefreshComplete();
                                return;
                            }
                            List<HomeSearchResultBean.ProductListBean> list = result.productList;
                            refreshScrollView.setMode(list.size() > 0 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                            if (page == 1 && (list.toString().equals("[]") || list.size() <= 0)) {
                                nothingData.setVisibility(View.VISIBLE);
                                if (mAdapter != null)
                                    mAdapter.notifyDataSetChanged();
                                refreshScrollView.onRefreshComplete();
                                return;
                            }
                            nothingData.setVisibility(View.GONE);
                            if (productList.size() > 0 && list.toString().equals("[]")) {
                                showShortToast("亲!已经到底了");
                                refreshScrollView.onRefreshComplete();
                                return;
                            }
                            for (int i = 0; i < list.size(); i++) {
                                productList.add(list.get(i));
                            }
                            refreshScrollView.onRefreshComplete();
                            //只有page=1 的时候设置适配器 下拉刷新直接调用notifyDataSetChanged()
                            if (page == 1) {

                                if (numColumns == 1){
                                    setSingLineAdapter();
                                }else {
                                    setMoreLineAdapter();
                                }
                            } else {
                                mAdapter.notifyDataSetChanged();
                            }
                            gv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    startActivity(new Intent(mContext, ProductDetailsActivity.class)
//                                            .putExtra(Constant.listInfo, JSON.toJSONString(productList.get(position))));
                                    findProductInfo(productList.get(position).alipayItemId,productList.get(position).id);

                                }
                            });

                        }

                        @Override
                        public void error(int code, String msg) {

                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            hideWaitDialog();
                        }
                    });
        } else {
            ll_notNet.setVisibility(View.VISIBLE);
            refreshScrollView.setVisibility(View.GONE);
            hideWaitDialog();
        }

    }


    //价格从高到底
    private boolean priceIsHighToDown = true;
    //销量从高到低
    private boolean xiaoliangIsHighToDown = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
                finish();
                break;

            case R.id.ll_search: //搜索
                startActivity(new Intent(mContext, SearchActivity.class));
                finish();
                break;
            case R.id.common_nothing_data:
                page = 1;
                if (null != productList)
                    productList.clear();
                getData();
                refreshScrollView.getRefreshableView().smoothScrollTo(0, 0);
                refreshScrollView.onRefreshComplete();
                break;
            case R.id.tv_search:
                Intent intent = new Intent(this,SearchActivity.class);
                //传递一个值,搜索结果页面用来判断是从分类还是搜索跳过去的 1:分类 2:搜索界面
                intent.putExtra("type", getIntent().getIntExtra("type", 1));
                //rootName传递过去显示在搜索框上
                intent.putExtra("rootName", getIntent().getStringExtra("rootName"));
                //rootId传递过去入参
                intent.putExtra("rootId", getIntent().getStringExtra("rootId"));
                intent.putExtra("searchContent", mTvSearch.getText());
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }



    private PullToRefreshBase.OnRefreshListener2 refresh = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            String label = DateUtils.formatDateTime(
                    mContext.getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            // 显示最后更新的时间
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            page = 1;
            if (null != productList) {
                productList.clear();
            }
            //refreshScrollView.getRefreshableView().smoothScrollTo(0, 0);
            // 模拟加载任务
            getData();
            refreshScrollView.onRefreshComplete();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            String label = DateUtils.formatDateTime(
                    mContext.getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            // 显示最后更新的时间
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            page += 1;
            // 模拟加载任务
            getData();
            refreshScrollView.onRefreshComplete();
        }
    };


    @Override
    public void currentSelectType(boolean zongHe, boolean quanHouJia, boolean xiaoLiang, int typeValue) {

        if (zongHe) {
            //综合排序
            if(typeValue == 0){
                showStyle = 1;
            }

            //预估佣金由高到低
            if(typeValue == 3){
                showStyle = 6;
            }
            page = 1;
            if (productList != null)
                productList.clear();
            getData();
        }

        if (quanHouJia) {
            /**
             * isPrice 1 价格从低到高排序
             * isPrice 2 价格从高到低排序
             */
            showStyle = 2;
            //价格从高到低
            priceIsHighToDown = typeValue == 0;
            page = 1;
            if (productList != null)
                productList.clear();
            getData();
        }

        if (xiaoLiang) {
            showStyle = 0;
            xiaoliangIsHighToDown = typeValue == 0;
            page = 1;
            if (productList != null)
                productList.clear();
            getData();
        }

    }

    @Override
    public void changeLayout(boolean singeLine) {

        if (singeLine){
            numColumns = 1;
            gv_search.setNumColumns(numColumns);
            setSingLineAdapter();
        }else {
            numColumns = 2;
            gv_search.setNumColumns(numColumns);
            setMoreLineAdapter();
        }

    }

    private void setMoreLineAdapter() {
        mAdapter = new CommonAdapter<HomeSearchResultBean.ProductListBean>(productList, mContext, R.layout.item_search_product_new1) {
            @Override
            public void setData(CommonViewHolder viewHolder, Object item) {
                HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) item;

                viewHolder.displayImage(R.id.iv_img, bean.imgs);
                //原件
                TextView view = viewHolder.getView(R.id.tv_oldprice);
                view.setText("¥" + bean.price);
                view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                //销量
                viewHolder.setText(R.id.tv_number, "已抢" + bean.nowNumber + "件");
                if(bean.couponMoney != null && !bean.couponMoney.equalsIgnoreCase("")){
                    //券
                    viewHolder.setText(R.id.tv_juan, "领¥" +bean.couponMoney + "券");
                    viewHolder.setText(R.id.tv_nowprice, bean.preferentialPrice);
                    viewHolder.setText(R.id.tv_head, "¥");
                }else {
                    viewHolder.setText(R.id.tv_juan, "立即购买");
                    viewHolder.setText(R.id.tv_nowprice, bean.preferentialPrice);
                    viewHolder.setText(R.id.tv_head, "¥");
                }

                View view2 = viewHolder.getView(R.id.ll_zhuan);

                if(bean.zhuanMoney != null && !bean.zhuanMoney.equalsIgnoreCase("")){
                    view2.setVisibility(View.VISIBLE);
                    String zhuanMoney = bean.zhuanMoney;
//                    viewHolder.setText(R.id.tv_zhuanMoney,zhuanMoney.substring(1,zhuanMoney.length()));
                    viewHolder.setText(R.id.tv_zhuanMoney,zhuanMoney);
                }else {
                    view2.setVisibility(View.GONE);
                    viewHolder.setText(R.id.tv_zhuanMoney,"");
                }

                SpannelTextView view1 = viewHolder.getView(R.id.tv_name);
                view1.setShopType(Integer.parseInt(bean.shopType));
                view1.setDrawText(bean.name);
            }
        };

        gv_search.setAdapter(mAdapter);
    }


    private void setSingLineAdapter() {
        mAdapter = new CommonAdapter<HomeSearchResultBean.ProductListBean>(productList,mContext,  R.layout.item_new_search_product) {
            @Override
            public void setData(CommonViewHolder viewHolder, Object item) {
                HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) item;
                viewHolder.displayImage(R.id.iv_img, bean.imgs);
                //原件
                TextView view = viewHolder.getView(R.id.tv_oldprice);
                TextView tv_zhuan = viewHolder.getView(R.id.tv_zhuan);
                LinearLayout ll_lingquan = viewHolder.getView(R.id.ll_lingquan);
                view.setText("¥" + bean.price);
                if (bean.zhuanMoney != null && UserHelper.getUserInfo(SearchResultNewActivity.this).isAgencyStatus == 1)
                    tv_zhuan.setText(bean.zhuanMoney);

                //销量
                viewHolder.setText(R.id.tv_number, "已抢" + bean.nowNumber + "件");

                //商品名字
                if (bean.couponMoney == null || bean.couponMoney.equals("0")) {
                    ll_lingquan.setVisibility(View.GONE);


                } else {
                    ll_lingquan.setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.tv_juan, "领券减" + bean.couponMoney);
                    view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                viewHolder.SpannelTextView(R.id.tv_name, bean.name,Integer.parseInt(bean.shopType));
                //新件
                viewHolder.setText(R.id.tv_nowprice, "¥" + bean.preferentialPrice);
                if (bean.shopType != null && bean.shopType.equals("2")) {
                    headImg = R.drawable.zhuan_tianmao;
                } else {  //shopType 1淘宝 2天猫 默认淘宝
                    headImg = R.drawable.zhuan_taobao;
                }
                SpannelTextView view1 = viewHolder.getView(R.id.tv_name);
                view1.setShopType(Integer.parseInt(bean.shopType));
                view1.setDrawText(bean.name);
            }
        };

        gv_search.setAdapter(mAdapter);
    }

    @Override
    public void wiperSitchChecked(boolean checkState) {
        isPeopleChange = checkState;
        if (productList != null) {
            productList.clear();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

//        if (isPeopleChange) {
//            findViewById(R.id.ll_type).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.ll_type).setVisibility(View.GONE);
//        }
        page = 1;
        getData();
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
                .subscribe(new NetResultHandler<LiveProductInfoBean>(SearchResultNewActivity.this) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        hideWaitDialog();
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(SearchResultNewActivity.this, ProductDetailsActivity.class)
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
