package com.lechuang.lqsq.fragments;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.HelpCenterActivity;
import com.lechuang.lqsq.activity.JinfenReflectActivity;
import com.lechuang.lqsq.activity.LoginActivity;
import com.lechuang.lqsq.activity.MyIncomeActivity;
import com.lechuang.lqsq.activity.NewsActivity;
import com.lechuang.lqsq.activity.NormalWebActivity;
import com.lechuang.lqsq.activity.PaiHangBangActivity;
import com.lechuang.lqsq.activity.ProfitActivity;
import com.lechuang.lqsq.activity.SettingActivity;
import com.lechuang.lqsq.activity.ShareAppActivity;
import com.lechuang.lqsq.activity.ShareMoneyActivity;
import com.lechuang.lqsq.activity.SigneActivity;
import com.lechuang.lqsq.activity.UserInfoActivity;
import com.lechuang.lqsq.activity.WoDeZujiActivity;
import com.lechuang.lqsq.activity.WodeFensiActivity;
import com.lechuang.lqsq.activity.WodeShouCangActivity;
import com.lechuang.lqsq.bean.DuiBaBean;
import com.lechuang.lqsq.bean.OwnNewsBean;
import com.lechuang.lqsq.bean.SignBean;
import com.lechuang.lqsq.bean.UpdataInfoBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.glide.CropCircleTransformation;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.DemoTradeCallback;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.widget.dialog.JiFenShuoming;
import com.lechuang.lqsq.widget.dialog.RedPacketDialog;
import com.lechuang.lqsq.widget.views.NumberRollingView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/30
 * 时间：14:00
 * 描述：
 */

