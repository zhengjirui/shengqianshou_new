package com.lechuang.lqsq.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaoLiaoActivity;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.ChaoJiSouActivity;
import com.lechuang.lqsq.activity.EmptyWebActivity;
import com.lechuang.lqsq.activity.FenXiangZhuanQianActivity;
import com.lechuang.lqsq.activity.JuHuaSuan;
import com.lechuang.lqsq.activity.KindDetailActivity;
import com.lechuang.lqsq.activity.KindDetailActivity1;
import com.lechuang.lqsq.activity.LoginActivity;
import com.lechuang.lqsq.activity.NormalWebActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.activity.ProfitActivity;
import com.lechuang.lqsq.activity.SearchActivity;
import com.lechuang.lqsq.activity.SongHaoliActivity;
import com.lechuang.lqsq.activity.TQGActivity;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.adapter.MyAdapterNew;
import com.lechuang.lqsq.bean.EnvelopeActivityBean;
import com.lechuang.lqsq.bean.GetBean;
import com.lechuang.lqsq.bean.HomeBannerBean;
import com.lechuang.lqsq.bean.HomeGunDongTextBean;
import com.lechuang.lqsq.bean.HomeKindItemBean;
import com.lechuang.lqsq.bean.HomeLastProgramBean;
import com.lechuang.lqsq.bean.HomeProgramBean;
import com.lechuang.lqsq.bean.HomeTodayProductBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.PickRedEnvBean;
import com.lechuang.lqsq.bean.ProductDetailsBean;
import com.lechuang.lqsq.bean.ProgramSwitch;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.NetwordUtil;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.GetApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.DemoTradeCallback;
import com.lechuang.lqsq.utils.LogUtils;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.utils.TDevice;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.BannerView;
import com.lechuang.lqsq.widget.CustomTabLayout;
import com.lechuang.lqsq.widget.views.AutoTextView;
import com.lechuang.lqsq.widget.views.GridItemDecoration;
import com.lechuang.lqsq.widget.views.MGridView;
import com.lechuang.lqsq.widget.views.SpannelTextView;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/30
 * 时间：14:00
 * 描述：
 */

public class ShouYeFragment extends BaseFragment implements OnItemClick, ViewSwitcher.ViewFactory {
    private Observable<String> login;
    private Observable<String> logout;

    //首页分类
    Unbinder unbinder;
    //自动滚动的textview
    private AutoTextView tvAutoText;
    private ImageSwitcher ivAutoImg;
    private ImageView ivProgram1;
    private ImageView ivProgram2;
    private ImageView ivProgram3;
    private ImageView ivProgram4;
    private ImageView lastRollViewPager;
    private MGridView gvKind;
    private View lineHome;
    private TextView djsh, djsm, djss, djsh1, djsm1, djss1;
    // header view中的TabLayout
    private TabLayout tabHome;
    // fragment_home_tablebar中的TabLayout
    @BindView(R.id.tablayout_home_top)
    TabLayout tabHomeTop;
    //首页最下商品gridview
    @BindView(R.id.rv_home_table)
    XRecyclerView rvHome;
    @BindView(R.id.iv_top)
    ImageView ivTop;        //回到顶部
    @BindView(R.id.line_home_tab_top)
    View lineHomeTop;


    TextView tv_title;

    private View v;
    private ArrayList<String> text = null;
    //轮播图
    private List<HomeBannerBean.IndexBannerList> bannerList;
    public boolean isBottom = false;
    private View header;
    private BannerView bannerView;
    List<HomeTodayProductBean.productList> todaykill = new ArrayList();
    RecyclerView todayProduct;

    private LinearLayoutManager mLayoutManager;
    private long mTime;
    //分页
    private int page = 1;
    // 底部商品


