package com.lechuang.lqsq.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.MyAdapterNew;
import com.lechuang.lqsq.bean.OrderBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/24
 * 时间：10:28
 * 描述：
 */

public class DingDanMingXiActivity extends BaseNormalTitleActivity implements View.OnClickListener {
    @BindView(R.id.content)
    XRecyclerView content;
    private LinearLayout header;
    private CommonRecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private List<OrderBean.OrderDetail> datas = new ArrayList<>();
    private TextView zsj, wode, yiji, erji, wodedesc, yijidesc, erjidesc;
    private View ll_wode, ll_yiji, ll_erji, all, youxiao, shixiao, line1, line2, line3, youxiao_fenlei;
    private TextView shouhuo, fukuan, jiesuan;
    private int type1 = 1;
    private String type2;
    private TextView[] tvs;
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ddmx;
    }

    @Override
    public void initView() {
        setTitleName("订单明细");
        initHeader();
        adapter = new CommonRecyclerAdapter(this, R.layout.ddmx_item, datas) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                OrderBean.OrderDetail bean = (OrderBean.OrderDetail) o;
                holder.setText(R.id.orderid, "订单号：" + bean.orderNum);
                holder.setText(R.id.ordertime, "下单时间：" + bean.createTime);
                holder.setText(R.id.desc, bean.sourceText);
                holder.setText(R.id.yongjin, TextUtils.isEmpty(bean.payClearingIncome) ? "佣金收入￥0" : bean.payClearingIncome);
                holder.setText(R.id.orderstate, bean.orderStatus);
                TextView state = holder.getView(R.id.orderstate);
                if (bean.orderStatus.equals("已结算")) {
                    state.setBackgroundResource(R.drawable.order_state_js);
                } else if (bean.orderStatus.equals("已收货")) {
                    state.setBackgroundResource(R.drawable.order_state_sh);
                } else if (bean.orderStatus.equals("已付款")) {
                    state.setBackgroundResource(R.drawable.order_state_fk);
                } else if (bean.orderStatus.equals("已失效")) {
                    state.setBackgroundResource(R.drawable.order_state_sx);
                }
            }


        };
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        content.setLayoutManager(manager);
        content.setAdapter(adapter);
        content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                switchJi(0);
            }

            @Override
            public void onLoadMore() {
                getData(type1, type2);
            }
        });
        content.addHeaderView(header);
    }

    private void initHeader() {
        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dingdanmingxi_header, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        header.setLayoutParams(lp);
        zsj = (TextView) header.findViewById(R.id.zongshouyi);
        wode = (TextView) header.findViewById(R.id.wode);
        yiji = (TextView) header.findViewById(R.id.yiji);
        erji = (TextView) header.findViewById(R.id.erji);
        wodedesc = (TextView) header.findViewById(R.id.wodeDesc);
        yijidesc = (TextView) header.findViewById(R.id.yijiDesc);
        erjidesc = (TextView) header.findViewById(R.id.erjiDesc);
        ll_wode = header.findViewById(R.id.ll_wode);
        ll_yiji = header.findViewById(R.id.ll_yiji);
        ll_erji = header.findViewById(R.id.ll_erji);
        wodedesc.setSelected(true);
        yijidesc.setSelected(false);
        erjidesc.setSelected(false);
        all = header.findViewById(R.id.all);
        youxiao = header.findViewById(R.id.youxiao);
        shixiao = header.findViewById(R.id.shixiao);
        line1 = header.findViewById(R.id.line1);
        line2 = header.findViewById(R.id.line2);
        line3 = header.findViewById(R.id.line3);

        youxiao_fenlei = header.findViewById(R.id.youxiao_fenlei);
        shouhuo = (TextView) header.findViewById(R.id.shouhuo);
        fukuan = (TextView) header.findViewById(R.id.fukuan);
        jiesuan = (TextView) header.findViewById(R.id.jiesuan);
        shouhuo.setOnClickListener(this);
        fukuan.setOnClickListener(this);
        jiesuan.setOnClickListener(this);
        ll_wode.setOnClickListener(this);
        ll_yiji.setOnClickListener(this);
        ll_erji.setOnClickListener(this);
        all.setOnClickListener(this);
        youxiao.setOnClickListener(this);
        shixiao.setOnClickListener(this);
        tvs = new TextView[]{wode, yiji, erji, wodedesc, yijidesc, erjidesc};
        switchJi(0);
    }

    @Override
    public void initData() {
//        getData(type1, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                youxiao_fenlei.setVisibility(View.GONE);
                page = 1;
                getData(type1, "");
                break;
            case R.id.youxiao:
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                youxiao_fenlei.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(type2)) {
                    type2 = "2";
                }
                if (type2.equals("3")) {
                    shouhuo.setSelected(true);
                    shouhuo.setTextColor(Color.WHITE);
                    fukuan.setSelected(false);
                    fukuan.setTextColor(Color.parseColor("#444444"));
                    jiesuan.setSelected(false);
                    jiesuan.setTextColor(Color.parseColor("#444444"));
                } else if (type2.equals("4")) {
                    shouhuo.setSelected(false);
                    shouhuo.setTextColor(Color.parseColor("#444444"));
                    fukuan.setSelected(false);
                    fukuan.setTextColor(Color.parseColor("#444444"));
                    jiesuan.setSelected(true);
                    jiesuan.setTextColor(Color.WHITE);
                } else if (type2.equals("2")) {
                    shouhuo.setSelected(false);
                    shouhuo.setTextColor(Color.parseColor("#444444"));
                    fukuan.setSelected(true);
                    fukuan.setTextColor(Color.WHITE);
                    jiesuan.setSelected(false);
                    jiesuan.setTextColor(Color.parseColor("#444444"));
                } else {
                    type2 = "2";
                    shouhuo.setSelected(false);
                    shouhuo.setTextColor(Color.parseColor("#444444"));
                    fukuan.setSelected(true);
                    fukuan.setTextColor(Color.WHITE);
                    jiesuan.setSelected(false);
                    jiesuan.setTextColor(Color.parseColor("#444444"));
                }
                page = 1;
                getData(type1, type2);
                break;
            case R.id.shixiao:
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                youxiao_fenlei.setVisibility(View.GONE);
                page = 1;
                getData(type1, "5");
                break;
            case R.id.ll_wode:
                switchJi(0);
                break;
            case R.id.ll_yiji:
                switchJi(1);
                break;
            case R.id.ll_erji:
                switchJi(2);
                break;
            case R.id.jiesuan:
                page = 1;
                shouhuo.setSelected(false);
                shouhuo.setTextColor(Color.parseColor("#444444"));
                fukuan.setSelected(false);
                fukuan.setTextColor(Color.parseColor("#444444"));
                jiesuan.setSelected(true);
                jiesuan.setTextColor(Color.WHITE);
                getData(type1, "4");
                break;
            case R.id.fukuan:
                page = 1;
                shouhuo.setSelected(false);
                shouhuo.setTextColor(Color.parseColor("#444444"));
                fukuan.setSelected(true);
                fukuan.setTextColor(Color.WHITE);
                jiesuan.setSelected(false);
                jiesuan.setTextColor(Color.parseColor("#444444"));
                getData(type1, "2");
                break;
            case R.id.shouhuo:
                page = 1;
                shouhuo.setSelected(true);
                shouhuo.setTextColor(Color.WHITE);
                fukuan.setSelected(false);
                fukuan.setTextColor(Color.parseColor("#444444"));
                jiesuan.setSelected(false);
                jiesuan.setTextColor(Color.parseColor("#444444"));
                getData(type1, "3");
                break;
        }
    }

    private int currentIndex = 1;

    private void switchJi(int index) {
        if (currentIndex == index) return;
        tvs[currentIndex].setTextColor(Color.parseColor("#848484"));
        tvs[currentIndex + 3].setBackgroundColor(Color.TRANSPARENT);
        tvs[currentIndex + 3].setTextColor(Color.parseColor("#848484"));
        tvs[index].setTextColor(Color.parseColor("#ff5c19"));
        tvs[index + 3].setBackgroundResource(R.drawable.ddmx_fs_bg);
        tvs[index + 3].setTextColor(Color.WHITE);
        currentIndex = index;
        type1 = index + 1;
        page = 1;
        getData(type1, "");
    }

    private void getData(int type1, String type2) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        if (this.type2 == null) {
            this.type2 = type2;
        }
        if (!TextUtils.isEmpty(type2)) {
            this.type2 = type2;
        }
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        if (!TextUtils.isEmpty(this.type2)) {
            map.put("type", this.type2);
        }

        map.put("flag", type1 + "");
        subscription = Network.getInstance().getApi(OwnApi.class)
                .orderDetails(map)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OrderBean>() {
                    @Override
                    protected void success(OrderBean data) {
                        zsj.setText(data.sumIncome);
                        wode.setText("￥" + data.ownIncome);
                        yiji.setText("￥" + data.agencyIncome);
                        erji.setText("￥" + data.nextAgencyIncome);
                        content.refreshComplete();
                        if (data == null) return;
                        if (page == 1) {
                            datas.clear();
                        }
                        datas.addAll(data.orderList);
                        adapter.notifyDataSetChanged();
                        page++;
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

}