public class WoDeFragment extends BaseFragment {
    @BindView(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.shoujihao)
    TextView shoujihao;
    @BindView(R.id.nicheng)
    TextView nicheng;
    @BindView(R.id.jifen_num)
    NumberRollingView jifenNum;
    @BindView(R.id.userInfoRl)
    RelativeLayout userInfoRl;
    private Observable<String> login;
    private Observable<String> qd;
    private Map exParams = new HashMap<>();
    private AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);

    @Override
    protected int getLayoutId() {
        return R.layout.wode_fragment;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getUserData();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        login = RxBus.getDefault().register(Constant.login_success, String.class);
        login.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        setUserData();
                        getUserData();
                    }
                });
        qd = RxBus.getDefault().register(Constant.qiandao, String.class);
        qd.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        qiandao();
                    }
                });
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        if (UserHelper.isLogin())
            getUserData();
    }

    private void getUserData() {
        getDataKefu();
        getJiFen();
        getJifenShangChengUrl(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserHelper.isLogin())
            setUserData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && jifenNum != null &&
                UserHelper.getUserInfo(getActivity()) != null &&
                UserHelper.getUserInfo(getActivity()).sumIntegral != null) {
            jifenNum.setUseCommaFormat(false);
            jifenNum.setFrameNum(50);
            jifenNum.setContent(UserHelper.getUserInfo(getActivity()).sumIntegral);
        }
    }

    private void setUserData() {
        UserInfo userInfo = UserHelper.getUserInfo(getActivity());
        shoujihao.setText(userInfo.phone == null ? "" : userInfo.phone.replace(userInfo.phone.substring(3, 7), "****"));
        if (!TextUtils.isEmpty(userInfo.photo)) {
            Glide.with(this).load(userInfo.photo).bitmapTransform(new CropCircleTransformation(getActivity())).error(R.mipmap.touxiang).placeholder(R.mipmap.touxiang).into(touxiang);
        }
        nicheng.setText(userInfo.nickName == null ? "" : userInfo.nickName);
    }

    @Override
    public void onDestroyView() {
        RxBus.getDefault().unregister(Constant.login_success, login);
        RxBus.getDefault().unregister(Constant.qiandao, qd);
        super.onDestroyView();
    }

    @OnClick({R.id.touxiang, R.id.zjf_ll, R.id.jifen_num, R.id.shezhi, R.id.xiaoxi, R.id.qiandao, R.id.fenxiangapp, R.id.jifen, R.id.fensi, R.id.shoucang, R.id.kefu, R.id.dingdan, R.id.shouyi, R.id.tixian, R.id.shangcheng, R.id.zuji, R.id.paihangbang, R.id.bangzhu, R.id.taobao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.touxiang:
                if (UserHelper.isLogin())
                    UserInfoActivity.launchActivity(getActivity());
                break;
            case R.id.shezhi:
                SettingActivity.launchActivity(getActivity());
                break;
            case R.id.xiaoxi:
                NewsActivity.launchActivity(getActivity());
                break;
            case R.id.qiandao:
                qiandao();
                break;
            case R.id.fenxiangapp:
//                startActivity(new Intent(getActivity(), ShareMoneyActivity.class));
                startActivity(new Intent(getActivity(), ShareAppActivity.class));
                break;
            case R.id.jifen_num:
                Intent intent = new Intent(getActivity(), ProfitActivity.class);
                startActivity(intent);
                break;
            case R.id.zjf_ll:
                JiFenShuoming dialog = new JiFenShuoming(getActivity());
                dialog.show();
                break;
            case R.id.fensi:
                startActivity(new Intent(getActivity(), WodeFensiActivity.class));
                break;
            case R.id.shoucang:
                startActivity(new Intent(getActivity(), WodeShouCangActivity.class));
                break;
            case R.id.kefu:
                goKeFu();
                break;
            case R.id.dingdan:
                showTaoBaoOrderType();
                break;
            case R.id.shouyi:
                startActivity(new Intent(getActivity(), MyIncomeActivity.class));
                break;
            case R.id.tixian:
                startActivity(new Intent(getActivity(), JinfenReflectActivity.class));
                break;
            case R.id.shangcheng:
                getJifenShangChengUrl(true);
                break;
            case R.id.zuji:
                if (UserHelper.isLogin()) {
                    startActivity(new Intent(getActivity(), WoDeZujiActivity.class));
                } else {
                    LoginActivity.launchActivity(getActivity());
                }
                break;
            case R.id.paihangbang:
                startActivity(new Intent(getActivity(), PaiHangBangActivity.class));
                break;
            case R.id.bangzhu:
                HelpCenterActivity.launchActivity(getActivity());
                break;
            case R.id.taobao:
                final AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(getActivity(), new AlibcLoginCallback() {
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
                        UserInfo userInfo = UserHelper.getUserInfo(getActivity());
                        userInfo.taobaoNumber = nick;
                        UserHelper.saveUserInfo(getActivity(), userInfo);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    public void qiandao() {
        SignBean data = CacheManager.getInstance(getActivity()).get(UserHelper.getToken() + StringUtils.getDateStrForyyyy_MM_dd(System.currentTimeMillis()), SignBean.class);
        if (data != null) {
            List<SignBean.ListBean> list = data.list;
            SigneActivity.launchActivity(getActivity(), list, data.continuousSigned);
        } else {
            Network.getInstance().getApi(CommenApi.class)
                    .signSuccessed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<String>(WoDeFragment.this) {
                        @Override
                        protected void success(String data) {
                            ToastManager.getInstance().showShortToast(data);
                            getQiandaoInfo();
                        }

                        @Override
                        public void error(int code, String msg) {
                            getQiandaoInfo();
                        }
                    });
        }


    }

    private void getQiandaoInfo() {
        Network.getInstance().getApi(CommenApi.class)
                .sign()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<SignBean>(WoDeFragment.this) {
                    @Override
                    protected void success(SignBean data) {
                        CacheManager.getInstance(getActivity()).put(UserHelper.getToken() + StringUtils.getDateStrForyyyy_MM_dd(System.currentTimeMillis()), data);
                        List<SignBean.ListBean> list = data.list;
                        SigneActivity.launchActivity(getActivity(), list, data.continuousSigned);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    //用户密码
    private String openImPassword;
    //用户账户
    private String phone;
    //客服账号
    private String customerServiceId;
    public YWIMKit mIMKit;
    private boolean getkefuing = true;

    //网络获取数据
    private void getDataKefu() {
        Network.getInstance().getApi(OwnApi.class)
                .isUnread()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnNewsBean>(WoDeFragment.this) {
                    @Override
                    public void success(OwnNewsBean result) {
                        phone = result.appUsers.phone;
                        openImPassword = result.appUsers.openImPassword;
                        customerServiceId = result.appUsers.customerServiceId;
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getkefuing = false;
                        if (!getJifening && !getkefuing) {
                            scrollView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        getkefuing = false;
                        if (!getJifening && !getkefuing) {
                            scrollView.onRefreshComplete();
                        }
                        super.onCompleted();
                    }
                });
    }

    private boolean getJifening = true;

    private void getJiFen() {
        Network.getInstance().getApi(OwnApi.class)
                .userInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>(WoDeFragment.this) {
                    @Override
                    public void success(UserInfo result) {
                        UserInfo userInfo = UserHelper.getUserInfo(getActivity());
                        userInfo.isAgencyStatus = result.isAgencyStatus;
                        userInfo.sumIntegral = result.sumIntegral;
                        UserHelper.saveUserInfo(getActivity(), userInfo);
                        jifenNum.setUseCommaFormat(false);
                        jifenNum.setFrameNum(50);
                        jifenNum.setContent(result.sumIntegral);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getJifening = false;
                        if (!getJifening && !getkefuing) {
                            scrollView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        getJifening = false;
                        if (!getJifening && !getkefuing) {
                            scrollView.onRefreshComplete();
                        }
                        super.onCompleted();
                    }
                });
    }

    private String duibaUrl;

    private void getJifenShangChengUrl(final boolean isFromUser) {
        Network.getInstance().getApi(OwnApi.class)
                .getduiba()
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<DuiBaBean>() {
                    @Override
                    protected void success(DuiBaBean data) {
                        duibaUrl = data.duiBaLoginUrl;
                        if (isFromUser) {
                            if (data == null || TextUtils.isEmpty(data.duiBaLoginUrl)) {
                                ToastManager.getInstance().showLongToast("积分商城入口找不到");
                            } else {
                                NormalWebActivity.lanuchActivity(getActivity(), "积分商城", duibaUrl);
                            }
                        }
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (isFromUser) {
                            ToastManager.getInstance().showLongToast(msg);
                        }
                    }

                });
    }

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

    //淘宝信息  订单 售后 ....
    private void showTaoBaoOrderType() {
        AlibcBasePage alibcBasePage = new AlibcMyOrdersPage(0, true);
        AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, null, exParams, new DemoTradeCallback());
    }

    public void showFirstLoginDialog() {
        RedPacketDialog dialog = new RedPacketDialog(getActivity());
        dialog.show();
        UserInfo userInfo = UserHelper.getUserInfo(getActivity());
        userInfo.firstLoginFlag = 1;
        UserHelper.saveUserInfo(getActivity(), userInfo);
    }


}
