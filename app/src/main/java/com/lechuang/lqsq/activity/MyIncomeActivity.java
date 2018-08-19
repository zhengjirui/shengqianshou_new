package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
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
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnIncomeBean;
import com.lechuang.lqsq.bean.OwnNewsBean;
import com.lechuang.lqsq.fragments.IncomeBaseFragment;
import com.lechuang.lqsq.fragments.WoDeFragment;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.utils.DemoTradeCallback;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.widget.views.NumberRollingView;
import com.lechuang.lqsq.widget.views.PopChoseAgency;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 我的收益
 * <p>
 * 作者：li on 2017/10/6 14:37
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class MyIncomeActivity extends AppCompatActivity implements PopChoseAgency.OnChoseAgency {
    @BindView(R.id.iv_income_back)
    ImageView ivBackIncome;
    //    @BindView(R.id.tv_income_nickname)
//    TextView tvNickName;
    @BindView(R.id.tv_income_forecast_today)
    TextView tvForecastToday;
    @BindView(R.id.tv_income_total_today)
    TextView tvTotalToday;
    @BindView(R.id.tv_income_forecast_month)
    TextView tvForecastMonth;
    @BindView(R.id.tv_income_forecast_lastmonth)
    TextView tvForecastLastMonth;
    //    @BindView(R.id.iv_income_headImg)
//    XCRoundImageView ivHead;
    @BindView(R.id.tl_income_count)
    TabLayout tlCount;
    @BindView(R.id.vp_income_details)
    ViewPager vpDetails;
    @BindView(R.id.agency_income_count)
    LinearLayout mAgenctCount;
    @BindView(R.id.tv_orderDetail_list)
    TextView tvOrderDetail;

    // 代理:显示  非代理:隐藏
//    @BindView(R.id.income_my_team)
//    LinearLayout mTeam;
//    @BindView(R.id.income_integral_bill)
//    LinearLayout mFriends;

    @BindView(R.id.tv_integral_total)
    NumberRollingView tvIntegralTotal;
    @BindView(R.id.tv_income_integral_enable)
    TextView tvIntegralEnable;
    @BindView(R.id.tv_income_integral_disable)
    TextView tvIntegralDisable;
    @BindView(R.id.yaoqingjiangli)
    TextView yqjl;
    @BindView(R.id.xinzengrenshu)
    TextView xinzengrenshu;
    @BindView(R.id.dailishow)
    View dailishow;
    private Context mContext = MyIncomeActivity.this;

    private List<IncomeBaseFragment> fragments;
    //标题信息
    public String[] title = new String[]{"今日统计", "昨日统计", "近7日统计", "本月统计", "上月统计"};
    private IncomePaperAdapter mPaperAdapter;

    //打开页面的方法
    private AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);
    private Map exParams = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);
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

    private void initView() {
        if (UserHelper.isLogin() && UserHelper.getUserInfo(this).isAgencyStatus == 1) {
//            mTeam.setVisibility(View.VISIBLE);
//            mFriends.setVisibility(View.VISIBLE);
//            tvOrderDetail.setText("订单明细");
            dailishow.setVisibility(View.VISIBLE);
        } else {
//            mTeam.setVisibility(View.INVISIBLE);
//            mFriends.setVisibility(View.INVISIBLE);
//            tvOrderDetail.setText("积分账单");
            dailishow.setVisibility(View.GONE);
        }

        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改

        fragments = new ArrayList<>();
        int length = title.length;
        //创建页面
        for (int i = 0; i < length; i++) {
            fragments.add((IncomeBaseFragment) setTitle(new IncomeBaseFragment(), title[i], i + 1));
        }
        //设置适配器
        mPaperAdapter = new IncomePaperAdapter(getSupportFragmentManager());
        vpDetails.setAdapter(mPaperAdapter);
        vpDetails.setOffscreenPageLimit(6);
        /*//设置tablout 滑动模式
        tablayoutTopoff.setTabMode(TabLayout.MODE_SCROLLABLE);*/
        //联系tabLayout和viewpager
        tlCount.setupWithViewPager(vpDetails);
        tlCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 设置头目
     */
    private Fragment setTitle(Fragment fragment, String title, int i) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("type", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // onstart里面执行 提现积分回来后 可刷新积分信息
        initData();
    }

    public void initData() {

//        if (!se.getString("photo", "").equals("")) {
//            Glide.with(MyApplication.getInstance()).load(se.getString("photo", "")).error(R.drawable.pic_morentouxiang).into(ivHead);
//        }
//        tvNickName.setText(se.getString("nickName",""));
        getDataKefu(false);
        Network.getInstance().getApi(OwnApi.class)
                .ownIncome(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnIncomeBean>() {
                    @Override
                    public void success(OwnIncomeBean result) {
//                        Log.i(" ResultBack_successed", JSON.toJSONString(result));
                        try {
                            tvIntegralTotal.setUseCommaFormat(false);
                            tvIntegralTotal.setFrameNum(50);
                            tvIntegralTotal.setContent(result.record.sumIntegral);
                            tvIntegralEnable.setText(StringUtils.isZhengshu(result.record.withdrawalIntegral));
                            tvIntegralDisable.setText(StringUtils.isZhengshu(result.record.notWithdrawalIntegral));
                            tvForecastToday.setText("¥" + result.record.todayEstimatedIncome);
                            tvTotalToday.setText(result.record.todayVolum);
                            tvForecastMonth.setText(result.record.currentMonthIncome);
                            tvForecastLastMonth.setText(result.record.totalIncome);
                            yqjl.setText(TextUtils.isEmpty(result.record.inviteRewards) ? "￥0" : "￥" + result.record.inviteRewards);
                            xinzengrenshu.setText(TextUtils.isEmpty(result.record.newTeamNum) ? "0人" : result.record.newTeamNum + "人");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @OnClick({R.id.iv_income_back, R.id.income_my_team, R.id.income_order_detail,
            R.id.income_integral_bill, R.id.tv_integral_withdraw, R.id.kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_income_back:
                finish();
                break;
            case R.id.tv_integral_withdraw:
                // 积分提现
                startActivity(new Intent(mContext, JinfenReflectActivity.class));
                break;
            case R.id.income_my_team:
                if (UserHelper.isLogin() && UserHelper.getUserInfo(this).isAgencyStatus == 1) {
                    // 我的团队
                    startActivity(new Intent(MyIncomeActivity.this, WodeFensiActivity.class));
                } else {
                    // add: 2018/1/9 PopWindow become agency
                    new PopChoseAgency(mContext, MyIncomeActivity.this);
                }
                break;
            case R.id.income_order_detail:
                if (UserHelper.isLogin() && UserHelper.getUserInfo(this).isAgencyStatus == 1) {
//                     订单明细
                    startActivity(new Intent(MyIncomeActivity.this, DingDanMingXiActivity.class));
                } else {
                    // add: 2018/1/9 goto taobao order list
                    AlibcBasePage alibcBasePage = new AlibcMyOrdersPage(0, true);
                    AlibcTrade.show(MyIncomeActivity.this, alibcBasePage, alibcShowParams, null, exParams, new DemoTradeCallback());
                }
                break;
            case R.id.income_integral_bill:
                // 积分账单
                Intent intent = new Intent(this, ProfitActivity.class);
                startActivity(intent);
                break;
            case R.id.kefu:
                if (openImPassword == null) {
                    getDataKefu(true);
                    return;
                }
                goKeFu();
                break;
        }
    }


    @Override
    public void gotoAgency() {
        // 去成为代理
        startActivity(new Intent(MyIncomeActivity.this, ApplyAgentActivity.class));
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

    /**
     * 适配器
     */
    private class IncomePaperAdapter extends FragmentPagerAdapter {

        public IncomePaperAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getArguments().getString("title");
        }
    }


}
