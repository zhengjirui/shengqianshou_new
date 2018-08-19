package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.umeng.analytics.MobclickAgent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：li on 2017/10/6 15:31
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class ApplyAgentActivity extends AppCompatActivity {

    private ImageView iv_noNet, iv_applyAgent;
    private Context mContext = ApplyAgentActivity.this;
    private ImageView mContentView;
    private boolean mLoadImg = false;
    private double payPriceStr;
    private OwnBean.Agency agency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_agent);


        initView();
//        WebViewUtils.loadUrl(progressWebView,this,"http://192.168.1.210:8889/user/appUsers/agencyDetail");

        Network.getInstance().getApi(OwnApi.class).agency().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnBean>() {
                    @Override
                    public void success(OwnBean result) {
                        agency = result.agency;
                        String img = agency.img;
                        payPriceStr = agency.payPriceStr;
                        Glide.with(ApplyAgentActivity.this).load(img).centerCrop().into(mContentView);
                        mLoadImg = true;
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });


        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLoadImg) return;
                if (agency.type == 1) {
                    return;
                }
                if (agency.type == 0 && agency.payPrice == 0) {
                    Network.getInstance().getApi(OwnApi.class)
                            .autoAgent()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NetResultHandler<String>() {
                                @Override
                                public void success(String result) {
                                    ToastManager.getInstance().showShortToast(result);
                                    UserInfo userInfo = UserHelper.getUserInfo(ApplyAgentActivity.this);
                                    userInfo.isAgencyStatus = 1;
                                    UserHelper.saveUserInfo(ApplyAgentActivity.this, userInfo);
                                    finish();
                                }

                                @Override
                                public void error(int code, String msg) {

                                }
                            });
                    return;
                }
                startActivity(new Intent(ApplyAgentActivity.this, PayStyleActivity.class).putExtra(Constant.KEY_PAY_PRICE, payPriceStr));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        getData();
//        findViewById(R.id.web_back).setVisibility(View.VISIBLE);
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
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_noNet = (ImageView) findViewById(R.id.iv_noNet);
        iv_applyAgent = (ImageView) findViewById(R.id.iv_applyAgent);
        mContentView = (ImageView) findViewById(R.id.content_agency);
    }

    public void getData() {
        // TODO: 2017/10/6
    }
}
