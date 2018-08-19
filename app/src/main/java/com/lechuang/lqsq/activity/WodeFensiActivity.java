package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.TeamBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/09
 * 时间：10:02
 * 描述：
 */

public class WodeFensiActivity extends BaseActivity {
    @BindView(R.id.zongzhi)
    TextView zongzhi;
    @BindView(R.id.qb_num)
    TextView qbNum;
    @BindView(R.id.yiji_num)
    TextView yijiNum;
    @BindView(R.id.erji_num)
    TextView erjiNum;
    @BindView(R.id.content)
    XRecyclerView content;
    public static final int selectedColor = Color.parseColor("#FF6c00");
    public static final int normalColor = Color.parseColor("#FFFFFF");
    @BindView(R.id.qb_fensi)
    TextView qbFensi;
    @BindView(R.id.yiji_fensi)
    TextView yijiFensi;
    @BindView(R.id.erji_fensi)
    TextView erjiFensi;
    private int currentIndex = 2;
    private TextView[] tvBt;
    private List<TeamBean.MineTeamBean.TeamSubBean> items = new ArrayList<>();
    private CommonRecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wode_fenxi;
    }

    @Override
    public void initView() {
        tvBt = new TextView[]{qbFensi, yijiFensi, erjiFensi};
    }

    @Override
    public void initData() {
        mAdapter = new CommonRecyclerAdapter(this, R.layout.fensi_item, items) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                final TeamBean.MineTeamBean.TeamSubBean mItem = (TeamBean.MineTeamBean.TeamSubBean) o;
                holder.setText(R.id.nicheng, mItem.nickname);
                //近三个月贡献隐藏
//                holder.setText(R.id.gongxian, mItem.contribution3);
                holder.setText(R.id.chengyuan, mItem.nextAgentCount);
                TextView caozuo = holder.getView(R.id.caozuo);
                if (!TextUtils.isEmpty(mItem.isAgencyStatus) && mItem.isAgencyStatus.equals("1")) {
                    caozuo.setText("查看成员");
                    caozuo.setTextColor(Color.WHITE);
                    caozuo.setBackgroundResource(R.drawable.fensi_item_bg);
                    caozuo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(WodeFensiActivity.this, MySecondFensiActivity.class).putExtra("userid", mItem.userIdStr));
                        }
                    });
                } else {
                    caozuo.setText("普通用户");
                    caozuo.setBackgroundColor(Color.TRANSPARENT);
                    caozuo.setTextColor(Color.parseColor("#838383"));
                }
                holder.setText(R.id.yaoqingjiangli, mItem.inviteRewards);
            }

        };
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        content.setLayoutManager(layoutManager);
        content.setAdapter(mAdapter);
//        content.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                switchData(currentIndex + 1);
            }

            @Override
            public void onLoadMore() {
                switchData(currentIndex + 1);
            }
        });
        swtich(0);
    }


    @OnClick({R.id.iv_income_back, R.id.qb_fensi, R.id.yiji_fensi, R.id.erji_fensi, R.id.tv_integral_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_income_back:
                finish();
                break;
            case R.id.qb_fensi:
                swtich(0);
                break;
            case R.id.yiji_fensi:
                swtich(1);
                break;
            case R.id.erji_fensi:
                swtich(2);
                break;
            case R.id.tv_integral_withdraw:
                startActivity(new Intent(this, JinfenReflectActivity.class));
                break;
        }
    }


    private void swtich(int index) {
        if (currentIndex == index) {
            return;
        }
        tvBt[index].setTextColor(selectedColor);
        tvBt[index].setSelected(true);
        tvBt[currentIndex].setTextColor(normalColor);
        tvBt[currentIndex].setSelected(false);
        currentIndex = index;
        page = 1;
        switchData(++index);
    }

    private void switchData(int index) {
        Network.getInstance().getApi(OwnApi.class)
                .mineTeam(page, index)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TeamBean>() {
                    @Override
                    protected void success(TeamBean data) {
                        content.refreshComplete();
                        if (data == null) {
                            return;
                        }
                        if (page == 1) {
                            items.clear();
                        }
                        items.addAll(data.record.list);
                        mAdapter.notifyDataSetChanged();
                        page++;
                        qbNum.setText(data.record.nextAgentCount + "人");
                        yijiNum.setText(data.record.members1 + "人");
                        erjiNum.setText(data.record.members2 + "人");
                        zongzhi.setText(data.record.sumContribution3 + "");
                    }

                    @Override
                    public void error(int code, String msg) {
                        content.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        content.refreshComplete();
                    }
                });
    }

}
