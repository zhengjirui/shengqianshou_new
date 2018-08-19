package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.KefuInfoBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.widget.views.ProgressWebView;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：12:22
 * 描述：帮助中心
 */

public class HelpCenterActivity extends BaseNormalTitleActivity {
    @BindView(R.id.wv_progress)
    ProgressWebView wvContent;

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, HelpCenterActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {
        setTitleName("帮助中心");
    }

    @Override
    public void initData() {
        Network.getInstance().getApi(CommenApi.class)
                .getHelpInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<KefuInfoBean>(HelpCenterActivity.this) {
                    @Override
                    protected void success(KefuInfoBean data) {
                        String helpInfo = data.HelpInfo.helpInfo;
                        webData(helpInfo);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void webData(String helpInfo) {
        //记载网页
        WebSettings webSettings = wvContent.getSettings();
        // 让WebView能够执行javaScript

        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        //自适应屏幕
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDefaultFontSize(30);
        //加载HTML字符串进行显示
        wvContent.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        wvContent.loadData(helpInfo, "text/html; charset=UTF-8", null);
        // 设置WebView的客户端
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
    }
}
