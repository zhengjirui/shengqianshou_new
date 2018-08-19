package com.lechuang.lqsq.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.PhotoUtil;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.views.ClearEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.lechuang.lqsq.utils.PhotoUtil.t;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/10
 * 时间：09:22
 * 描述：
 */

public class BoundPhoneActivity extends BaseNormalTitleActivity implements View.OnClickListener {
    private Context mContext = BoundPhoneActivity.this;
    private TextView tv_code;
    //手机号 验证码
    private ClearEditText et_phonenum, et_proving;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bound_phone;
    }

    @Override
    public void initView() {
        setTitleName("绑定手机号");
        tv_code = (TextView) findViewById(R.id.tv_code);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        et_phonenum = (ClearEditText) findViewById(R.id.et_phonenum);
        //验证码
        et_proving = (ClearEditText) findViewById(R.id.et_proving);
        tv_code.setOnClickListener(this);
    }

    private int boundChannel;//绑定的渠道，淘宝还是微信或者QQ

    @Override
    public void initData() {
        boundChannel = getIntent().getIntExtra("boundChannel", -1);

    }

    @Override
    public void back(View view) {
        super.back(view);
        exitTaobao();
        showShortToast("登录失败");
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_code: //获取验证码
                if (!Utils.isTelNumber(et_phonenum.getText().toString())) {
                    showShortToast("请输入正确的手机号");
                    return;
                }
                if (Utils.isNetworkAvailable(mContext)) {
                    threeCode();
                    PhotoUtil.getCode(tv_code);
                    PhotoUtil.handler.post(t);
                    tv_code.setEnabled(false);
                } else {
                    showShortToast(getString(R.string.net_error1));
                }
                break;
            case R.id.btn_ok: //确定
                if (Utils.isEmpty(et_phonenum)) {
                    showShortToast("请输入手机号");
                    return;
                }
                if (Utils.isEmpty(et_proving)) {
                    showShortToast("请输入验证码");
                    return;
                }
                /*if (Utils.isEmpty(tv_code)) {
                    showShortToast("请输入验证码");
                    return;
                }*/
                if (Utils.isTelNumber(et_phonenum.getText().toString())) {
                    if (Utils.isNetworkAvailable(mContext)) {
                        if (boundChannel == 0) {
                            threeBinding();
                        } else {
                            //微信和QQ绑定一样
                            threeQQANDWXBinding();
                        }
                    } else {
                        showShortToast(getString(R.string.net_error1));
                    }
                } else {
                    showShortToast("请输入正确的手机号");
                    return;
                }
                break;
            default:
                break;
        }
    }

    //验证码
    private void threeCode() {
        try {
            UserInfo userInfo = UserHelper.getUserInfo(BoundPhoneActivity.this);
            CommenApi api = Network.getInstance().getApi(CommenApi.class);
            Observable<NetResult<String>> netResultObservable;

            if (boundChannel == 0) {
                netResultObservable = api.threeSendCode(et_phonenum.getText().toString(),
                        userInfo.nickName,
                        URLEncoder.encode(TextUtils.isEmpty(userInfo.photo) ? "" : userInfo.photo, "utf-8"));
            } else {
                netResultObservable = api.threeSendCode(et_phonenum.getText().toString(),
                        URLEncoder.encode(TextUtils.isEmpty(userInfo.photo) ? "" : userInfo.photo, "utf-8"));
            }
            netResultObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<String>() {

                        @Override
                        public void success(String result) {
                            showShortToast(result);
                        }

                        @Override
                        public void error(int code, String msg) {

                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void threeBinding() {
        Network.getInstance()
                .getApi(CommenApi.class)
                .threeBinding(et_phonenum.getText().toString(), UserHelper.getUserInfo(BoundPhoneActivity.this).nickName, et_proving.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>() {

                    @Override
                    public void success(UserInfo result) {
                        UserHelper.saveUserInfo(BoundPhoneActivity.this, result);
                        RxBus.getDefault().post(Constant.login_success, "");
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void threeQQANDWXBinding() {
        String name = UserHelper.getUserInfo(BoundPhoneActivity.this).nickName;
        String photo = UserHelper.getUserInfo(BoundPhoneActivity.this).photo;

        Network.getInstance()
                .getApi(CommenApi.class)
                .threeWeiXinBinding(et_phonenum.getText().toString(),
                        UserHelper.getUserInfo(BoundPhoneActivity.this).openid,
                        et_proving.getText().toString(),
                        name, photo,
                        boundChannel + "")//QQ和微信绑定一样，
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>() {
                    @Override
                    protected void success(UserInfo data) {
                        UserHelper.saveUserInfo(BoundPhoneActivity.this, data);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    //退出淘宝
    private void exitTaobao() {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(BoundPhoneActivity.this, new LogoutCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoUtil.closeCode();
    }


}
