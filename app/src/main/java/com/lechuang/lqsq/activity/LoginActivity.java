package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.DataBean;
import com.lechuang.lqsq.bean.TaobaoLoginBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.utils.TDevice;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/27
 * 时间：09:08
 * 描述：登录与注册
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_tv)
    TextView loginTv;
    @BindView(R.id.login_line)
    View loginLine;
    @BindView(R.id.reg_tv)
    TextView regTv;
    @BindView(R.id.reg_line)
    View regLine;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.ver_et)
    EditText verEt;
    @BindView(R.id.getVer)
    TextView getVer;
    @BindView(R.id.verrl)
    RelativeLayout verrl;
    @BindView(R.id.pass_et)
    EditText passEt;
    @BindView(R.id.delu_rl)
    RelativeLayout deluRl;
    @BindView(R.id.button)
    TextView button;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.bottomll)
    LinearLayout bottomll;

    private boolean isLogin;
    public static boolean startups = false;
    private int time = 60;
    private CountDownTimer countDownTimer;

    public synchronized static void launchActivity(Context context) {
        if (startups) {
            return;
        }
        startups = true;
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        isLogin = true;
        showDL();
    }

    @Override
    public void initData() {
        UserHelper.clearUserToken(this);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
        startups = false;
    }


    @OnClick({R.id.reg_ly, R.id.login_ly, R.id.getVer, R.id.userxieyi, R.id.getPass, R.id.button,R.id.wx_lg1,R.id.qq_lg1, R.id.tb_lg1, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_ly:
                if (isLogin)
                    showREG();
                TDevice.hideSoftKeyboard(getWindow().getDecorView());
                break;
            case R.id.login_ly:
                if (!isLogin) {
                    if (isCountTime) {
                        countDownTimer.cancel();
                        isCountTime = false;
                        time = 60;
                        getVer.setEnabled(true);
                        getVer.setText("获取验证码");
                        getVer.setTextColor(Color.WHITE);
                    }
                    showDL();
                }
                TDevice.hideSoftKeyboard(getWindow().getDecorView());
                break;
            case R.id.getVer:
                getVerCode();
                break;
            case R.id.userxieyi:
                NormalWebActivity.lanuchActivity(this, "用户协议", QUrl.userAgreement);
                break;
            case R.id.getPass:
                startActivity(new Intent(this, GetPass.class));
                break;
            case R.id.button:
                TDevice.hideSoftKeyboard(getWindow().getDecorView());
                if (isLogin)
                    login();
                else
                    regUser();
                break;
            case R.id.tb_lg1:
                taobaoLogin();
                break;
            case R.id.wx_lg1:
                WeiXin();
                break;
            case R.id.qq_lg1:
                qqLogin();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void showDL() {
        isLogin = true;
        content.setBackgroundResource(R.mipmap.delu_bg);
        loginTv.setTextColor(Color.parseColor("#FF5C19"));
        loginLine.setVisibility(View.VISIBLE);
        regTv.setTextColor(Color.parseColor("#848484"));
        regLine.setVisibility(View.INVISIBLE);
        verrl.setVisibility(View.GONE);
        deluRl.setVisibility(View.VISIBLE);
        bottomll.setVisibility(View.VISIBLE);
        button.setText("登录");

    }

    private void showREG() {
        isLogin = false;
        content.setBackgroundResource(R.mipmap.content_bg);
        regTv.setTextColor(Color.parseColor("#FF5C19"));
        regLine.setVisibility(View.VISIBLE);
        loginTv.setTextColor(Color.parseColor("#848484"));
        loginLine.setVisibility(View.INVISIBLE);
        verrl.setVisibility(View.VISIBLE);
        verrl.setVisibility(View.VISIBLE);
        deluRl.setVisibility(View.GONE);
        bottomll.setVisibility(View.GONE);
        button.setText("完成");
        getVer.setTextColor(Color.WHITE);
    }

    private void login() {
        String phone = phoneEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(phone)) {
            showLongToast("请正确输入手机号");
            return;
        }
        String pass = passEt.getText().toString().trim();
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            showLongToast("请正确输入密码");
            return;
        }
        pass = StringUtils.md5(pass);
        showWaitDialog("正在登录...").setCancelable(false);
        //开始登陆
        Network.getInstance().getApi(CommenApi.class)
                .login(phone, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<DataBean>(LoginActivity.this) {
                    @Override
                    protected void success(DataBean data) {
                        UserHelper.saveUserInfo(LoginActivity.this, data.user);
                        showLongToast("登陆成功");
                        RxBus.getDefault().post(Constant.login_success, "");
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void regUser() {
        String phone = phoneEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(phone)) {
            showLongToast("请正确输入手机号");
            return;
        }
        String verCode = verEt.getText().toString().trim();
        if (TextUtils.isEmpty(verCode) || verCode.length() != 6) {
            showLongToast("请正确输入验证码");
            return;
        }
        String pass = passEt.getText().toString().trim();
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            showLongToast("请正确输入密码");
            return;
        }
        pass = StringUtils.md5(pass);
        showWaitDialog("正在注册...").setCancelable(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pass);
        map.put("verifiCode", verCode);
        //开始注册
        subscription = Network.getInstance().getApi(CommenApi.class)
                .register(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(LoginActivity.this) {
                    @Override
                    protected void success(String data) {
                        showShortToast(data);
                        showDL();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void getVerCode() {
        String phone = phoneEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(phone)) {
            showLongToast("请正确输入手机号");
            return;
        }
        subscription = Network.getInstance().getApi(CommenApi.class)
                .threeBind(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(LoginActivity.this) {
                    @Override
                    protected void success(String data) {
                        showLongToast(data);
                        countTime();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    private boolean isCountTime = false;

    private void countTime() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getVer.setText("重新发送(" + --time + ")");
            }

            @Override
            public void onFinish() {
                isCountTime = false;
                time = 60;
                getVer.setEnabled(true);
                getVer.setText("获取验证码");
                getVer.setTextColor(Color.WHITE);
            }
        };
        getVer.setTextColor(Color.parseColor("#999999"));
        getVer.setEnabled(false);
        countDownTimer.start();
        isCountTime = true;
    }

    private void taobaoLogin() {
        boundChannel = 0;
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(this, new AlibcLoginCallback() {

            @Override
            public void onFailure(int i, String s) {

            }

            @Override
            public void onSuccess() {
                Session taobao = alibcLogin.getSession();
                UserInfo userInfo = new UserInfo();
                userInfo.photo = taobao.avatarUrl;
                userInfo.nickName = taobao.nick;
                UserHelper.saveUserInfo(LoginActivity.this, userInfo);
                threeLogin(userInfo.nickName);
            }
        });


    }

    /**
     * @author li
     * 邮箱：961567115@qq.com
     * @time 2017/10/5  19:24
     * @describe 绑定手机号
     */
    public void threeLogin(String params) {
        Network.getInstance().getApi(CommenApi.class)
                .threeLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>() {

                    @Override
                    public void success(UserInfo result) {  //代表之前绑定过手机号码
                        //用户信息
                        //登录状态设为true
                        if (result == null)
                            return;
                        //绑定的支付宝号
                        UserHelper.saveUserInfo(LoginActivity.this, result);
                        RxBus.getDefault().post(Constant.login_success, "");
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == 300) {    //第一次登录淘宝账号,要绑定手机号
                            showShortToast(msg);
                            startActivity(new Intent(LoginActivity.this, BoundPhoneActivity.class).putExtra("boundChannel", boundChannel));
                            finish();
                        }
                    }
                });

    }

    private int boundChannel;

    /**
     * 微信QQ的登录监听
     */
    private UMAuthListener mUMAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("----",data.toString());
            Toast.makeText(MyApplication.getContext(), "成功了", Toast.LENGTH_LONG).show();

            UserInfo userInfo = new UserInfo();
            userInfo.photo = data.get("iconurl");
            userInfo.nickName = data.get("name");
            userInfo.openid = data.get("openid");
            UserHelper.saveUserInfo(LoginActivity.this, userInfo);
            vertifyIsBound(userInfo.openid);
        }
        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(MyApplication.getContext(), "失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MyApplication.getContext(), "取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * qq登录
     */
    private void qqLogin(){
        boundChannel = 2;
        UMShareAPI.get(MyApplication.getContext()).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, mUMAuthListener);
    }

    /**
     * 微信登录
     */
    private void WeiXin(){
        boundChannel = 1;
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(MyApplication.getContext()).setShareConfig(config);
        UMShareAPI.get(MyApplication.getContext()).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, mUMAuthListener);
    }

    private void vertifyIsBound(String openId) {
        Network.getInstance().getApi(CommenApi.class)
                .threeLoginQQANDWX(openId,boundChannel + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>() {
                    @Override
                    protected void success(UserInfo data) {
                        //用户信息
                        //登录状态设为true
                        if (data == null)
                            return;
                        UserHelper.saveUserInfo(LoginActivity.this, data);
                        showLongToast("登陆成功");
                        RxBus.getDefault().post(Constant.login_success, "");
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == 300) {    //绑定手机号
                            showShortToast(msg);
                            startActivity(new Intent(LoginActivity.this, BoundPhoneActivity.class).putExtra("boundChannel", boundChannel));
                            finish();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }


}
