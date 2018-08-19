package com.lechuang.lqsq.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;


import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.AdvertisementBean;
import com.lechuang.lqsq.bean.ControlBean;
import com.lechuang.lqsq.bean.LoadingImgBean;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: guoning
 * Date: 2017/10/10
 * Description:
 */

public class SplashActivity extends AppCompatActivity {
    private Context mContext = this;
    //启动页list
    private List<String> loadingList;
    private final String isFirstIn = "isFirstIn";
    private Boolean isFirst;
    private AdvertisementBean.AdvertisingImgBean advertisingImg;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Network.getInstance().getApi(CommenApi.class, null)
                .tellServer("lqsq", UserHelper.isLogin() ? UserHelper.getUserInfo(this).phone : null, BuildConfig.buildTime, BuildConfig.VERSION_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
        Network.getInstance().getApi(CommenApi.class, null)
                .getControlInfo(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ControlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ControlBean controlBean) {
                        if (controlBean == null) return;
                        CacheManager.getInstance(SplashActivity.this).put("cinfo", controlBean);
                    }
                });
        ControlBean cinfo = CacheManager.getInstance(this).get("cinfo", ControlBean.class);
        if (cinfo != null && cinfo.close) {
            if (!TextUtils.isEmpty(cinfo.phoneRegex)) {
                if (UserHelper.isLogin() && Pattern.matches(cinfo.phoneRegex, UserHelper.getUserInfo(this).phone)) {
                    findViewById(R.id.systemClose).setVisibility(View.VISIBLE);
                    return;
                }
            } else {
                findViewById(R.id.systemClose).setVisibility(View.VISIBLE);
                return;
            }

        }
        isFirst = ((String) CacheManager.getInstance(this).get(isFirstIn, "Y")).equalsIgnoreCase("Y");
        initDate();
        FrameLayout root = (FrameLayout) findViewById(R.id.splash_content);
        ViewPropertyAnimator animate = root.animate();
        animate.setDuration(1500).start();
        animate.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 如果第一次，则进入引导页WelcomeActivity
                        if (isFirst) {
                            CacheManager.getInstance(SplashActivity.this).put(isFirstIn, "N");
                            loadingList = new ArrayList<String>();
                            getFirstAdverPic();
                        } else if (isLoadAdvertisement) {
                            //广告图
                            startActivity(new Intent(SplashActivity.this, AdvertisementActivity.class)
                                    .putExtra("bean", advertisingImg));
                            //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                        // 设置Activity的切换效果
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    private boolean isLoadAdvertisement = false;

    private void initDate() {
        Network.getInstance().getApi(CommenApi.class)
                .advertisementInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<AdvertisementBean>() {
                    @Override
                    public void success(AdvertisementBean result) {
                        if (result.advertisingImg != null) {
                            isLoadAdvertisement = true;
                            advertisingImg = result.advertisingImg;
                        }
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

    public void getFirstAdverPic() {
        Network.getInstance().getApi(CommenApi.class)
                .loadingImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<LoadingImgBean>() {
                    @Override
                    public void success(LoadingImgBean result) {
                        int size = result.list.size();
                        for (int i = 0; i < size; i++) {
                            loadingList.add(result.list.get(i).startImage);
                        }
                        Intent intent = new Intent(mContext, FirstAdvertActivity.class);
                        intent.putStringArrayListExtra("arraylist", (ArrayList<String>) loadingList);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isLoadAdvertisement) {
                            //广告图
                            startActivity(new Intent(SplashActivity.this, AdvertisementActivity.class)
                                    .putExtra("bean", advertisingImg));
                            //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                });
    }

}
