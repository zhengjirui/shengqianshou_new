package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.view.View;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.getmoney.GetMoneyActivity;
import com.lechuang.lqsq.bean.UpdataInfoBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：12:32
 * 描述：分享赚钱
 */

public class FenXiangZhuanQianActivity extends BaseNormalTitleActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_fxzq;
    }

    @Override
    public void initView() {
        setTitleName("分享赚钱");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.fenxiang, R.id.taobao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fenxiang:
//                startActivity(new Intent(this, ShareMoneyActivity.class));
                startActivity(new Intent(this, ShareAppActivity.class));
                finish();
                break;
            case R.id.taobao:
                /*startActivity(new Intent(this, GetMoneyActivity.class));
                finish();*/
                final AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(this, new AlibcLoginCallback() {
                    @Override
                    public void onSuccess() {
                        Session taobao = alibcLogin.getSession();
                        updateInfoTaobao(taobao.nick);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastManager.getInstance().showLongToast(s);
                    }
                });
                break;
        }
    }

    private void updateInfoTaobao(final String nick) {
        Map<String, String> map = new HashMap<>();
        map.put("taobaoNumber", nick);
        Network.getInstance().getApi(CommenApi.class)
                .updataInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpdataInfoBean>() {
                    @Override
                    public void success(UpdataInfoBean result) {
                        UserInfo userInfo = UserHelper.getUserInfo(FenXiangZhuanQianActivity.this);
                        userInfo.taobaoNumber = nick;
                        UserHelper.saveUserInfo(FenXiangZhuanQianActivity.this, userInfo);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }
}
