package com.lechuang.lqsq.activity;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.MyBaseListAdapter;
import com.lechuang.lqsq.bean.HeroBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.widget.views.NoScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：12:52
 * 描述：排行榜
 */

public class PaiHangBangActivity extends BaseNormalTitleActivity implements View.OnClickListener {
    @BindView(R.id.jinri)
    TextView jinri;
    @BindView(R.id.qiri)
    TextView qiri;
    @BindView(R.id.np)
    TextView np;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.listContent)
    NoScrollListView listContent;
    @BindView(R.id.scrollView)
    PullToRefreshScrollView scrollView;
    private List<HeroBean.ListBean> heroBean1, heroBean2;
    private HeroBean.ListBean heroBean1Top, heroBean2Top;
    private boolean show1 = false;
    private MyBaseListAdapter<HeroBean.ListBean> adapter;
    private View loading;
    //没有网络状态
    private LinearLayout ll_noNet;
    //刷新重试按钮
    private ImageView iv_tryAgain;

    @Override
    public int getLayoutId() {
        return R.layout.activity_phb;
    }

    @Override
    public void initView() {
        setTitleName("排行榜");
        switchshow();
        loading = findViewById(R.id.common_loading_all);
        ll_noNet = (LinearLayout) findViewById(R.id.ll_noNet);
        //刷新重试
        iv_tryAgain = (ImageView) findViewById(R.id.iv_tryAgain);
        iv_tryAgain.setOnClickListener(this);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (!show1)
                    switchshow();
                initData();
            }
        });
    }

    @Override
    public void initData() {
        subscription = Network.getInstance().getApi(OwnApi.class)
                .heroAgentInfo(1)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HeroBean>() {
                    @Override
                    protected void success(HeroBean data) {
                        heroBean1Top = data.list.get(0);
                        data.list.remove(0);
                        heroBean1 = data.list;
                        getQiri();
                    }

                    @Override
                    public void error(int code, String msg) {
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                        scrollView.onRefreshComplete();
                    }
                });
    }

    private void getQiri() {
        subscription = Network.getInstance().getApi(OwnApi.class)
                .heroAgentInfo(2)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HeroBean>() {
                    @Override
                    protected void success(HeroBean data) {
                        np.setText(heroBean1Top.nickName);
                        num.setText("￥" + heroBean1Top.sumIntegralStr);
                        loading.setVisibility(View.GONE);
                        ll_noNet.setVisibility(View.GONE);
                        heroBean2Top = data.list.get(0);
                        data.list.remove(0);
                        heroBean2 = data.list;
                        adapter =
                                new MyBaseListAdapter<HeroBean.ListBean>(heroBean1, PaiHangBangActivity.this) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        convertView = View.inflate(context, R.layout.phb_item, null);
                                        TextView mc = (TextView) convertView.findViewById(R.id.mc);
                                        TextView phone = (TextView) convertView.findViewById(R.id.phone);
                                        TextView name = (TextView) convertView.findViewById(R.id.name);
                                        TextView num = (TextView) convertView.findViewById(R.id.num);
                                        HeroBean.ListBean item = getItem(position);
                                        if (position == 0) {
                                            mc.setText("");
                                            mc.setBackgroundResource(R.mipmap.yajun);
                                        } else if (position == 1) {
                                            mc.setText("");
                                            mc.setBackgroundResource(R.mipmap.jijun);
                                        } else {
                                            mc.setText((position + 2) + "");
                                        }
                                        phone.setText(item.phone);
                                        name.setText(item.nickName);
                                        num.setText("￥" + item.sumIntegralStr);
                                        return convertView;
                                    }
                                };
                        listContent.setAdapter(adapter);
                    }

                    @Override
                    public void error(int code, String msg) {
                        loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                        scrollView.onRefreshComplete();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        scrollView.onRefreshComplete();
                    }
                });
    }

    @OnClick({R.id.jinri, R.id.qiri})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jinri:
                if (show1) {
                    return;
                }
                switchshow();
                break;
            case R.id.qiri:
                if (!show1) {
                    return;
                }
                switchshow();
                break;
        }
    }

    private void switchshow() {
        if (show1) {
            qiri.setSelected(true);
            jinri.setSelected(false);
            qiri.setTextColor(Color.parseColor("#FFFFFF"));
            jinri.setTextColor(Color.parseColor("#696969"));
            if (adapter != null) {
                adapter.setDatas(heroBean2);
                np.setText(heroBean2Top.nickName);
                num.setText("￥" + heroBean2Top.sumIntegralStr);
            }
        } else {
            jinri.setSelected(true);
            qiri.setSelected(false);
            jinri.setTextColor(Color.parseColor("#FFFFFF"));
            qiri.setTextColor(Color.parseColor("#696969"));
            if (adapter != null) {
                adapter.setDatas(heroBean1);
                np.setText(heroBean1Top.nickName);
                num.setText("￥" + heroBean1Top.sumIntegralStr);
            }
        }
        show1 = !show1;
    }

    @Override
    public void onClick(View v) {
        loading.setVisibility(View.VISIBLE);
        ll_noNet.setVisibility(View.GONE);
        initData();
    }
}
