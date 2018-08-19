package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.widget.views.ProgressWebView;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：14:21
 * 描述：直播间
 */

public class ZBActivity extends BaseNormalTitleActivity {
    private ProgressWebView mWeb;
    private boolean isRefulsh = true;    //是否刷新

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, ZBActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    public void initView() {
        setTitleName("直播间");

    }

    @Override
    public void initData() {
        mWeb = (ProgressWebView) findViewById(R.id.wv_live);
        //js调用
        mWeb.getSettings().setJavaScriptEnabled(true);
        //是否储存
        mWeb.getSettings().setDomStorageEnabled(true);
        //缓存大小
        //mWeb.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        //缓存路径
        //String appCachePath = getCacheDir().getAbsolutePath();
        //mWeb.getSettings().setAppCachePath(appCachePath);
        //是否禁止访问文件数据
        mWeb.getSettings().setAllowFileAccess(true);
        mWeb.getSettings().setAppCacheEnabled(true);
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        //是否支持缩放
        mWeb.getSettings().setSupportZoom(true);
        mWeb.getSettings().setBuiltInZoomControls(true);
        mWeb.getSettings().setDisplayZoomControls(false);
        mWeb.loadUrl(QUrl.live);
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("appfun:live:")) {
                    isRefulsh = false;
                    // appfun:live:533792247617:1vyg6
                    String produceId = "";
                    String zbjId = "";
                    try {
                        String alipayItemId = url.substring(12);
                        String[] split = alipayItemId.split(":");
                        produceId = split[0];
                        zbjId = split[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String mzbjId = zbjId;
                    Map<String, String> map = new HashMap<>();
                    map.put("productId", produceId);
                    map.put("type", "3");
                    map.put("zbjId", zbjId);
                    Network.getInstance().getApi(CommenApi.class)
                            .getProductInfo(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NetResultHandler<LiveProductInfoBean>(ZBActivity.this) {
                                @Override
                                public void success(LiveProductInfoBean result) {
                                    if (result.productWithBLOBs != null) {
                                        Intent intent = new Intent(ZBActivity.this, ProductDetailsActivity.class);
                                        intent.putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs));
                                        intent.putExtra("t", 3);
                                        intent.putExtra("zbjId", mzbjId);
                                        startActivity(intent);
                                    } else {
                                        showShortToast("商品已过期");
                                    }

                                }

                                @Override
                                public void error(int code, String msg) {

                                }
                            });
                    //startActivity(new Intent(getActivity(),LiveProductDetailsActivity.class).putExtra("alipayItemId",alipayItemId));

                } else if (url.startsWith("appfun:zoomImage:")) {
                    //是否刷新
                    isRefulsh = false;
                    Intent intent = new Intent(ZBActivity.this, SunBigPicActivity.class);
                    intent.putExtra("live", 1);
                    intent.putExtra("bigImg", url.substring(17));
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //点击大图之后不刷新
        if (isRefulsh) {
            initData();
        }
        isRefulsh = true;
    }
}