    private ImageView zhucehaoli, fenxiangzhuanqian;
    private CountDownTimer countDownTimer, countDownTimer1;
    private View zfprogram;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_tablebar;
    }

    private TranslateAnimation createAnim(float fromXDelta, float toXDelta,
                                          float fromYDelta, float toYDelta) {
        final TranslateAnimation rotation = new TranslateAnimation(fromXDelta,
                toXDelta, fromYDelta, toYDelta);
        rotation.setDuration(600);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);

        // TODO init XRecyclerView
        header = LayoutInflater.from(getActivity()).inflate(R.layout.header_home_tablebar,
                (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        djsh = (TextView) header.findViewById(R.id.djsh);
        djsm = (TextView) header.findViewById(R.id.djsm);
        djss = (TextView) header.findViewById(R.id.djss);
        djsh1 = (TextView) header.findViewById(R.id.djsh1);
        djsm1 = (TextView) header.findViewById(R.id.djsm1);
        djss1 = (TextView) header.findViewById(R.id.djss1);
        //轮播图
        todayProduct = (RecyclerView) header.findViewById(R.id.todayProduct);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        bannerView = (BannerView) header.findViewById(R.id.rv_banner);
        bannerView.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                int[] location = new int[2];

                String url = bannerList.get(position).link;

                //获取到点击条目
                int programaId = bannerList.get(position).programaId;
                if (programaId == 1) {
                    ivProgram1.getLocationInWindow(location);
                    rvHome.scrollBy(0, location[1] - findView(R.id.home_title_bar).getHeight() - TDevice.getStatuBarHeight());
                    //栏目1
                                   /* startActivity(new Intent(getActivity(), ProgramDetailActivity.class)
                                            .putExtra("programaId", 1));*/
                } else if (programaId == 2) {
                    ivProgram2.getLocationInWindow(location);
                    rvHome.scrollBy(0, location[1] - findView(R.id.home_title_bar).getHeight() - TDevice.getStatuBarHeight());
                    //栏目2
                                    /*startActivity(new Intent(getActivity(), ProgramDetailActivity.class)
                                            .putExtra("programaId", 2));*/
                } else if (programaId == 3) {
                    ivProgram3.getLocationInWindow(location);
                    rvHome.scrollBy(0, location[1] - findView(R.id.home_title_bar).getHeight() - TDevice.getStatuBarHeight());
                    //栏目3
                                    /*startActivity(new Intent(getActivity(), ProgramDetailActivity.class)
                                            .putExtra("programaId", 3));*/
                } else if (programaId == 4) {
                    ivProgram4.getLocationInWindow(location);
                    rvHome.scrollBy(0, location[1] - findView(R.id.home_title_bar).getHeight() - TDevice.getStatuBarHeight());
                    //栏目4
//                                    startActivity(new Intent(getActivity(), ProgramDetailActivity.class)
//                                            .putExtra("programaId", 4));
                } else if (StringUtils.isDetails(url)) {
                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("isUrl", true);
                    intent.putExtra("url", url);
                    startActivity(intent);

                } else if (StringUtils.isTaobaoOrTmall(url)) {
                    AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                    AlibcTaokeParams taoke = new AlibcTaokeParams(Constant.PID, "", "");
                    Map<String, String> exParams = new HashMap<String, String>();
                    exParams.put("isv_code", "appisvcode");
                    exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                    AlibcBasePage alibcBasePage = new AlibcPage(bannerList.get(position).link);
                    //AlibcTrade.show(ProductDetailsActivity.this, alibcBasePage, alibcShowParams, null, exParams , new DemoTradeCallback());

                    //添加购物车
                    //AlibcBasePage alibcBasePage = new AlibcAddCartPage(getIntent().getStringExtra("alipayItemId"));
                    AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, taoke, exParams, new DemoTradeCallback());
                } else {
                    try {
                        if (TextUtils.isEmpty(bannerList.get(position).link))
                            return;
                        NormalWebActivity.lanuchActivity(getActivity(), "", bannerList.get(position).link);
                    } catch (Exception e) {

                    }
                }
            }
        });
        tabHome = (TabLayout) header.findViewById(R.id.tablayout_home);
        tvAutoText = (AutoTextView) header.findViewById(R.id.tv_auto_text);
        ivAutoImg = (ImageSwitcher) header.findViewById(R.id.auto_img);
        ivAutoImg.setInAnimation(createAnim(0, 0, 90, 0));
        ivAutoImg.setOutAnimation(createAnim(0, 0, 0, -90));
        ivAutoImg.setFactory(this);
        ivProgram1 = (ImageView) header.findViewById(R.id.iv_program1);
        ivProgram2 = (ImageView) header.findViewById(R.id.iv_program2);
        ivProgram3 = (ImageView) header.findViewById(R.id.iv_program3);
        ivProgram4 = (ImageView) header.findViewById(R.id.iv_program4);
        zhucehaoli = (ImageView) header.findViewById(R.id.zhucehaoli);
        fenxiangzhuanqian = (ImageView) header.findViewById(R.id.fenxiangzhuangqian);
        zfprogram = header.findViewById(R.id.zfprogram);
        zhucehaoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserHelper.isLogin()){
                    LoginActivity.launchActivity(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), SongHaoliActivity.class));
            }
        });
        fenxiangzhuanqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserHelper.isLogin()){
                    LoginActivity.launchActivity(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), FenXiangZhuanQianActivity.class));
            }
        });
        lastRollViewPager = (ImageView) header.findViewById(R.id.lastRollViewPager);
        gvKind = (MGridView) header.findViewById(R.id.gv_kind);
        lineHome = (View) header.findViewById(R.id.line_home_tab);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        todayProduct.setLayoutManager(layoutManager);
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] loc = new int[2];
                tabHome.getLocationInWindow(loc);

                if (tabHomeTop.getHeight() >= loc[1]) {
                    tabHome.setVisibility(View.INVISIBLE);
                    lineHome.setVisibility(View.INVISIBLE);
                    tabHomeTop.setVisibility(View.VISIBLE);
                    lineHomeTop.setVisibility(View.VISIBLE);
                    ivTop.setVisibility(View.VISIBLE);
                } else {
                    tabHome.setVisibility(View.VISIBLE);
                    lineHome.setVisibility(View.VISIBLE);
                    tabHomeTop.setVisibility(View.INVISIBLE);
                    lineHomeTop.setVisibility(View.INVISIBLE);
                    ivTop.setVisibility(View.GONE);
                }

                if (mProductList.size() - mLayoutManager.findLastVisibleItemPosition() < 5) {
                    if (System.currentTimeMillis() - mTime > 1000 && !isBottom) {
                        page += 1;
                        getProductList();
                    }
                }
            }
        });

        rvHome.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isBottom = false;
                page = 1;
                getData(true);

            }

            @Override
            public void onLoadMore() {
                page += 1;
                getProductList();
//                mAdapter.notifyDataSetChanged();
//                rvHome.refreshComplete();
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        regiUserState();
        getData(true);
    }


    //网络获取数据
    public void getData(boolean isLoadTabData) {
        if (Utils.isNetworkAvailable(getActivity())) {
            if (isLoadTabData)
                //获取首页轮播图数据
                getHomeBannerData();
            //首页分类数据
            getHomeKindData();
            getTodayProduct();
            //首页滚动文字数据
            getHomeScrollTextView();
            //首页4个图片栏目数据
            getHomeProgram();
            // tab数据
            if (isLoadTabData)
                getTabData();
            // 商品数据(默认'全部')
            mRootId = 0;
            getProductList();
            getProgramSwitch();

            //红包
            if (UserHelper.isLogin()) {
                getHongbao();
            }


        } else {
            //隐藏加载框
            ToastManager.getInstance().showShortToast(getString(R.string.net_error));
        }
    }

    private void getProgramSwitch() {
        Network.getInstance().getApi(HomeApi.class)
                .getProgramSwitch()
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<ProgramSwitch>() {
                    @Override
                    protected void success(ProgramSwitch data) {
                        if (data == null || data.allSwitchInfo == null || data.allSwitchInfo.isEmpty())
                            return;
                        for (int i = 0; i < data.allSwitchInfo.size(); i++) {
                            if (data.allSwitchInfo.get(i).type.equals("1") && data.allSwitchInfo.get(i).state.equals("0")) {
                                zfprogram.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    //获取首页轮播图数据
    private void getHomeBannerData() {
        //首页轮播图数据
        Network.getInstance().getApi(HomeApi.class)
                .homeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeBannerBean>(ShouYeFragment.this) {

                    @Override
                    public void success(final HomeBannerBean result) {
                        if (result == null) {
                            return;
                        }
                        bannerList = result.indexBannerList0;
                        List<String> imgList = new ArrayList<>();
                        for (int i = 0; i < bannerList.size(); i++) {
                            imgList.add(bannerList.get(i).img);
                        }
                        bannerView.bindData(imgList, 3000);

                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private int[] homeKindImg = {R.drawable.jingdong, R.drawable.weipinhui, R.drawable.baoliao,
            R.drawable.icon_chaojisou, R.drawable.yiyuan};
    private String[] homeKindName = {"京东", "唯品会", "小编说", "超级搜", "1元试用"};
    private List<HomeKindItemBean> homeKindList = new ArrayList<>();

    //获取首页分类数据
    private void getHomeKindData() {
        if (homeKindList != null) {
            homeKindList.clear();
        }
        for (int i = 0; i < homeKindImg.length; i++) {
            HomeKindItemBean bean = new HomeKindItemBean();
            bean.img = homeKindImg[i];
            bean.name = homeKindName[i];
            homeKindList.add(bean);
        }
        gvKind.setAdapter(new CommonAdapter<HomeKindItemBean>(homeKindList, getActivity(), R.layout.home_kinds_item) {
            @Override
            public void setData(CommonViewHolder viewHolder, Object item) {
                HomeKindItemBean bean = (HomeKindItemBean) item;
                //分类名称
                viewHolder.setText(R.id.tv_kinds_name, bean.name);
                //分类图片
                viewHolder.setImageResource(R.id.iv_kinds_img, bean.img);
            }
        });
        // TODO: 2017/10/2  分类item
        gvKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("click", "click");
                switch (position) {
                    case 0:  //京东
//                        startActivity(new Intent(getActivity(), KindDetailActivity.class)
//                                .putExtra("type", 12).putExtra("name", "聚划算"));
                        NormalWebActivity.lanuchActivity(getActivity(), "京东", getString(R.string.jd));
                        break;
                    case 1:  //唯品会
//                        startActivity(new Intent(getActivity(), KindDetailActivity.class)
//                                .putExtra("type", 9).putExtra("name", "今日新品"));
                        NormalWebActivity.lanuchActivity(getActivity(), "唯品会", getString(R.string.wph));
                        break;
                    case 2:  //小编说
                        startActivity(new Intent(getActivity(), BaoLiaoActivity.class));
                        break;
                    case 3:  //超级搜
//                        ZBActivity.launchActivity(getActivity());
                        ChaoJiSouActivity.launchActivity(getActivity());
                        break;
                    case 4:  //1元试用KindDetailActivity
//                        startActivity(new Intent(getActivity(), GetMoneyActivity.class));
                        startActivity(new Intent(getActivity(), KindDetailActivity.class)
                                .putExtra("type", 3).putExtra("name", "1元试用"));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //每日必杀
    private void getTodayProduct() {
        Network.getInstance().getApi(HomeApi.class)
                .homeTodayProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeTodayProductBean.ProudList>(ShouYeFragment.this) {
                    @Override
                    public void success(HomeTodayProductBean.ProudList result) {
                        if (result == null)
                            return;
                        tv_title.setText(result.programName);
                        todaykill = result.productList;
                        todayProduct.setAdapter(new MyAdapterNew(result.adProductList, new MyAdapterNew.RecycleViewListener() {
                            @Override
                            public void itemClik(HomeTodayProductBean.adProductList listBean) {
//                                startActivity(new Intent(getActivity(), ProductDetailsActivity.class
//                                ).putExtra(Constant.listInfo, JSON.toJSONString(listBean)));
                                findProductInfo(listBean.alipayItemId,listBean.id);
                            }
                        }));
                        final int miao = getMiao();
                        countDownTimer = new CountDownTimer(miao * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long l = millisUntilFinished / 1000;
                                long l1 = l / 3600;
                                long l2 = l % 3600 / 60;
                                long l3 = l % 60;
                                String s1 = l1 > 9 ? l1 + "" : "0" + l1;
                                String s2 = l2 > 9 ? l2 + "" : "0" + l2;
                                String s3 = l3 > 9 ? l3 + "" : "0" + l3;
                                djsh.setText(s1);
                                djsm.setText(s2);
                                djss.setText(s3);
                            }

                            @Override
                            public void onFinish() {
                                getTodayProduct();
                            }
                        };
                        countDownTimer.start();
//
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                });
    }

    //首页滚动文字数据
    private void getHomeScrollTextView() {
        Network.getInstance().getApi(HomeApi.class)
                .gunDongText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeGunDongTextBean>(ShouYeFragment.this) {
                    @Override
                    public void success(final HomeGunDongTextBean result) {
                        if (result == null) {
                            return;
                        }
                        final List<HomeGunDongTextBean.IndexMsgListBean> list = result.indexMsgList;
                        //滚动TextView
                        text = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            text.add(list.get(i).productName + "<br>" + list.get(i).productText);
                        }
                        //自定义的滚动textview
                        tvAutoText.setTextAuto(text);
                        //设置时间
                        tvAutoText.setGap(3000);
                        tvAutoText.setOnItemClickListener(new AutoTextView.onItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
//                                startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                        .putExtra(Constant.listInfo, JSON.toJSONString(list.get(position))));
                                findProductInfo(list.get(position).alipayItemId,list.get(position).id);
                            }
                        });
                        final Drawable[] drawables = new Drawable[result.indexMsgList.size()];

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < drawables.length; i++) {
                                        Bitmap bitmap = Glide.with(getActivity()).load(result.indexMsgList.get(i).img).asBitmap().into(500, 500).get();
                                        drawables[i] = new BitmapDrawable(bitmap);
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }).start();


                        try {
                            tvAutoText.setOnSwitchListener(new AutoTextView.onSwitchListener() {
                                @Override
                                public void onSwitch(int position) {
//                                    Glide.with(getActivity()).load(result.indexMsgList.get(position).img).into(ivAutoImg);
                                    position--;
                                    if (position < 0) position = result.indexMsgList.size() - 1;
                                    if (drawables[position] != null)
                                        ivAutoImg.setImageDrawable(drawables[position]);
                                }
                            });

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    //首页4个图片栏目数据
    private void getHomeProgram() {
        Network.getInstance().getApi(HomeApi.class)
                .homeProgramaImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeProgramBean>(ShouYeFragment.this) {
                    @Override
                    public void success(final HomeProgramBean result) {
                        if (result == null) {
                            return;
                        }
                        final List<HomeProgramBean.ListBean> list = result.programaImgList;
                        final List<String> imgList = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) != null)
                                imgList.add(list.get(i).img);
                        }
                        //栏目1
                        if (imgList.get(0) != null)
                            Glide.with(getActivity()).load(imgList.get(0)).placeholder(getResources().getDrawable(R.drawable.ming)).into(ivProgram1);
                        //栏目2
                        if (imgList.get(1) != null)
                            Glide.with(getActivity()).load(imgList.get(1)).placeholder(getResources().getDrawable(R.drawable.lingdian)).into(ivProgram2);
                        //栏目3
                        if (imgList.get(2) != null)
                            Glide.with(getActivity()).load(imgList.get(2)).placeholder(getResources().getDrawable(R.drawable.yi)).into(ivProgram3);

                        //栏目4
                        if (imgList.get(3) != null)
                            Glide.with(getActivity()).load(imgList.get(3)).placeholder(getResources().getDrawable(R.drawable.yi)).into(ivProgram4);
                        ivProgram1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int type = result.programaImgList.get(0).programaId;
                                String name = result.programaImgList.get(0).pname;
                                if (name.equals("聚划算")) {
                                    startActivity(new Intent(getActivity(), JuHuaSuan.class));
                                } else if (name.equals("淘抢购")) {
                                    startActivity(new Intent(getActivity(), TQGActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), KindDetailActivity1.class)
                                            .putExtra("programaId", type)
                                            .putExtra("name", name));
                                }

                            }
                        });
                        ivProgram2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int type = result.programaImgList.get(1).programaId;
                                String name = result.programaImgList.get(1).pname;
                                if (name.equals("聚划算")) {
                                    startActivity(new Intent(getActivity(), JuHuaSuan.class));
                                } else if (name.equals("淘抢购")) {
                                    startActivity(new Intent(getActivity(), TQGActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), KindDetailActivity1.class)
                                            .putExtra("programaId", type)
                                            .putExtra("name", name));
                                }

                            }
                        });
                        ivProgram3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int type = result.programaImgList.get(2).programaId;
                                String name = result.programaImgList.get(2).pname;
                                if (name.equals("聚划算")) {
                                    startActivity(new Intent(getActivity(), JuHuaSuan.class));
                                } else if (name.equals("淘抢购")) {
                                    startActivity(new Intent(getActivity(), TQGActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), KindDetailActivity1.class)
                                            .putExtra("programaId", type)
                                            .putExtra("name", name));
                                }
                            }
                        });
                        ivProgram4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int id = result.programaImgList.get(3).programaId;
                                String name = result.programaImgList.get(3).pname;
                                if (name.equals("聚划算")) {
                                    startActivity(new Intent(getActivity(), JuHuaSuan.class));
                                } else if (name.equals("淘抢购")) {
                                    startActivity(new Intent(getActivity(), TQGActivity.class));
                                } else {
                                    startActivity(new Intent(getActivity(), KindDetailActivity1.class)
                                            .putExtra("programaId", id)
                                            .putExtra("name", name));
                                }
                            }
                        });
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    // viewpage标题
    private List<GetBean.TopTab> topTabList = new ArrayList<>();
    boolean isLoadingTabData = false;
    boolean isLoaddownTabData = false;

    private void getTabData() {
        if (isLoadingTabData) return;
        if (isLoadingTabData) return;
        isLoadingTabData = true;
        if (tabHome != null && tabHome.getTabCount() > 0) {
            topTabList.clear();
            tabHome.removeAllTabs();
        }

        if (Utils.isNetworkAvailable(getActivity())) {
            Network.getInstance().getApi(GetApi.class)
                    .topTabList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<GetBean>(ShouYeFragment.this) {
                        @Override
                        public void success(GetBean result) {
                            if (result == null) return;
                            GetBean.TopTab topTab = new GetBean.TopTab();
                            topTab.rootId = 0;
                            topTab.rootName = "全部";
                            topTabList.add(topTab);
                            List<GetBean.TopTab> tabList = result.tbclassTypeList;
                            if (tabList != null) {
                                topTabList.addAll(tabList);
                            }

                            for (GetBean.TopTab tab : topTabList) {
                                tabHome.addTab(tabHome.newTab().setText(tab.rootName));
                            }

                            for (GetBean.TopTab tab : topTabList) {
                                tabHomeTop.addTab(tabHomeTop.newTab().setText(tab.rootName));
                            }

                            CustomTabLayout.reflex(tabHome);
                            CustomTabLayout.reflex(tabHomeTop);
                            // 为Tab添加响应
                            // 切换Tab时,两个TabLayout都需要保存当前选中位置
                            onSelectTabItem();
                            onSelectTopTabItem();
                            isLoaddownTabData = true;
                        }

                        @Override
                        public void error(int code, String msg) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            isLoadingTabData = false;
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            isLoadingTabData = false;
                        }
                    });
        } else {

        }
    }

    /**
     * TabLayout事件监听
     */
    private void onSelectTabItem() {
        //获取Tab的数量
        int tabCount = tabHome.getTabCount();
        for (int position = 0; position < tabCount; position++) {
            TabLayout.Tab tab = tabHome.getTabAt(position);
            if (tab == null) {
                continue;
            }
            // 这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //c.getDeclaredField 获取私有属性。
                //“mView”是Tab的私有属性名称，类型是 TabView ，TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    continue;
                }
                final int item = position;
                view.setTag(item);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 清空网络请求连接池
                        Network.getInstance().getOkHttpClient().connectionPool().evictAll();
                        tabHomeTop.setScrollPosition(item, 0, true);
                        // TabLayout child 事件处理
                        mRootId = topTabList.get(item).rootId;
                        page = 1;
                        getProductList();
                        rvHome.scrollToPosition(2);
                    }
                });

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TabLayout事件监听
     */
    private void onSelectTopTabItem() {
        //获取Tab的数量
        int tabCount = tabHomeTop.getTabCount();
        for (int position = 0; position < tabCount; position++) {
            TabLayout.Tab tab = tabHomeTop.getTabAt(position);
            if (tab == null) {
                continue;
            }
            // 这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //c.getDeclaredField 获取私有属性。
                //“mView”是Tab的私有属性名称，类型是 TabView ，TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    continue;
                }
                final int item = position;
                view.setTag(item);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 清空网络请求连接池
                        Network.getInstance().getOkHttpClient().connectionPool().evictAll();
                        tabHome.setScrollPosition(item, 0, true);
                        // TabLayout child 事件处理
                        mRootId = topTabList.get(item).rootId;
                        page = 1;
                        getProductList();
                        rvHome.scrollToPosition(2);
                    }
                });

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // 设置Adapter
    private CommonRecyclerAdapter mAdapter;
    // 底部商品
    private ArrayList<HomeLastProgramBean.ListBean> mProductList = new ArrayList<>();
    // 当前分类ID
    private int mRootId = 0;

    /**
     * 底部商品数据
     */
    private void getProductList() {
        mTime = System.currentTimeMillis();
        HashMap<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("classTypeId", mRootId + "");
//        if (mRootId != 0) {  //Integer classTypeId   分类id,精选不传
//            map.put("classTypeId", mRootId + "");
//        } else {             //Integer is_official   精选传1,其他不传
//            map.put("type", "");
//        }

        subscription = Network.getInstance().getApi(HomeApi.class)
                .homeLastProgram(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HomeLastProgramBean>(ShouYeFragment.this) {
                    @Override
                    protected void success(HomeLastProgramBean result) {
                        if (result == null) return;
                        if (page == 1 && mProductList != null) {
                            mProductList.clear();
                        }
                        List<HomeLastProgramBean.ListBean> list = result.productList;
                        if (page > 1 && list.isEmpty()) {
//                            Utils.show(getActivity(), "亲,已经到底了~");
                            rvHome.noMoreLoading();
                            isBottom = true;
                            return;
                        }
                        mProductList.addAll(list);
                        if (page == 1) {
                            //轮播图图片数据集合
                            final List<HomeLastProgramBean.programaBean.ListBean> list1 = result.programa.indexBannerList;
                            Glide.with(getActivity()).load(list1.get(0).img).into(lastRollViewPager);
                            lastRollViewPager.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(list1.get(0).link)) {
//                                        startActivity(new Intent(getActivity(), EmptyWebActivity.class)
//                                                .putExtra("url", list1.get(0).link));
                                    }

                                }
                            });
//                            mAdapter = new CustomHomeAdapter(mProductList);
//                            rvHome.addItemDecoration(new GridSpacingItemDecoration(9, 8, false));
//                            rvHome.setAdapter(mAdapter);
//                            mAdapter.setOnItemClick(HomeTableFragment.this);
                            if (mAdapter == null) {
                                mAdapter = new CommonRecyclerAdapter<HomeLastProgramBean.ListBean>(getActivity(), R.layout.item_last_program1, mProductList) {
                                    @Override
                                    public void convert(ViewHolderRecycler holder, HomeLastProgramBean.ListBean data) {
                                        try {
                                            HomeLastProgramBean.ListBean bean = (HomeLastProgramBean.ListBean) data;
                                            //商品图
//                                            Glide.with(mContext).load(bean.imgs).into((ImageView) holder.getView(R.id.iv_img));
                                            holder.displayImage(R.id.iv_img, bean.imgs, R.drawable.ic_home_default);
                                            //动态调整滑动时的内存占用
                                            Glide.get(mContext).setMemoryCategory(MemoryCategory.LOW);
                                            // 原价
                                            TextView tv_xiaoliang = holder.getView(R.id.tv_xiaoliang);
                                            TextView jinfennum = holder.getView(R.id.jifen_num);
                                            SpannelTextView spannelTextView = holder.getView(R.id.spannelTextView);
                                            spannelTextView.setDrawText(bean.name);
                                            spannelTextView.setShopType(bean.shopType.equals("1") ? 1 : 2);
                                            tv_xiaoliang.setText("销量：" + bean.nowNumber);

                                            // 领券减
                                            TextView tvCoupon = holder.getView(R.id.tv_quanMoney);
                                            tvCoupon.setText("优惠券：" + bean.couponMoney + "元");
                                            TextView tvOldPrice = holder.getView(R.id.tv_oldprice);
                                            tvOldPrice.setText(bean.price + "");
                                            // 券后价
                                            holder.setText(R.id.tv_nowprice, "" + bean.preferentialPrice);
                                            if (UserHelper.isLogin() && UserHelper.getUserInfo(getActivity()).isAgencyStatus == 1) {
                                                jinfennum.setText(bean.zhuanMoney);
                                            } else {
                                                jinfennum.setVisibility(View.INVISIBLE);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };


                                mLayoutManager = new LinearLayoutManager(getContext());
                                mLayoutManager.setSmoothScrollbarEnabled(true);

                                rvHome.addItemDecoration(new GridItemDecoration(
                                        new GridItemDecoration.Builder(getActivity())
                                                .isExistHead(true)
                                                .margin(8, 8)
                                                .horSize(10)
                                                .verSize(10)
                                                .showLastDivider(true)
                                ));

                                rvHome.addHeaderView(header);
                                rvHome.setNestedScrollingEnabled(false);
                                rvHome.setLayoutManager(mLayoutManager);
                                rvHome.setAdapter(mAdapter);
                                mAdapter.setOnItemClick(ShouYeFragment.this);
                            }
                        } else {
//                            mAdapter.notifyDataSetChanged();
//                            rvHome.refreshComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                        rvHome.refreshComplete();
                        productListCountTime();

                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private long lastTime = 0;

    private void productListCountTime() {
        final int miao = getMiao();
        int miao1 = miao % (15 * 60);
        if (miao1 < 10) {
            miao1 += 900;
        }
        if (countDownTimer1 != null) {
            countDownTimer1.cancel();
        }
        countDownTimer1 = new CountDownTimer(miao1 * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > lastTime && lastTime > 0) return;
                lastTime = millisUntilFinished;
                long l = millisUntilFinished / 1000;
                long l1 = l / 60;
                long l2 = l % 60;
                long l3 = millisUntilFinished % 1000 / 10;
                String s1 = l1 > 9 ? l1 + "" : "0" + l1;
                String s2 = l2 > 9 ? l2 + "" : "0" + l2;
                String s3 = l3 > 9 ? l3 + "" : "0" + l3;
                djsh1.setText(s1);
                djsm1.setText(s2);
                djss1.setText(s3);
            }

            @Override
            public void onFinish() {
                getProductList();
                djss1.setText("00");
                lastTime = 0;
            }
        };
        countDownTimer1.start();
    }

    private void regiUserState() {
        login = RxBus.getDefault().register(Constant.login_success, String.class);
        login.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        rvHome.scrollToPosition(0);
                        ivTop.setVisibility(View.GONE);
                        getData(false);
                    }
                });
        logout = RxBus.getDefault().register(Constant.logout, String.class);
        logout.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        rvHome.scrollToPosition(0);
                        ivTop.setVisibility(View.GONE);
                        getData(false);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (countDownTimer1 != null) {
            countDownTimer1.cancel();
        }
        RxBus.getDefault().unregister(Constant.login_success, login);
        RxBus.getDefault().unregister(Constant.logout, logout);
        super.onDestroyView();
    }

    @OnClick({R.id.tv_search, R.id.iv_news, R.id.iv_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                SearchActivity.lanuchActivity(getActivity(), 1);
                break;
            //签到
            case R.id.iv_news:

                if (UserHelper.isLogin())
                    RxBus.getDefault().post(Constant.qiandao, "");
                else
                    LoginActivity.launchActivity(getActivity());
                break;
            //底部跳转到顶部的按钮
            case R.id.iv_top:
                // 回到顶部
                rvHome.scrollToPosition(0);
                ivTop.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void itemClick(View v, int position) {
//        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                .putExtra(Constant.listInfo, JSON.toJSONString(mProductList.get(position))));
        findProductInfo(mProductList.get(position).alipayItemId,mProductList.get(position).id);
    }

    public int getMiao() {
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (int) (tommorowDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
    }

    @Override
    public View makeView() {
        ImageView i = new ImageView(getContext());
        i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return i;
    }

    /**
     * 活动是否已经显示过了<br>
     * 打开一次app，最多只会显示一次
     */
    private boolean isHuodongShowed;

    /**
     * 获取红包信息
     */
    private void getHongbao() {
        Network.getInstance().getApi(HomeApi.class)
                .envelopeactivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<EnvelopeActivityBean>() {
                    @Override
                    protected void success(EnvelopeActivityBean result) {
                        if (result == null || result.activityDesc == null) {
                            return;
                        }
                        EnvelopeActivityBean.ActivityDesc ad = result.activityDesc;
                        if (ad.status == 0 || isHuodongShowed && (ad.status == 1 || ad.status == 3)
                                || ad.status == 2 && ad.appuserId != null) {
                            return;
                        }
                        if (dialog != null && dialog.isShowing()) {
                            return;
                        }
                        DrawableTypeRequest<String> dtr = Glide.with(getActivity()).load(ad.activeImageUrl);
                        if (ad.status == 1) {
                            Uri uri = Uri.parse(ad.activeUrl);
                            dtr.into(new HuodongTarget(uri));
                        } else if (ad.status == 2) {
                            dtr.into(new HuodongTarget());
                        } else if (ad.status == 3) {
                            dtr.into(new HuodongTarget(ad.alipayItemId));
                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private Dialog dialog;

    class HuodongTarget extends SimpleTarget<GlideDrawable> {
        @Nullable
        private Uri huodongUri;
        /**
         * 是否是商品链接
         */
        @Nullable
        private String productID;
        private ImageView imageView;
        private TextView hongbao_money;
        private TextView hongbao_explain;

        /**
         * 领红包时，点击页面的单击事件
         */
        private Runnable hongbaoRunnable;

        public HuodongTarget(@Nullable Uri huodongUri) {
            this.huodongUri = huodongUri;
        }

        public HuodongTarget(@Nullable String productID) {
            this.productID = productID;
        }

        public HuodongTarget() {
            super();
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_hongbao);
            hongbao_money = (TextView) dialog.findViewById(R.id.hongbao_money);
            hongbao_money.setText("");
            hongbao_explain = (TextView) dialog.findViewById(R.id.hongbao_explain);
            hongbao_explain.setText("");
            imageView = (ImageView) dialog.findViewById(R.id.hongbao_background);
            imageView.setImageDrawable(resource);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //活动
                    if (huodongUri != null) {
//                    if (Utils.isTaobaoUri(huodongUri)) {
//                        Utils.print("这里是跳转到淘宝的活动");
//                    } else {
//                        Utils.print("这里是跳转到其他的活动");
//                    }
                        startActivity(new Intent(getActivity(), EmptyWebActivity.class)
                                .putExtra("url", huodongUri.toString()));
                        dialog.onBackPressed();
                        isHuodongShowed = true;
                    } else if (productID != null) {
                        showWaitDialog("");
                        //商品链接
                        //先查询商品信息
                        NetwordUtil.queryProductDetails(productID, new NetResultHandler<ProductDetailsBean>() {
                            @Override
                            protected void success(ProductDetailsBean data) {
                                hideWaitDialog();
//                                startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                        .putExtra(Constant.listInfo, JSON.toJSONString(data.productWithBLOBs)));
                                findProductInfo(data.productWithBLOBs.alipayItemId,data.productWithBLOBs.id);
                                dialog.onBackPressed();
                            }

                            @Override
                            public void error(int code, String msg) {
                                hideWaitDialog();
                            }
                        });
                        isHuodongShowed = true;
                    } else {
                        if (hongbaoRunnable != null) {
                            hongbaoRunnable.run();
                        } else {

                            if (!UserHelper.isLogin()) {
                                return;
                            }
                            //领取红包
                            Network.getInstance().getApi(HomeApi.class)
                                    .pickRedEnv()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new NetResultHandler<PickRedEnvBean>() {
                                        @Override
                                        protected void success(PickRedEnvBean result) {
                                            if (result != null && result.isSuccess()) {
                                                imageView.setImageResource(R.mipmap.hongbao_suc);
                                                hongbao_money.setText(result.randomRedEnvelope.amountStr);
                                                hongbao_explain.setText(result.randomRedEnvelope.envelopeDescription);
//                                            hongbaoRunnable = () -> {
//                                                startActivity(new Intent(getActivity(), ProfitActivity.class));
//                                                dialog.onBackPressed();
//                                            };
                                                hongbaoRunnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(getActivity(), ProfitActivity.class));
                                                        dialog.onBackPressed();
                                                    }
                                                };
                                            } else {
                                                imageView.setImageResource(R.mipmap.hongbao_fail);
//                                            hongbaoRunnable = () -> dialog.onBackPressed();
                                                hongbaoRunnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.onBackPressed();
                                                    }
                                                };
                                            }
                                        }

                                        @Override
                                        public void error(int code, String msg) {

                                        }

                                    });
                        }
                    }
                }
            });
//            dialog.findViewById(R.id.close).setOnClickListener(v2 -> dialog.onBackPressed());
            dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.onBackPressed();

                }
            });
            dialog.show();

            Window mWindow = dialog.getWindow();
            if (mWindow != null) {
                WindowManager.LayoutParams wl = mWindow.getAttributes();
                wl.width = -1;
                wl.height = -1;
                mWindow.setAttributes(wl);
                mWindow.setBackgroundDrawable(null);
            }
            hongbaoRunnable = null;
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
