package com.lechuang.lqsq.fragments;

import android.content.Intent;
import android.media.Image;
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
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.KindDetailActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.activity.SearchActivity;
import com.lechuang.lqsq.activity.SearchResultActivity;
import com.lechuang.lqsq.activity.SunBigPicActivity;
import com.lechuang.lqsq.activity.ZBActivity;
import com.lechuang.lqsq.activity.ZQZNActivity;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.CountBean;
import com.lechuang.lqsq.bean.HomeKindBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.SoufanliProgremBean;
import com.lechuang.lqsq.bean.SoufanliResultBean;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.HomeApi;
import com.lechuang.lqsq.utils.Utils;
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
 * 日期：2018/01/30
 * 时间：14:00
 * 描述：
 */

public class ZbFragment extends BaseFragment{

    private boolean isRefulsh = true;    //是否刷新
    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.wv_live)
    ProgressWebView mWeb;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        ivLeftBack.setVisibility(View.GONE);
        titleName.setText("直播间");

    }

    @Override
    protected void initData() {
        super.initData();
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
                            .subscribe(new NetResultHandler<LiveProductInfoBean>((BaseActivity) getActivity()) {
                                @Override
                                public void success(LiveProductInfoBean result) {
                                    if (result.productWithBLOBs != null) {
                                        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                                        intent.putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs));
                                        intent.putExtra("t", 3);
                                        intent.putExtra("zbjId", mzbjId);
                                        startActivity(intent);
                                    } else {
                                        Utils.show(getContext(),"商品已过期");
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
                    Intent intent = new Intent(getContext(), SunBigPicActivity.class);
                    intent.putExtra("live", 1);
                    intent.putExtra("bigImg", url.substring(17));
                    startActivity(intent);
                }
                return true;
            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //todo  这里应该是显示或者获取的时候开始刷新
//        if (!hidden && countNumber != null && countNumResult != null) {
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //todo  这里应该是显示或者获取的时候开始刷新
    }

    @Override
    public void onPause() {
        super.onPause();
        //点击大图之后不刷新
        if (isRefulsh) {
            initData();
        }
        isRefulsh = true;
    }
}
