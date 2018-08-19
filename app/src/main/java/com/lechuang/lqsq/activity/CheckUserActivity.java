package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UpdataInfoBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.utils.TDevice;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：12:32
 * 描述：验证手机号
 */

public class CheckUserActivity extends BaseNormalTitleActivity {
    private final static int selectedColor = Color.parseColor("#ff5c19");
    private final static int normalColor = Color.parseColor("#cccccc");
    @BindView(R.id.sjh_et)
    EditText sjhEt;
    @BindView(R.id.yzm_et)
    EditText yzmEt;
    @BindView(R.id.getyzm)
    TextView getyzm;
    private int time = 60;
    private CountDownTimer countDownTimer;
    private int type;

    public static void launchActivity(Context context, int type) {
        context.startActivity(new Intent(context, CheckUserActivity.class).putExtra("type", type));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_user;
    }

    @Override
    public void initView() {
        setTitleName("验证手机号");
        setGetyzm(true);
        sjhEt.setText(UserHelper.getUserInfo(this).phone);
        sjhEt.setEnabled(false);
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", 0);
    }

    private void setGetyzm(boolean enable) {
        if (enable) {
            getyzm.setSelected(true);
            getyzm.setText("身份验证");
            getyzm.setTextColor(selectedColor);
            getyzm.setEnabled(true);
        } else {
            getyzm.setSelected(false);
            getyzm.setText("重新获取(60)");
            getyzm.setTextColor(normalColor);
            getyzm.setEnabled(false);
        }
    }

    @OnClick({R.id.getyzm, R.id.comlete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getyzm:
                getVerifyCode();
                break;
            case R.id.comlete:
                complete();
                break;
        }
    }

    private void getVerifyCode() {
        String phone = sjhEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(phone)) {
            showLongToast("请正确输入手机号");
            return;
        }
        Network.getInstance().getApi(CommenApi.class)
                .getCheckCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(CheckUserActivity.this) {
                    @Override
                    protected void success(String data) {
                        showLongToast(data);
                        countTime();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == Constant.need_relogin) {
                            RxBus.getDefault().post(Constant.userinfo_close, "");
                            finish();
                        }
                    }
                });
    }

    private void complete() {
        TDevice.hideSoftKeyboard(getWindow().getDecorView());
        final String sjh = sjhEt.getText().toString().trim();
        if (!StringUtils.isPhoneNum(sjh)) {
            showLongToast("请正确输入手机号");
            return;
        }
        String yzm = yzmEt.getText().toString().trim();
        if (TextUtils.isEmpty(yzm) || yzm.length() != 6) {
            showLongToast("请正确输入验证码");
            return;
        }
        Network.getInstance().getApi(CommenApi.class)
                .getVerifiCode(yzm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(CheckUserActivity.this) {
                    @Override
                    protected void success(String data) {
                        if (type == 1) {
                            UpdatePhoneActivity.launchActivity(CheckUserActivity.this);
                        } else if (type == 2) {
                            UserInfo userInfo = UserHelper.getUserInfo(CheckUserActivity.this);
                            if (TextUtils.isEmpty(userInfo.alipayNumber)) {
                                BindAlipayActivity.launchActivity(CheckUserActivity.this);
                            } else {
                                BindAlipaySuccessActivity.launchActivity(CheckUserActivity.this);
                            }
                        }
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == Constant.need_relogin) {
                            RxBus.getDefault().post(Constant.userinfo_close, "");
                            finish();
                        }
                    }
                });

    }

    private void countTime() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getyzm.setText("重新发送(" + --time + ")");
            }

            @Override
            public void onFinish() {
                time = 60;
                setGetyzm(true);
            }
        };
        setGetyzm(false);
        countDownTimer.start();
    }
}
