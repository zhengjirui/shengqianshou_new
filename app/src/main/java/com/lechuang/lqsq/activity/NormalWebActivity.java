package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.widget.views.ProgressWebView;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：10:02
 * 描述：
 */

public class NormalWebActivity extends BaseNormalTitleActivity {
    private ProgressWebView mWeb;
    private boolean isRefulsh = true;    //是否刷新
    private String t, u;

    public static void lanuchActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, NormalWebActivity.class);
        intent.putExtra("t", title);
        intent.putExtra("u", url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_normal_web;
    }

    @Override
    public void initView() {
        t = getIntent().getStringExtra("t");
        u = getIntent().getStringExtra("u");
        if (!TextUtils.isEmpty(t))
            setTitleName(t);
        mWeb = (ProgressWebView) findViewById(R.id.wv_live);
        //js调用
        mWeb.getSettings().setJavaScriptEnabled(true);
        //是否储存
        mWeb.getSettings().setDomStorageEnabled(true);
        //缓存大小
        mWeb.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        //缓存路径
        //String appCachePath = getCacheDir().getAbsolutePath();
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
                if(startUrl != null && startUrl.equals(url)){
                    view.loadUrl(url);
                }else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startUrl = url;
            }
        });
        mWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                if (TextUtils.isEmpty(t)) {
//                    setTitleName(title);
//                }
                if (!TextUtils.isEmpty(title)){
                    setTitleName(title);
                }else if (!TextUtils.isEmpty(t)){
                    setTitleName(t);
                }
                super.onReceivedTitle(view, title);
            }
        });
        mWeb.loadUrl(u);
    }
    private String startUrl;

    @Override
    public void initData() {
    }

    @Override
    public void back(View view) {
        if (mWeb.canGoBack()) {
            mWeb.goBack();
            return;
        }
        super.back(view);
    }

}
