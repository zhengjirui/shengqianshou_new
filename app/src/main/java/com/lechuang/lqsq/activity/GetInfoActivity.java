package com.lechuang.lqsq.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.GetInfoBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：13:58
 * 描述：分享赚说明
 */

public class GetInfoActivity extends BaseNormalTitleActivity {
    @BindView(R.id.wv_content)
    WebView wvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_getinfo;
    }

    @Override
    public void initView() {
        setTitleName("分享赚");
    }

    @Override
    public void initData() {
        Network.getInstance().getApi(CommenApi.class)
                .zhaunInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<GetInfoBean>(GetInfoActivity.this) {
                    @Override
                    public void success(GetInfoBean result) {
                        webData(result.record.shareZhuanInfo);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    /**
     * 中间段加载数据
     */
    private void webData(String str) {
        //记载网页
        WebSettings webSettings = wvContent.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);

        //自适应屏幕
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        //加载HTML字符串进行显示
        wvContent.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        wvContent.loadData(str, "text/html; charset=UTF-8", null);
        // 设置WebView的客户端
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
    }
}
