package com.lechuang.lqsq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.TeamNextBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.widget.views.XCRoundImageView;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/10
 * 时间：14:15
 * 描述：
 */

public class MySecondFensiActivity extends BaseNormalTitleActivity {
    @BindView(R.id.touxiang)
    XCRoundImageView touxiang;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.content)
    XRecyclerView content;
    @BindView(R.id.common_nothing_data)
    RelativeLayout nothingData;
    private int page = 1;
    private CommonRecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<TeamNextBean.TeamNext.TeamMember> items = new ArrayList<>();
    private String userId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_second_team;
    }

    @Override
    public void initView() {
        mAdapter = new CommonRecyclerAdapter(this, R.layout.second_fensi_item, items) {
            @Override
            public void convert(ViewHolderRecycler holder, Object o) {
                TeamNextBean.TeamNext.TeamMember bean = (TeamNextBean.TeamNext.TeamMember) o;
                holder.displayImage(R.id.touxiang, bean.photo, R.mipmap.touxiang);
                holder.setText(R.id.name, bean.nickname);
                holder.setText(R.id.time, bean.joinTime);
                holder.setText(R.id.jiangli, bean.nextAgentCount);
                //近三个月贡献隐藏
//                holder.setText(R.id.gongxian, bean.contribution3);
            }

        };
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        content.setLayoutManager(layoutManager);
        content.setAdapter(mAdapter);
        content.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
    }

    @Override
    public void initData() {
        userId = getIntent().getStringExtra("userid");
        getData();
    }

    public void back(View view) {
        finish();
    }


    private void getData() {
        subscription = Network.getInstance().getApi(OwnApi.class)
                .nextTeam(userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TeamNextBean>() {
                    @Override
                    protected void success(TeamNextBean result) {
                        if (result == null) {
                            content.refreshComplete();
                            content.noMoreLoading();
                            if (page == 1)
                                content.setVisibility(View.GONE);
                            return;
                        }
                        if (page == 1) {
                            items.clear();
                            name.setText(result.record.nickname);
//                            num.setText(result.record.contributionSum);
                            Glide.with(MySecondFensiActivity.this).load(result.record.photo).error(R.mipmap.touxiang).placeholder(R.mipmap.touxiang).into(touxiang);
                        }
                        items.addAll(result.record.list);
                        mAdapter.notifyDataSetChanged();
                        content.refreshComplete();
                        page++;
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }
}
