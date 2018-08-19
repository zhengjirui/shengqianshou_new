package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.CountBean;
import com.lechuang.lqsq.bean.HomeKindBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.SoufanliProgremBean;
import com.lechuang.lqsq.bean.SoufanliResultBean;
import com.lechuang.lqsq.fragments.ChaoJiSouFragment;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.widget.views.MGridView;
import com.lechuang.lqsq.widget.views.NumberRollingView;
import com.lechuang.lqsq.widget.views.ProgressWebView;
import com.lechuang.lqsq.widget.views.SpannelTextView;
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
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：14:21
 * 描述：直播间
 */

public class ChaoJiSouActivity extends BaseNormalTitleActivity implements View.OnClickListener, OnItemClick {
    private View header;
    private ImageView ivProgram1;
    private ImageView ivProgram2;
    private ImageView ivProgram3;
    private ImageView ivProgram4;
    private MGridView gvKind;
    private LinearLayout llProgram;
    private LinearLayout ll_like;

    @BindView(R.id.rv_soufanli)
    XRecyclerView rvSoufanli;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.et_content)
    TextView etContent;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.countNumber)
    NumberRollingView countNumber;
    @BindView(R.id.searchInfo)
    TextView searchInfo;

    @BindView(R.id.common_loading_all)
    RelativeLayout commonLoading;
    private LinearLayoutManager mLayoutManager;
    int page = 1;
    //底部数据展示
    private CommonRecyclerAdapter mAdapter;
    private List<SoufanliResultBean.ProductListBean> mProductList = new ArrayList<>();

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, ChaoJiSouActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.chaojisou_fragment;
    }

    @Override
    public void initView() {
        header = LayoutInflater.from(ChaoJiSouActivity.this).inflate(R.layout.header_soufanli,
                (ViewGroup) findViewById(android.R.id.content), false);
        //轮播图
        ivProgram1 = (ImageView) header.findViewById(R.id.iv_program1);
        ivProgram2 = (ImageView) header.findViewById(R.id.iv_program2);
        ivProgram3 = (ImageView) header.findViewById(R.id.iv_program3);
        ivProgram4 = (ImageView) header.findViewById(R.id.iv_program4);
        gvKind = (MGridView) header.findViewById(R.id.gv_kind);
        llProgram = (LinearLayout) header.findViewById(R.id.ll_program);
        ll_like = (LinearLayout) header.findViewById(R.id.ll_like);

        ivTop.setOnClickListener(this);
        search.setOnClickListener(this);
        etContent.setOnClickListener(this);
        searchInfo.setOnClickListener(this);


        rvSoufanli.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastVisibleItemPosition() > 15) {
                    ivTop.setVisibility(View.VISIBLE);
                } else {
                    ivTop.setVisibility(View.GONE);
                }
            }
        });

        rvSoufanli.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (mProductList != null)
                    mProductList.clear();
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                page = 1;
                initData();

            }

            @Override
            public void onLoadMore() {
                page += 1;
                initRecycle();
            }
        });
    }

    @Override
    public void initData() {
        if (UserHelper.isLogin()) {
            ll_like.setVisibility(View.VISIBLE);
        } else {
            ll_like.setVisibility(View.GONE);
        }
        initCont();
        initKin();
        initProgrem();
        initRecycle();

    }

    private CountBean countNumResult;
    @Override
    protected void onResume() {
        super.onResume();

        if (countNumber != null && countNumResult != null) {
            countNumber.setUseCommaFormat(false);
            countNumber.setFrameNum(50);
            countNumber.setContent(countNumResult.couponCount);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initCont() {
        Network.getInstance().getApi(HomeApi.class)
                .countSum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<CountBean>(ChaoJiSouActivity.this) {
                    @Override
                    public void success(CountBean result) {
                        countNumResult = result;
                        countNumber.setUseCommaFormat(false);
                        countNumber.setFrameNum(50);
                        countNumber.setContent(result.couponCount);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void initKin() {
        Network.getInstance().getApi(HomeApi.class)
                .homeClassify()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeKindBean>(ChaoJiSouActivity.this) {
                    @Override
                    public void success(HomeKindBean result) {
                        if (result == null) {
                            return;
                        }
                        List<HomeKindBean.ListBean> list = result.tbclassTypeList;
                        //取前10类
                        if (list.size() > 10) {
                            list = list.subList(0, 10);
                        }
                        gvKind.setAdapter(new CommonAdapter<HomeKindBean.ListBean>(list, MyApplication.getContext(), R.layout.home_kinds_item) {
                            @Override
                            public void setData(CommonViewHolder viewHolder, Object item) {
                                HomeKindBean.ListBean bean = (HomeKindBean.ListBean) item;
                                //分类名称
                                viewHolder.setText(R.id.tv_kinds_name, bean.rootName);
                                //分类图片
                                viewHolder.displayImage(R.id.iv_kinds_img, bean.img);
                            }
                        });
                        final List<HomeKindBean.ListBean> list1 = result.tbclassTypeList;
                        gvKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ChaoJiSouActivity.this, SearchResultNewActivity.class);
                                //传递一个值,搜索结果页面用来判断是从分类还是搜索跳过去的 1:分类 2:搜索界面
                                intent.putExtra("type", 1);
                                //rootName传递过去显示在搜索框上
                                intent.putExtra("rootName", list1.get(position).rootName);
                                //rootId传递过去入参
                                intent.putExtra("rootId", list1.get(position).rootId + "");
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void initRecycle() {
        if (page == 1) {
//            commonLoading.setVisibility(View.VISIBLE);
        }
        Network.getInstance().getApi(HomeApi.class)
                .soufanliProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<SoufanliResultBean>(ChaoJiSouActivity.this) {
                    @Override
                    public void success(SoufanliResultBean result) {
                        commonLoading.setVisibility(View.GONE);
                        if (result == null) {
                            ll_like.setVisibility(View.GONE);
                            return;
                        }
                        List<SoufanliResultBean.ProductListBean> list = result.productList;

                        if (list.size() == 0) {
                            ll_like.setVisibility(View.GONE);
                        }

                        if (page == 1 && mProductList != null) {
                            mProductList.clear();
                        }
                        mProductList.addAll(list);
                        if (page > 1 && list.isEmpty()) {
                            rvSoufanli.noMoreLoading();
                            return;
                        }

                        if (page == 1) {
                            if (mAdapter == null) {
                                mAdapter = new CommonRecyclerAdapter(ChaoJiSouActivity.this, R.layout.item_last_programnew, mProductList) {
                                    @Override
                                    public void convert(ViewHolderRecycler holder, Object data) {
                                        try {
                                            SoufanliResultBean.ProductListBean bean = (SoufanliResultBean.ProductListBean) data;
                                            //商品图
                                            holder.displayImage(R.id.iv_img, bean.imgs, R.drawable.ic_home_default);
//        //动态调整滑动时的内存占用
                                            Glide.get(mContext).setMemoryCategory(MemoryCategory.LOW);
                                            if (bean.couponMoney > 0) {
                                                //领券减金额
                                                holder.setText(R.id.goumai, "领券减￥" + bean.couponMoney);
                                                holder.getView(R.id.ll_quan).setVisibility(View.VISIBLE);
                                            } else {
                                                //领券减金额
                                                holder.setText(R.id.goumai, "立即购买");
                                                holder.getView(R.id.ll_quan).setVisibility(View.INVISIBLE);
                                            }
                                            //原价
                                            holder.setText(R.id.tv_oldprice, bean.price + "");

                                            holder.setText(R.id.tv_nowprice, "" + bean.preferentialPrice);
                                            if (!TextUtils.isEmpty(bean.zhuanMoney)) {
                                                holder.setText(R.id.jifen_num, bean.zhuanMoney + "");
                                            } else {
                                                holder.getView(R.id.jifen_num).setVisibility(View.INVISIBLE);
                                            }

                                            //商品名称
                                            SpannelTextView spannelTextView = holder.getView(R.id.spannelTextView);
                                            spannelTextView.setDrawText(bean.name);
                                            spannelTextView.setShopType(bean.shopType);
                                            //销量
                                            holder.setText(R.id.tv_xiaoliang, "销量：" + bean.nowNumber);
                                            TextView jinfennum = holder.getView(R.id.jifen_num);
                                            if (UserHelper.isLogin() && UserHelper.getUserInfo(ChaoJiSouActivity.this).isAgencyStatus == 1) {
                                                jinfennum.setText(bean.zhuanMoney);
                                            } else {
                                                jinfennum.setVisibility(View.INVISIBLE);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                mLayoutManager = new LinearLayoutManager(ChaoJiSouActivity.this, LinearLayoutManager.VERTICAL, false);
                                mLayoutManager.setSmoothScrollbarEnabled(true);

                                rvSoufanli.addHeaderView(header);
                                rvSoufanli.setNestedScrollingEnabled(false);
                                rvSoufanli.setLayoutManager(mLayoutManager);
                                rvSoufanli.setAdapter(mAdapter);
                                mAdapter.setOnItemClick(ChaoJiSouActivity.this);

                            }
                        } else {

                        }
                        mAdapter.notifyDataSetChanged();
                        rvSoufanli.refreshComplete();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void initProgrem() {
        Network.getInstance().getApi(HomeApi.class)
                .soufanliProgramaImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<SoufanliProgremBean>(ChaoJiSouActivity.this) {
                    @Override
                    public void success(SoufanliProgremBean result) {
                        if (result == null) {
                            return;
                        }
                        final List<SoufanliProgremBean.SearchImgListBean> list = result.searchImgList;
                        List<String> imgList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) != null)
                                imgList.add(list.get(i).img);
                        }
                        //栏目1
                        if (imgList.get(0) != null)
                            Glide.with(ChaoJiSouActivity.this).load(imgList.get(0)).placeholder(R.drawable.cjs).into(ivProgram1);
                        //栏目2
                        if (imgList.get(1) != null)
                            Glide.with(ChaoJiSouActivity.this).load(imgList.get(1)).placeholder(R.drawable.cjs).into(ivProgram2);
                        //栏目3
                        if (imgList.get(2) != null)
                            Glide.with(ChaoJiSouActivity.this).load(imgList.get(2)).placeholder(R.drawable.cjs).into(ivProgram3);
                        //栏目4
                        if (imgList.get(3) != null)
                            Glide.with(ChaoJiSouActivity.this).load(imgList.get(3)).placeholder(R.drawable.cjs).into(ivProgram4);
                        ivProgram1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ChaoJiSouActivity.this, KindDetailActivity.class)
                                        .putExtra("type", list.get(0).programType).putExtra("name", list.get(0).name));
                            }
                        });
                        ivProgram2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ChaoJiSouActivity.this, KindDetailActivity.class)
                                        .putExtra("type", list.get(1).programType).putExtra("name", list.get(1).name));
                            }
                        });
                        ivProgram3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ChaoJiSouActivity.this, KindDetailActivity.class)
                                        .putExtra("type", list.get(2).programType).putExtra("name", list.get(2).name));
                            }
                        });
                        ivProgram4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ChaoJiSouActivity.this, KindDetailActivity.class)
                                        .putExtra("type", list.get(3).programType).putExtra("name", list.get(3).name));
                            }
                        });
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @Override
    public void itemClick(View v, int position) {
//        startActivity(new Intent(ChaoJiSouActivity.this, ProductDetailsActivity.class)
//                .putExtra(Constant.listInfo, JSON.toJSONString(mProductList.get(position))));
        findProductInfo(mProductList.get(position).alipayItemId,mProductList.get(position).id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top:
                rvSoufanli.scrollToPosition(0);
                break;
            case R.id.searchInfo:
                ZQZNActivity.launchActivity(ChaoJiSouActivity.this);
                break;
            case R.id.search:
            case R.id.et_content:
                //搜索内容
                SearchActivity.lanuchActivity(ChaoJiSouActivity.this, 1);
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
                .subscribe(new NetResultHandler<LiveProductInfoBean>(ChaoJiSouActivity.this) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        hideWaitDialog();
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(ChaoJiSouActivity.this, ProductDetailsActivity.class)
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
