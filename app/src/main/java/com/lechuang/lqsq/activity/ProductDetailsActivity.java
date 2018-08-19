package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.AllProductBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.TaobaoUrlBean;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.DemoTradeCallback;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.widget.views.ProgressWebView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：14:29
 * 描述：
 */

public class ProductDetailsActivity extends BaseActivity {
    @BindView(R.id.ll_youquan)
    View llyou;
    @BindView(R.id.ll_wuquan)
    View llwu;
    @BindView(R.id.shoucang_img)
    ImageView shoucangImg;
    @BindView(R.id.shoucang_tv)
    TextView shoucangTv;
    @BindView(R.id.jifen_num)
    TextView jifenNum;
    @BindView(R.id.quan_num)
    TextView quanNum;
    @BindView(R.id.shoucang_img1)
    ImageView shoucangImg1;
    @BindView(R.id.shoucang_tv1)
    TextView shoucangTv1;
    @BindView(R.id.quan_num1)
    TextView quanNum1;
    private ProgressWebView mWeb;
    private String productUrl;
    private Context mContext = ProductDetailsActivity.this;

    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    private int t;
    private String zbjId;
    private String type;
    //是否是代理
    private int isAgencyStatus;
    //用户id
    private String userId;
    private AllProductBean.ListInfo bean;
    private boolean shoucangLoading = false;
    private LiveProductInfoBean details;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_details;
    }

    private boolean isLoadUrl = false;

    @Override
    public void initView() {
        if (UserHelper.isLogin()) {
            llyou.setVisibility(View.VISIBLE);
            llwu.setVisibility(View.GONE);
            isAgencyStatus = UserHelper.getUserInfo(this).isAgencyStatus;
            userId = UserHelper.getUserInfo(this).id;
        } else {
            llyou.setVisibility(View.GONE);
            llwu.setVisibility(View.VISIBLE);
            isAgencyStatus = 0;
            userId = "";
        }
        isLoadUrl = getIntent().getBooleanExtra("isUrl", false);
        type = getIntent().getStringExtra("type");
        if (isLoadUrl) {
            this.loadUrl = getIntent().getStringExtra("url");
        } else {
            bean = JSON.parseObject(getIntent().getStringExtra("listInfo"), AllProductBean.ListInfo.class);
            t = getIntent().getIntExtra("t", 1);
            zbjId = getIntent().getStringExtra("zbjId");


            StringBuilder sb = new StringBuilder();

            if (isAgencyStatus == 1) {
                sb.append(QUrl.productDetails + "?a=1");
            }else {
                sb.append(QUrl.productDetails + "?a=0");
            }

            StringUtils.append(sb,"i",bean.alipayItemId);
            if (TextUtils.isEmpty(type)){
                StringUtils.append(sb,"t",t + "");
            }else {
                StringUtils.append(sb,"t",type + "");
            }

            StringUtils.append(sb,"userId",userId);
            StringUtils.append(sb,"zbjId",zbjId);
            StringUtils.append(sb,"id",bean.id);
            this.loadUrl = sb.toString();

//            if (isAgencyStatus == 1) {
//                //代理  a = 1
//                this.loadUrl = QUrl.productDetails + "?i=" + bean.alipayItemId + "&t=" + t + "&a=1" + "&userId=" + userId + "&zbjId=" + zbjId + "&id=" + bean.id;
//            } else {
//                this.loadUrl = QUrl.productDetails + "?i=" + bean.alipayItemId + "&t=" + t + "&a=0" + "&userId=" + userId + "&zbjId=" + zbjId + "&id=" + bean.id;
//            }


        }
        mWeb = (ProgressWebView) findViewById(R.id.web_product);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserHelper.isLogin()) {
            llyou.setVisibility(View.VISIBLE);
            llwu.setVisibility(View.GONE);
            isAgencyStatus = UserHelper.getUserInfo(this).isAgencyStatus;
            userId = UserHelper.getUserInfo(this).id;
        } else {
            llyou.setVisibility(View.GONE);
            llwu.setVisibility(View.VISIBLE);
            isAgencyStatus = 0;
            userId = "";
        }
    }

    private String loadUrl;

    @Override
    public void initData() {
        getDetails();
        //js调用
        mWeb.getSettings().setJavaScriptEnabled(true);
        //是否储存
        mWeb.getSettings().setDomStorageEnabled(true);
        //缓存大小
        //mWeb.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        //缓存路径
        //String appCachePath = getCacheDir().getAbsolutePath();
        //mWeb.getSettings().setAppCachePath(appCachePath);
        mWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mWeb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            mWeb.getSettings().setAllowFileAccessFromFileURLs(true);
        }

        mWeb.getSettings().setAllowContentAccess(true);

        mWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
        //是否禁止访问文件数据
        mWeb.getSettings().setAllowFileAccess(true);
        mWeb.getSettings().setAppCacheEnabled(true);
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        //是否支持缩放
        mWeb.getSettings().setSupportZoom(true);
        mWeb.getSettings().setBuiltInZoomControls(true);
        mWeb.getSettings().setDisplayZoomControls(false);
        mWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http")) {
                    if (url.equals(loadUrl)) {
                        return super.shouldOverrideUrlLoading(view, url);
                    } else {
                        view.loadUrl(url);
                        return true;
                    }
                } else if (url.equals("appfun:product:detail") && UserHelper.isLogin()) {
                    if (UserHelper.isLogin())
                        goumai();
                    else
                        LoginActivity.launchActivity(ProductDetailsActivity.this);
                }
                return true;

            }
        });
        mWeb.loadUrl(loadUrl);

    }

    public void back(View view) {
        if (mWeb.canGoBack()) {
            mWeb.goBack();
            return;
        }
        finish();
    }

    @OnClick({R.id.zuji1, R.id.shoucang1, R.id.fenxiang1, R.id.goumai1, R.id.zuji, R.id.shoucang, R.id.fenxiang, R.id.goumai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zuji:
            case R.id.zuji1:
                if (UserHelper.isLogin()) {
                    if (getIntent().getBooleanExtra("isClose", false)) {
                        finish();
                    }
                    startActivity(new Intent(this, WoDeZujiActivity.class));
                } else {
                    LoginActivity.launchActivity(this);
                }
                break;
            case R.id.shoucang:
            case R.id.shoucang1:
                if (UserHelper.isLogin())
                    shouchang();
                else
                    LoginActivity.launchActivity(this);
                break;
            case R.id.fenxiang:
            case R.id.fenxiang1:
                if (UserHelper.isLogin()) {
                    startActivity(new Intent(mContext, ProductShareActivity.class).putExtra(Constant.listInfo, bean));
                } else {
                    LoginActivity.launchActivity(this);
                }
                break;
            case R.id.goumai:
            case R.id.goumai1:
                goumai();
                break;
        }
    }

    private void goumai() {
        Network.getInstance().getApi(CommenApi.class)
                .tb_privilegeUrl(bean.alipayItemId, bean.alipayCouponId, bean.imgs, bean.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TaobaoUrlBean>(ProductDetailsActivity.this) {
                    @Override
                    public void success(TaobaoUrlBean result) {
                        productUrl = result.productWithBLOBs.tbPrivilegeUrl;
                        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                        AlibcTaokeParams taoke = new AlibcTaokeParams(Constant.PID, "", "");
                        exParams = new HashMap<>();
                        exParams.put("isv_code", "appisvcode");
                        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                        //Utils.E("url:"+url);
                        //view.loadUrl(url);

                        //Utils.E("cccccc:"+getIntent().getStringExtra("alipayItemId"));
                        //AlibcBasePage alibcBasePage = new AlibcDetailPage(getIntent().getStringExtra("alipayItemId"));
                        //实例化URL打开page
                        //AlibcBasePage alibcBasePage = new AlibcPage("https://uland.taobao.com/coupon/edetail?activityId=91cdf70f6a944043b21c9dfca39a889c&pid=mm_116411007_36292444_142176907&itemId=544410512702&src=lc_tczs");
                        AlibcBasePage alibcBasePage = new AlibcPage(productUrl);
                        //AlibcTrade.show(ProductDetailsActivity.this, alibcBasePage, alibcShowParams, null, exParams , new DemoTradeCallback());

                        //添加购物车
                        //AlibcBasePage alibcBasePage = new AlibcAddCartPage(getIntent().getStringExtra("alipayItemId"));
                        AlibcTrade.show(ProductDetailsActivity.this, alibcBasePage, alibcShowParams, taoke, exParams, new DemoTradeCallback());

                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void getDetails() {
        Map<String, String> map = new HashMap<>();
        if (!isLoadUrl) {
            if (!TextUtils.isEmpty(bean.id))
                map.put("id", bean.id);
            map.put("productId", bean.alipayItemId);
            if (!TextUtils.isEmpty(zbjId)) {
                map.put("type", t + "");
                map.put("zbjId", zbjId);
            }
        } else {
            map.put("productId", Uri.parse(loadUrl).getQueryParameter("id"));
        }
        if (!TextUtils.isEmpty(type)){
            map.put("type",type);
        }
        Network.getInstance().getApi(CommenApi.class)
                .getProductInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<LiveProductInfoBean>(ProductDetailsActivity.this) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        details = result;
//                        quanNum.setText(result.productWithBLOBs.couponMoney + "");
//                        quanNum1.setText(result.productWithBLOBs.couponMoney + "");
                        quanNum.setText(result.productWithBLOBs.preferentialPrice);
                        quanNum1.setText(result.productWithBLOBs.preferentialPrice);
                        if (TextUtils.isEmpty(result.productWithBLOBs.zhuanMoney))
                            jifenNum.setText("0");
                        else
                            jifenNum.setText(result.productWithBLOBs.zhuanMoney);
                        shoucangsta(details.productWithBLOBs.isCollection == 1);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void shouchang() {
        if (shoucangLoading) return;

        HashMap<String, String> map = new HashMap<>();
        if (!isLoadUrl) {
            if (details == null)
                return;

            map.put("productId", TextUtils.isEmpty(details.productWithBLOBs.id) ? "" : details.productWithBLOBs.id);
            map.put("alipayItemId", details.productWithBLOBs.alipayItemId);
        } else {
            if (loadUrl.contains("id"))
                map.put("productId", Uri.parse(loadUrl).getQueryParameter("id"));
        }
        shoucangLoading = true;
        Network.getInstance().getApi(CommenApi.class)
                .coullect(map)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(ProductDetailsActivity.this) {
                    @Override
                    protected void success(String data) {
                        if (UserHelper.isLogin())
                            shoucangsta(!shoucangImg.isSelected());
                        else
                            shoucangsta(!shoucangImg1.isSelected());
                    }

                    @Override
                    public void error(int code, String msg) {
                        shoucangLoading = false;
                    }

                    @Override
                    public void onCompleted() {
                        shoucangLoading = false;
                        super.onCompleted();
                    }
                });
    }

    private void shoucangsta(boolean is) {
        if (is) {
            shoucangTv.setTextColor(Color.parseColor("#ff5c19"));
            shoucangTv1.setTextColor(Color.parseColor("#ff5c19"));
        } else {
            shoucangTv.setTextColor(Color.parseColor("#666666"));
            shoucangTv1.setTextColor(Color.parseColor("#666666"));
        }
        shoucangImg.setSelected(is);
        shoucangImg1.setSelected(is);
    }

}
