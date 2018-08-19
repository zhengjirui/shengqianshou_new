package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.AdvertisementBean;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author yrj
 * @date 2017/10/30
 * @E-mail 1422947831@qq.com
 * @desc 广告图
 */

public class AdvertisementActivity extends AppCompatActivity {

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private Context mContext = AdvertisementActivity.this;
    //跳转的url
    private String adUrl;
    //广告跳转类型 0：跳app外web页面 1：跳商品详情
    private int type;
    private String alipayCouponId;
    private String alipayItemId;
    //商品名
    private String productName;
    //原价
    private String price;
    //优惠价
    private String preferentialPrice;
    //分享赚
    private String shareIntegral;
    //商品图
    private String img;
    //商品类型
    private String shopType;

    private ArrayList<String> smallImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        ButterKnife.bind(this);
        initView();
    }

    //初始化视图
    private void initView() {
        getAdvertisementTime(tvTime);
        handler.post(t);
        AdvertisementBean.AdvertisingImgBean bean = (AdvertisementBean.AdvertisingImgBean) getIntent().getSerializableExtra("bean");
        if (bean == null) {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
            return;
        }
        Glide.with(mContext).load(bean.adImage).into(ivImg);
        type = bean.type;
        adUrl = bean.adUrl;
        alipayCouponId = bean.alipayCouponId;
        alipayItemId = bean.alipayItemId;


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

    LiveProductInfoBean.ProductWithBLOBsBean productWithBLOBs;

    @OnClick({R.id.iv_img, R.id.tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                if (type == 1) {
                    //商品详情
                    if (alipayItemId != null) {
                        handlers.sendEmptyMessage(1);
                    }
                    startActivity(new Intent(mContext, MainActivity.class));


                } else {
                    startActivity(new Intent(mContext, MainActivity.class));
                    startActivity(new Intent(mContext, EmptyWebActivity.class).putExtra("url", adUrl));
                }
                finish();
                break;
            case R.id.tv_time:
                //跳过广告图
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
                break;
        }
    }


    //广告图时间倒计时
    private int advertisement_time = Constant.ADVERTISEMENT_TIME;
    /*
    * 倒计时
    * */
    public static Handler handler;
    public static Thread t;

    //广告图倒计时
    public void getAdvertisementTime(final TextView tv) {
        handler = new Handler();
        t = new Thread() {
            @Override
            public void run() {
                super.run();
                advertisement_time--;
                tv.setText(advertisement_time + "s后跳过广告");

                if (advertisement_time <= 0) {
                    startActivity(new Intent(mContext, MainActivity.class));
                    handler = null;
                    finish();
                    return;
                }
                if (handler != null)
                    handler.postDelayed(this, 1000);
            }
        };
    }

    //广告图倒计时回收
    public void advertisementCloseCode() {
        if (handler != null) {
            handler = null;
            t = null;
            advertisement_time = Constant.ADVERTISEMENT_TIME;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisementCloseCode();
    }

    public Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Map<String, String> map = new HashMap<>();
                map.put("productId", alipayItemId);
                Network.getInstance().getApi(CommenApi.class)
                        .getProductInfo(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new NetResultHandler<LiveProductInfoBean>() {
                            @Override
                            public void success(LiveProductInfoBean result) {
                                LiveProductInfoBean.ProductWithBLOBsBean productWithBLOBs = result.productWithBLOBs;
                                if (productWithBLOBs != null) {
//                                    startActivity(new Intent(mContext, ProductDetailsActivity.class)
//                                            .putExtra(Constant.listInfo, JSON.toJSONString(productWithBLOBs)));
                                    findProductInfo(productWithBLOBs.alipayItemId,productWithBLOBs.id);
                                }
                            }

                            @Override
                            public void error(int code, String msg) {

                            }


                        });
            }
        }
    };

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
                        startActivity(new Intent(AdvertisementActivity.this, ProductDetailsActivity.class)
                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs)));
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
