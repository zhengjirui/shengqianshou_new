package com.lechuang.lqsq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnJiFenInfoBean;
import com.lechuang.lqsq.bean.OwnNewsBean;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.views.ClearEditText;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：10:16
 * 描述：积分提现
 */

public class JinfenReflectActivity extends BaseNormalTitleActivity implements View.OnClickListener {
    private ClearEditText et_money;
    private TextView tv_jifen;  //可提现积分
    private TextView tv_money;  //可提现金额
    private TextView tv_rule;   //提现规则  1元 = ?积分
    private TextView tv_tixian_min; //最小提现金额
    private TextView tv_ali;        //支付宝账号
    private Button btn_tixian;      //提现按钮
    //可提现金额
    private double withDrawMoney;
    //最小提现金额
    private int minWithDrawMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_jinfen_reflect;
    }

    @Override
    public void initView() {
        setTitleName("积分提现");
        String alipayNumber = UserHelper.getUserInfo(this).alipayNumber;
        ((TextView) findViewById(R.id.tv_ali)).setText("支付宝账号  (" + (TextUtils.isEmpty(alipayNumber) ? "未绑定" : alipayNumber) + ")");
        et_money = (ClearEditText) findViewById(R.id.et_money);
        findViewById(R.id.btn_tx).setOnClickListener(this);
        findViewById(R.id.ll_go_alipay).setOnClickListener(this);
        findViewById(R.id.kefu).setOnClickListener(this);
        getDataKefu(false);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        jifenInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tx: //提现
                //是否有网络
                if (Utils.isNetworkAvailable(this)) {

                    //是否绑定支付宝账号
                    String alipayNumber = UserHelper.getUserInfo(this).alipayNumber;
                    if (TextUtils.isEmpty(alipayNumber)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(JinfenReflectActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您还未绑定支付宝,前去绑定?");
                        dialog.setNegativeButton("考虑一下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setPositiveButton("前去绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckUserActivity.launchActivity(JinfenReflectActivity.this, 2);
                                finish();
                                return;
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                        return;
                        //showShortToast("未绑定支付宝账号,请先绑定支付宝账号~");
                    }
                    String money = et_money.getText().toString().trim();
                    if (Utils.isEmpty(et_money)) {
                        showShortToast("提现金额不能为空");
                        return;
                    }
                    //输入的金额大于用户可提现的金额
                    if (Double.parseDouble(money) > withDrawMoney) {
                        showShortToast("提现金额超过可提现的总金额");
                        return;
                    }
                    //提现的金额小于最小的提现金额
                    if (Double.parseDouble(money) < minWithDrawMoney) {
                        showShortToast("提现金额小于最小可提现金额");
                        return;
                    }
                    //提现的金额小于最小的提现金额
                    if (Double.parseDouble(money) <= 0) {
                        showShortToast("提现金额不能小于0");
                        return;
                    }
                    //提现
                    withDraw(Double.parseDouble(money));
                } else {
                    showShortToast(getString(R.string.net_error1));
                }

                break;
            case R.id.ll_go_alipay:
                CheckUserActivity.launchActivity(this, 2);
                break;
            case R.id.kefu:
                if (openImPassword == null) {
                    getDataKefu(true);
                    return;
                }
                goKeFu();
                break;
            default:
                break;
        }

    }
    //用户密码
    private String openImPassword;
    //用户账户
    private String phone;
    //客服账号
    private String customerServiceId;

    //网络获取数据
    private void getDataKefu(final boolean isFromClick) {
        Network.getInstance().getApi(OwnApi.class)
                .isUnread()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnNewsBean>() {
                    @Override
                    public void success(OwnNewsBean result) {
                        phone = result.appUsers.phone;
                        openImPassword = result.appUsers.openImPassword;
                        customerServiceId = result.appUsers.customerServiceId;
                        if (isFromClick)
                            goKeFu();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    public YWIMKit mIMKit;

    private void goKeFu() {
        if (phone != null && openImPassword != null && customerServiceId != null) {
            //此实现不一定要放在Application onCreate中
            //此对象获取到后，保存为全局对象，供APP使用
            //此对象跟用户相关，如果切换了用户，需要重新获取
            mIMKit = YWAPI.getIMKitInstance(phone, Constant.APP_KEY);
            //开始登录
            IYWLoginService loginService = mIMKit.getLoginService();
            YWLoginParam loginParam = YWLoginParam.createLoginParam(phone, openImPassword);
            loginService.login(loginParam, new IWxCallback() {

                @Override
                public void onSuccess(Object... arg0) {
                    //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
                    EServiceContact contact = new EServiceContact(customerServiceId, 0);
                    //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                    //的setNeedByPass方法，参数为false。
                    contact.setNeedByPass(false);
                    Intent intent = mIMKit.getChattingActivityIntent(contact);
                    startActivity(intent);
                }

                @Override
                public void onProgress(int arg0) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onError(int errCode, String description) {
                    //如果登录失败，errCode为错误码,description是错误的具体描述信息
//                                    Utils.show(mContext, description);
                    ToastManager.getInstance().showShortToast(description);
                }
            });
        } else {
            ToastManager.getInstance().showShortToast("网络异常，请检查网络");
        }
    }

    //积分信息
    private void jifenInfo() {
        if (Utils.isNetworkAvailable(this)) {
            Network.getInstance().getApi(OwnApi.class)
                    .jifenInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<OwnJiFenInfoBean>(JinfenReflectActivity.this) {
                        @Override
                        public void success(OwnJiFenInfoBean result) {
                            if (result == null)
                                return;
                            String number = UserHelper.getUserInfo(JinfenReflectActivity.this).alipayNumber;
                            if (TextUtils.isEmpty(number)) {
                                number = "未绑定";
                            }
                            ((TextView) findViewById(R.id.tv_ali)).setText("支付宝账号  (" + number + ")");
                            //积分提现规则 1元 = ?积分
                            ((TextView) findViewById(R.id.tv_rule)).setText("1元=" + result.integralRate + "积分");
                            //用户可提现积分
                            ((TextView) findViewById(R.id.tv_jifen)).setText(StringUtils.isZhengshu(result.withdrawIntegral) + "积分");
                            //用户可提现金额
                            withDrawMoney = result.withdrawPrice;
                            ((TextView) findViewById(R.id.tv_money)).setText(result.withdrawPrice + "元");
                            //最小提现金额
                            minWithDrawMoney = result.withdrawMinPrice;
                            ((TextView) findViewById(R.id.tv_tixian_min)).setText("满" + result.withdrawMinPrice + "元可提现");
                            ((TextView) findViewById(R.id.tv_reflect_remind)).setText(result.cashDeclaration);

                        }

                        @Override
                        public void error(int code, String msg) {

                        }
                    });
        } else {
            showShortToast(getString(R.string.net_error));
        }
    }

    //提现
    private void withDraw(double money) {
        Network.getInstance().getApi(OwnApi.class)
                .jifenTx(money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(JinfenReflectActivity.this) {
                    @Override
                    public void success(String result) {
                        showShortToast(result);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }
}
