package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.GetHostUrlBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.TipoffDetail;
import com.lechuang.lqsq.bean.TipoffListBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.TipoffShowApi;
import com.lechuang.lqsq.widget.dialog.DialogAlertView;
import com.lechuang.lqsq.widget.views.MListView;
import com.lechuang.lqsq.widget.views.XCRoundImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：li on 2017/9/22 15:17
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class StoryDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_author_head)
    XCRoundImageView ivAuthorHead;
    @BindView(R.id.tv_author_name)
    TextView tvAuthorName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_see_number)
    TextView tvSeeNumber;
    @BindView(R.id.iv_dianzan)
    ImageView ivDianzan;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_number_dianzan)
    TextView tvNumberDianzan;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.seeAll)
    TextView seeAll;
    @BindView(R.id.lv_comment)
    MListView lvComment;
    @BindView(R.id.iv_product)
    ImageView iv_product;
    @BindView(R.id.mScrollView)
    PullToRefreshScrollView mScrollView;
    @BindView(R.id.tv_title_story)
    TextView tvTitleStory;
    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerlayout;   //侧滑菜单
    @BindView(R.id.ll_chouti)
    LinearLayout llChouti;       //侧滑布局
    @BindView(R.id.lv_chouti)
    MListView lvChouti;          //侧滑的listview


    private boolean flag = true;
    private TipoffDetail.TipOffBean tipOff;
    //private List<TipoffDetail.ProductListBean> product;
    private Context mContext = this;
    private String id;
    private List<TipoffListBean.AppraiseListBean> appraiseList;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout_story_detail);
        findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.titleName);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("小编说");
        ButterKnife.bind(this);
        mScrollView.setOnRefreshListener(refresh);
        initView();
        getData();

    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    private void initView() {
        //去除侧滑菜单出来时的阴影
        //drawerlayout.setScrimColor(Color.TRANSPARENT);
        //侧滑点击监听
        drawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //设置ture点击侧滑菜单内容区域不会响应
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        iv_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.openDrawer(llChouti);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout.closeDrawer(llChouti);
            }
        });
    }

    @OnClick({R.id.iv_dianzan, R.id.seeAll, R.id.back, R.id.add_comment,R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*case R.id.ll_good: //商品详情

            case R.id.iv_cart: //去购买
            case R.id.tv_gobuy://去购买
                startActivity(new Intent(this, ProductDetailsActivity.class)
                        .putExtra("t", 2)
                        .putExtra(Constants.listInfo, JSON.toJSONString(product)));
                break;*/
            case R.id.iv_dianzan://点赞// ;
                changeVeiw();
                tipHelp(id, 1);
                break;
            case R.id.seeAll://查看全部
                //传入一个id入参,whichComment作用判断哪个界面的评论详情 1:爆料 2:晒单
                startActivity(new Intent(mContext, TipOffCommentDetailsActivity.class).putExtra("tipOffId", tipOff.id).putExtra("whichComment", 1));
                break;

            case R.id.back://返回
                finish();
                break;
            case R.id.add_comment:
                startActivity(new Intent(this, AddCommentActivity.class)
                        .putExtra("tipId", id).putExtra("type", 1));
                break;
            case R.id.iv_right:
                getShareProductUrl();
                break;
            default:
                break;
        }
    }

    private String appHost;
    private void share() {
        DialogAlertView dialogAlertView =null;
        if (dialogAlertView == null){
            dialogAlertView = new DialogAlertView(Gravity.BOTTOM, this);
            dialogAlertView.setView(R.layout.show_share_dialog);
            dialogAlertView.findViewById(R.id.share_weixin).setOnClickListener(this);
            dialogAlertView.findViewById(R.id.share_friends).setOnClickListener(this);
            dialogAlertView.findViewById(R.id.share_qq).setOnClickListener(this);
            dialogAlertView.findViewById(R.id.share_qq_kongjian).setOnClickListener(this);
        }

        if (dialogAlertView.isShowing()){
            dialogAlertView.dismiss();
        }else {
            dialogAlertView.show();
        }
    }

    private void getShareProductUrl(){

        Network.getInstance().getApi(CommenApi.class)
                .getShareProductUrl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<GetHostUrlBean>() {
                    @Override
                    public void success(GetHostUrlBean result) {
                        CacheManager.getInstance(StoryDetailActivity.this).put(Constant.getShareProductHost, result.show.appHost);
                        //转链接
                        appHost = result.show.appHost;
                        share();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        getCommentDate();
    }

    /**
     * 改变显示状态
     *
     * @describe
     */
    private void changeVeiw() {

        if (ivDianzan.isSelected()) {
            ivDianzan.setSelected(false);
            tipOff.praiseCount = tipOff.praiseCount - 1;
            tvNumberDianzan.setText(tipOff.praiseCount + "次赞");
        } else {
            ivDianzan.setSelected(true);
            tipOff.praiseCount = tipOff.praiseCount + 1;
            tvNumberDianzan.setText(tipOff.praiseCount + "次赞");
        }
    }


    //访问网络
    public void getData() {
        id = getIntent().getStringExtra("id");
        Network.getInstance().getApi(TipoffShowApi.class)
                .getTipoffDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TipoffDetail>() {
                    @Override
                    public void success(TipoffDetail data) {
                        tipOff = data.tipOff;
                        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
                        if (data.status == 0) {//表示一点赞
                            flag = false;
                            ivDianzan.setSelected(true);
                        } else {//没点赞
                            flag = true;
                            ivDianzan.setSelected(false);
                        }
                        //有头像设置头像
                        String photo = data.tipOff.photo;
                        if (photo != null && !photo.equals("")) {
                            Glide.with(StoryDetailActivity.this).load(photo).into((ivAuthorHead));//作者头像
                        }
                        webData();
                        tvTime.setText(tipOff.createTimeStr);//时间
                        tvNumberDianzan.setText(tipOff.praiseCount + "次赞");//点赞
                        tvTitleStory.setText(tipOff.title);//文章标题
                        tvAuthorName.setText(tipOff.nickName); //作者名
                        tvSeeNumber.setText(tipOff.pageViews + "");//浏览量
                        mScrollView.onRefreshComplete();
                        getCommentDate();
                        //侧滑栏数据
                        getDrawerData(data.productList);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    //侧滑栏数据
    private void getDrawerData(final List<TipoffDetail.ProductListBean> product) {
        if (product != null) {

            lvChouti.setAdapter(new CommonAdapter<TipoffDetail.ProductListBean>(product, mContext, R.layout.item_story_detail_product) {
                @Override
                public void setData(CommonViewHolder viewHolder, Object item) {

                    viewHolder.displayImage(R.id.iv_img, ((TipoffDetail.ProductListBean) item).imgs);
                    viewHolder.setText(R.id.tv_title, ((TipoffDetail.ProductListBean) item).name);
                    viewHolder.setText(R.id.tv_price, "¥" + ((TipoffDetail.ProductListBean) item).preferentialPrice);
                    if (((TipoffDetail.ProductListBean) item).couponMoney != null) {
                        viewHolder.getView(R.id.ll_quan).setVisibility(View.VISIBLE);
                        viewHolder.setText(R.id.couponMoney, ((TipoffDetail.ProductListBean) item).couponMoney);
                        viewHolder.setText(R.id.tv_soujia, "券后价");
                    } else {
                        viewHolder.getView(R.id.ll_quan).setVisibility(View.GONE);
                        viewHolder.setText(R.id.tv_soujia, "售价");
                    }
                }
            });

            lvChouti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    startActivity(new Intent(mContext, ProductDetailsActivity.class)
//                            .putExtra("t", 2)
//                            .putExtra(Constant.listInfo, JSON.toJSONString(product.get(position))));

                    findProductInfo(product.get(position).alipayItemId,product.get(position).id);

                }
            });
        }
    }



    /**
     * 中间段加载数据
     */
    private void webData() {
        //记载网页
        WebSettings webSettings = wvContent.getSettings();
        // 让WebView能够执行javaScript


        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        //自适应屏幕
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);

        //加载HTML字符串进行显示
        wvContent.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        wvContent.loadData(tipOff.textBoxContent, "text/html; charset=UTF-8", null);
        // 设置WebView的客户端
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
    }

    /**
     * 获取评论状态
     */
    public void getCommentDate() {
        String id = getIntent().getStringExtra("id");
        Network.getInstance().getApi(TipoffShowApi.class)
                .getTipoffList(id, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TipoffListBean>() {
                    @Override
                    public void success(TipoffListBean result) {
                        appraiseList = result.appraiseList;


                        lvComment.setAdapter(new CommonAdapter<TipoffListBean.AppraiseListBean>(result.appraiseList, mContext, R.layout.tipoff_comment_item) {
                            @Override
                            public void setData(CommonViewHolder viewHolder, Object item) {
                                final TipoffListBean.AppraiseListBean tipAppraise = (TipoffListBean.AppraiseListBean) item;
                                if (tipAppraise.photo != null && !tipAppraise.photo.equals("")) {
                                    viewHolder.displayImage(R.id.iv_img, tipAppraise.photo);
                                }
                                viewHolder.setText(R.id.tv_nickname, tipAppraise.nickName);
                                viewHolder.setText(R.id.tv_time, tipAppraise.createTimeStr);
                                viewHolder.setText(R.id.tv_details, tipAppraise.content);
                            }
                        });
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    //评论点赞
    private void tipHelp(String tipId, int index) {
        Network.getInstance().getApi(TipoffShowApi.class)
                .tipPraise(tipId, index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>() {
                    @Override
                    protected void success(String data) {
                        ToastManager.getInstance().showShortToast(data);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }


                });
    }

    private PullToRefreshBase.OnRefreshListener2 refresh = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            String label = DateUtils.formatDateTime(
                    getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            // 显示最后更新的时间
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            // 模拟加载任务
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            // 显示最后更新的时间
            String label = DateUtils.formatDateTime(
                    getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            mScrollView.onRefreshComplete();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断侧滑栏是否开启  如果开启关闭
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerlayout.isDrawerOpen(llChouti)) {
                drawerlayout.closeDrawer(llChouti);
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!UserHelper.isLogin()){
            LoginActivity.launchActivity(this);
            return;
        }
        UMWeb umWeb = new UMWeb(appHost + "/" + QUrl.tipOffNewShare + "?id=" + tipOff.id + "&userId=" + UserHelper.getUserInfo(this).id);
        umWeb.setTitle(getResources().getString(R.string.app_name));
        umWeb.setThumb(new UMImage(this,R.mipmap.icon));
        umWeb.setDescription(tipOff.title);
        ShareAction shareAction = new ShareAction(this)
                .withMedia(umWeb);
        switch (v.getId()){
            case R.id.share_weixin:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_friends:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_qq:
                shareAction.setPlatform(SHARE_MEDIA.QQ);
                break;
            case R.id.share_qq_kongjian:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                break;
        }
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Toast.makeText(StoryDetailActivity.this, "分享开始", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Toast.makeText(StoryDetailActivity.this, "成功了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Toast.makeText(StoryDetailActivity.this, "异常:"+throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(StoryDetailActivity.this, "取消分享", Toast.LENGTH_LONG).show();
            }
        }).share();
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
                .subscribe(new NetResultHandler<LiveProductInfoBean>() {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        if (result == null) {
                            return;
                        }
//                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
//                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
//                                .putExtra("type", 4 + ""));
                        startActivity(new Intent(StoryDetailActivity.this, ProductDetailsActivity.class)
                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
                                .putExtra("t", 2));
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
