package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yrj on 2017/8/16.
 * 支付方式页面
 */

public class PayStyleActivity extends AppCompatActivity {
    //可以开始支付
    private static final int BEGIN_PAY = 1;
    //不能开始支付
    private static final int ERROR_PAY = 2;
    @BindView(R.id.back)
    ImageView ivBack;
    @BindView(R.id.cb_alipay)
    CheckBox cb_alipay;
    @BindView(R.id.ll_alipay)
    LinearLayout llAlipay;
    /* @BindView(R.id.cb_yuEpay)
     CheckBox cb_yuEpay;
     @BindView(R.id.ll_yuEpay)
     LinearLayout llYuEpay;
     @BindView(R.id.cb_wxpay)
     CheckBox cb_wxpay;
     @BindView(R.id.ll_wxpay)
     LinearLayout llWxpay;*/
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.btn_pay)
    Button btnPay;
    //储存支付方式的变量 0:支付宝支付 1:余额支付  2:微信支付
    private int payStyle = 0;

    private String orderNum;
    private String transaction_id;
    private String money;
    private Context mContext = PayStyleActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_style);
        ButterKnife.bind(this);
        initView();
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

    public void initView() {

        ((TextView) findViewById(R.id.tv_title)).setText("支付方式");
        ((TextView) findViewById(R.id.tv_money)).setText(String.format("¥%s", getIntent().getDoubleExtra("payPriceStr", 0)));
        //cb_wxpay.setBackgroundResource(R.drawable.icon_jinxuan);

    }

    @OnClick({R.id.iv_back, R.id.cb_alipay, R.id.ll_alipay, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_alipay://阿里支付
                payStyle = 0;
                selectPayStyle(payStyle);
                break;
           /* case R.id.ll_yuEpay: //余额支付
                payStyle = 1;
                selectPayStyle(payStyle);
                break;
            case R.id.ll_wxpay: //微信支付
                Utils.show(this,"抱歉,暂不支持微信支付");
                break;
            case R.id.cb_yuEpay:
                break;
            case R.id.cb_wxpay:
                break;*/
            case R.id.btn_pay:
                pay();
//                "http://192.168.1.210:8889/agency/agencySendOrder";
                /*if (Utils.isNetworkAvailable(mContext)) {
                    payDaoImpl.pay(mContext, new ApiRequest() {
                        @Override
                        public void onSuccess(int status, Object... object) throws JSONException {
                            if (status == Constants.STATE) {
                                //加签支付
                                String sign = (String) object[0];
                                final PayUtils pay = new PayUtils(PayStyleActivity.this, new PayUtils.onAliPayListener() {
                                    @Override
                                    public void onFailure(Object msg) {  //支付失败
                                        Utils.E("msg:" + msg);
                                        Intent intent = new Intent(PayStyleActivity.this, PayFailActivity.class);
                                        intent.putExtra("payPriceStr", getIntent().getStringExtra("payPriceStr"));
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Object msg) { //支付成功
                                        Utils.E("msg1:" + msg);
                                        try {
                                            JSONObject jsonObject = JSON.parseObject((String) msg);
                                            JSONObject object = (JSONObject) jsonObject.get("alipay_trade_app_pay_response");
                                            orderNum = (String) object.get("out_trade_no");
                                            money = (String) object.get("total_amount");
                                            transaction_id = (String) object.get("trade_no");
                                            //支付回调刷新代理状态
                                            payCallBack();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                pay.payV2(sign);
                            } else {
                                //失败
                                show((String) object[0]);
                            }
                        }

                        @Override
                        public void onFailure() {

                        }
                    });

                } else {
                    Utils.show(this,mContext.getResources().getString(R.string.net_error1));
                }
                // pay.payV2(null);*/

                break;
        }
    }

    private void pay() {
        Network.getInstance().getApi(OwnApi.class)
                .pay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnBean.Pay>() {
                    @Override
                    public void success(OwnBean.Pay result) {
                        final String sign = result.sign;

                        Runnable payThread = new Runnable() {
                            @Override
                            public void run() {
                                PayTask payTask = new PayTask(PayStyleActivity.this);
                                final Map<String, String> result = payTask.payV2(sign, true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String resultStatus = result.get("resultStatus");
                                        if (TextUtils.equals(resultStatus, "9000")) {
                                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                            String resultData = result.get("result");
                                            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject((String) resultData);
                                            com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) jsonObject.get("alipay_trade_app_pay_response");
                                            orderNum = (String) object.get("out_trade_no");
                                            money = (String) object.get("total_amount");
                                            transaction_id = (String) object.get("trade_no");
                                            map.put("transaction_type", "PAY");
                                            map.put("channel_type", "ALIPAY");
                                            map.put("transaction_id", transaction_id);
                                            map.put("transaction_fee", money);
                                            map.put("orderNum", orderNum);
                                            payCallBack();
                                        } else {
                                            ToastManager.getInstance().showShortToast(result.get("memo"));
                                            startActivity(new Intent(PayStyleActivity.this, PayFailActivity.class).putExtras(getIntent()));
                                            finish();
                                        }
                                    }
                                });
                            }
                        };

                        new Thread(payThread).start();

                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }


    HashMap<String, String> map = new HashMap<>();

    //支付回调刷新用户信息状态
    private void payCallBack() {
        Network.getInstance().getApi(OwnApi.class)
                .paySuccess(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>() {
                    @Override
                    public void success(String result) {
                        //跳转到支付成功页面
                        Intent intent = new Intent(PayStyleActivity.this, PaySuccessActivity.class);
                        intent.putExtra("money", getIntent().getDoubleExtra("payPriceStr", 0));
                        startActivity(intent);
                        UserInfo userInfo = UserHelper.getUserInfo(PayStyleActivity.this);
                        userInfo.isAgencyStatus = 1;
                        UserHelper.saveUserInfo(PayStyleActivity.this, userInfo);
                        RxBus.getDefault().post(Constant.login_success, "Y");
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    //选择支付方式
    private void selectPayStyle(int payStyleNum) {
//        if (payStyleNum == 0) {
//            cb_alipay.setChecked(true);
//            cb_yuEpay.setChecked(false);
//            cb_wxpay.setChecked(false);
//        } else if (payStyleNum == 1) {
//            cb_alipay.setChecked(false);
//            cb_yuEpay.setChecked(true);
//            cb_wxpay.setChecked(false);
//        } else if (payStyleNum == 2) {
//            cb_alipay.setChecked(false);
//            cb_yuEpay.setChecked(false);
//            cb_wxpay.setChecked(true);
//        } else {
//            cb_alipay.setChecked(true);
//            cb_yuEpay.setChecked(false);
//            cb_wxpay.setChecked(false);
//        }
        cb_alipay.setChecked(true);
        //cb_yuEpay.setChecked(false);
        //cb_wxpay.setChecked(false);
    }
}

