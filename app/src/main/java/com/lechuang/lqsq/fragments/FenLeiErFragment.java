package com.lechuang.lqsq.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.activity.SearchResultNewActivity;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.FenLeiBean;
import com.lechuang.lqsq.bean.HomeSearchResultBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.widget.views.GridItemDecoration;
import com.lechuang.lqsq.widget.views.MGridView;
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
 * 日期：2018/02/09
 * 时间：13:49
 * 描述：
 */

public class FenLeiErFragment extends LazyBaseFragment implements OnItemClick, View.OnClickListener {
    @BindView(R.id.content)
    XRecyclerView content;
    @BindView(R.id.common_loading_all)
    View loading;
    @BindView(R.id.emptyView)
    View emptyView;
    private List<FenLeiBean.TBClassTypeList> data;
    private boolean isInit = false;
    private boolean isLoading = false;
    private boolean isLoadSuccess = false;
    private CommonRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private List<HomeSearchResultBean.ProductListBean> list = new ArrayList<>();
    private int page = 1;
    private int rootid;
    private String currKey = "isAppraise", currValue = "1";
    private View[] lineView;
    private TextView[] tvView;
    private int textColor;
    private int currentIndex = 0;

    public void setData(int rootId, List<FenLeiBean.TBClassTypeList> data) {
        this.rootid = rootId;
        this.data = data;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fenlei_er;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
//        lineView = new View[]{tuijian, xiaolinag, zuixin};
        mAdapter = new CommonRecyclerAdapter(getContext(), R.layout.item_zuji, list) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                HomeSearchResultBean.ProductListBean bean = (HomeSearchResultBean.ProductListBean) o;
                holder.displayImage(R.id.img, bean.imgs, R.drawable.ic_home_default);
                holder.getView(R.id.ll_jifen).setVisibility(TextUtils.isEmpty(bean.zhuanMoney) ||
                        !UserHelper.isLogin() ||
                        UserHelper.getUserInfo(getActivity()).isAgencyStatus != 1 ?
                        View.GONE : View.VISIBLE);
                holder.setText(R.id.jiangli, bean.zhuanMoney);
                holder.setSpannelTextViewGrid(R.id.tv_name, bean.name, bean.shopType == null ? 1 : Integer.parseInt(bean.shopType));
                holder.setText(R.id.yiqiang, "已抢" + bean.nowNumber + "件");
                TextView jiage = holder.getView(R.id.jiage);
                jiage.setText("￥" + bean.price);
                jiage.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.getView(R.id.ll_quan).setVisibility(TextUtils.isEmpty(bean.zhuanMoney) ? View.GONE : View.VISIBLE);
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

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible && !isInit) {
            init();
        }
        if (isVisible && !isLoading && isInit && !isLoadSuccess) {
            getData();
        }
    }

    private View header;

    public void init() {
        if (data == null) {
            return;
        }
        header = LayoutInflater.from(getActivity()).inflate(R.layout.fenlei_er_header,
                (ViewGroup) mRoot, false);
        lineView = new View[]{header.findViewById(R.id.v_tuijian), header.findViewById(R.id.v_xiaoliang), header.findViewById(R.id.v_zuixin)};
        tvView = new TextView[]{(TextView) header.findViewById(R.id.tv_tuijian), (TextView) header.findViewById(R.id.tv_xiaoliang), (TextView) header.findViewById(R.id.tv_zuixin)};
        textColor = tvView[1].getCurrentTextColor();
        MGridView gvKind = (MGridView) header.findViewById(R.id.gv_kind);
        header.findViewById(R.id.tuijian_ll).setOnClickListener(this);
        header.findViewById(R.id.xiaoliang_ll).setOnClickListener(this);
        header.findViewById(R.id.zuixin_ll).setOnClickListener(this);
        gvKind.setAdapter(new CommonAdapter<FenLeiBean.TBClassTypeList>(data, getActivity(), R.layout.home_kinds_item) {
            @Override
            public void setData(CommonViewHolder viewHolder, Object item) {
                FenLeiBean.TBClassTypeList bean = (FenLeiBean.TBClassTypeList) item;
                //分类名称
                viewHolder.setText(R.id.tv_kinds_name, bean.name);
                //分类图片
                viewHolder.displayImage(R.id.iv_kinds_img, bean.img);
            }
        });
        gvKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SearchResultNewActivity.class);
                //传递一个值,搜索结果页面用来判断是从分类还是搜索跳过去的 1:分类 2:搜索界面
                intent.putExtra("type", 2);
                //rootName传递过去显示在搜索框上
                intent.putExtra("rootName", data.get(position).name);
                //rootId传递过去入参
                intent.putExtra("rootId", data.get(position).name + "");
                startActivity(intent);
            }
        });
        isInit = true;
        content.addHeaderView(header);
    }

    private void getData() {
        isLoading = true;
        HashMap<String, String> allParamMap = new HashMap<>();
        allParamMap.put("page", page + "");
        allParamMap.put("classTypeId", rootid + "");
        if (!TextUtils.isEmpty(currKey))
            allParamMap.put(currKey, currValue);
        subscription = Network.getInstance().getApi(HomeApi.class)
                .searchResult(allParamMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeSearchResultBean>() {
                    @Override
                    protected void success(HomeSearchResultBean data) {
                        isLoading = false;
                        isLoadSuccess = true;
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
                        isLoading = false;
                        isLoadSuccess = true;
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        content.refreshComplete();
                        super.onError(e);
                        isLoading = false;
                        loading.setVisibility(View.GONE);
                    }


                });
    }

    private void switchData(int index) {
        if (index == currentIndex) return;
        for (int i = 0; i < lineView.length; i++) {
            lineView[i].setVisibility(View.GONE);
            tvView[i].setTextColor(textColor);

        }
        tvView[index].setTextColor(getResources().getColor(R.color.main));
        lineView[index].setVisibility(View.VISIBLE);
        currentIndex = index;
        page = 1;
        content.setLoadingMoreEnabled(true);
        getData();
    }

    @Override
    public void itemClick(View v, int position) {
//        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                .putExtra(Constant.listInfo, JSON.toJSONString(list.get(position))));
        findProductInfo(list.get(position).alipayItemId,list.get(position).id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tuijian_ll:
                currKey = "isAppraise";
                currValue = "1";
                switchData(0);
                break;
            case R.id.xiaoliang_ll:
                currKey = "isVolume";
                currValue = "1";
                switchData(1);
                break;
            case R.id.zuixin_ll:
                currKey = "isNew";
                currValue = "1";
                switchData(2);
                break;
        }
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
                    public void error(int code, String msg) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }
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
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }
                });

    }
}
