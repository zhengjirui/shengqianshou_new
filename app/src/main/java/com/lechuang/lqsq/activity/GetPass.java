package com.lechuang.lqsq.activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.StringUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/27
 * 时间：14:07
 * 描述：找回密码
 */

public class GetPass extends BaseActivity {
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.ver_et)
    EditText verEt;
    @BindView(R.id.getVer)
    TextView getVer;
    @BindView(R.id.pass_et)
    EditText passEt;

    private int time = 60;
    private CountDownTimer countDownTimer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_getpass;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.close, R.id.getVer, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.getVer:
                getVerCode();
                break;
            case R.id.button:
                complete();
                break;
        }
    }

    private void getVerCode() {
        String phone = phoneEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(phone)) {
            showLongToast("请正确输入手机号");
            return;
        }
        subscription = Network.getInstance().getApi(CommenApi.class)
                .findCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(GetPass.this) {
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

    private void complete() {
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
        showWaitDialog("正在处理...").setCancelable(false);
        HashMap map = new HashMap();
        map.put("phone", phone);
        map.put("password", pass);
        map.put("verifiCode", verCode);
        Network.getInstance().getApi(CommenApi.class)
                .findPwd(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(GetPass.this) {
                    @Override
                    protected void success(String data) {
                        showLongToast(data);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void countTime() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getVer.setText("重新发送(" + --time + ")");
            }

            @Override
            public void onFinish() {
                time = 60;
                getVer.setEnabled(true);
                getVer.setText("获取验证码");
                getVer.setTextColor(Color.parseColor("#FF5C19"));
            }
        };
        getVer.setTextColor(Color.parseColor("#999999"));
        getVer.setEnabled(false);
        countDownTimer.start();
    }
}
