package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.SunDetailListBean;
import com.lechuang.lqsq.bean.TipoffListBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.TheSunApi;
import com.lechuang.lqsq.net.api.TipoffShowApi;
import com.lechuang.lqsq.widget.views.MListView;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 评论详情
 */
public class TipOffCommentDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private PullToRefreshScrollView detail_refresh;
    private MListView lv_detailsComment;
    private int page = 1;
    private String id;
    private int whichComment;
    Context mContext = this;
    private TextView tvNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_off_comment_details);
        initVeiw();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
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
    private void initVeiw() {
        ((TextView) findViewById(R.id.titleName)).setText("全部评论");
        lv_detailsComment = (MListView) findViewById(R.id.lv_detailsComment);
        detail_refresh = (PullToRefreshScrollView) findViewById(R.id.detail_refresh);
        tvNum = (TextView) findViewById(R.id.tv_num);
        findViewById(R.id.tv_addComment).setOnClickListener(this);
        detail_refresh.setOnRefreshListener(refresh);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void getData() {
        id = getIntent().getStringExtra("tipOffId");
        whichComment = getIntent().getIntExtra("whichComment", 0);
        if (whichComment == 1) { //故事详情评论
            getCommentData();
        } else if (whichComment == 2) { //晒单评论
            getSunCommentData();
        }

    }

    /**
     * 评论
     */
    private List<TipoffListBean.AppraiseListBean> list = new ArrayList<>();
    private CommonAdapter<TipoffListBean.AppraiseListBean> commonAdapter;

    public void getCommentData() {
        Network.getInstance().getApi(TipoffShowApi.class)
                .getTipoffList(id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TipoffListBean>() {
                    @Override
                    public void success(TipoffListBean result) {
                        List<TipoffListBean.AppraiseListBean> appraiseList = result.appraiseList;
                        detail_refresh.setMode(appraiseList.size() > 0 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                        if (page != 1 && appraiseList.toString().equals("[]")) {            //数据没有了
                            detail_refresh.onRefreshComplete();
                            return;
                        }
                        if (page == 1 && list != null) {
                            list.clear();
                        }
                        list.addAll(appraiseList);
                        if (page == 1) {
                            tvNum.setText(result.counts + "条评论");
                            commonAdapter = new CommonAdapter<TipoffListBean.AppraiseListBean>(list, mContext, R.layout.tipoff_comment_item) {
                                @Override
                                public void setData(CommonViewHolder viewHolder, Object item) {
                                    {
                                        final TipoffListBean.AppraiseListBean tipAppraise = (TipoffListBean.AppraiseListBean) item;
                                        if (tipAppraise.photo != null && !tipAppraise.photo.equals("")) {
                                            viewHolder.displayImage(R.id.iv_img, tipAppraise.photo);
                                        }
                                        viewHolder.setText(R.id.tv_nickname, tipAppraise.nickName);
                                        viewHolder.setText(R.id.tv_time, tipAppraise.createTimeStr);
                                        viewHolder.setText(R.id.tv_details, tipAppraise.content);
                                    }
                                }
                            };
                            lv_detailsComment.setAdapter(commonAdapter);
                        } else {
                            commonAdapter.notifyDataSetChanged();
                        }

                        detail_refresh.onRefreshComplete();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    /**
     * 晒单
     */
    private List<SunDetailListBean.AppraiseListBean> list1 = new ArrayList<>();
    private CommonAdapter<SunDetailListBean.AppraiseListBean> mAdapter;

    public void getSunCommentData() {
        Network.getInstance().getApi(TheSunApi.class)
                .sunCommentList(id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<SunDetailListBean>() {
                    @Override
                    public void success(SunDetailListBean result) {
                        List<SunDetailListBean.AppraiseListBean> appraiseList = result.appraiseList;
                        detail_refresh.setMode(appraiseList.size() > 0 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                        if (appraiseList.toString().equals("[]")) {            //数据没有了
                            detail_refresh.onRefreshComplete();
                            return;
                        }
                        if (page == 1 && list1 != null) {
                            list1.clear();
                        }
                        list1.addAll(appraiseList);
                        if (page == 1) {
                            tvNum.setText(result.counts + "条评论");
                            mAdapter = new CommonAdapter<SunDetailListBean.AppraiseListBean>(list1, mContext, R.layout.tipoff_comment_item) {

                                @Override
                                public void setData(CommonViewHolder viewHolder, Object item) {
                                    {
                                        final SunDetailListBean.AppraiseListBean tipAppraise = (SunDetailListBean.AppraiseListBean) item;
                                        if (tipAppraise.photo != null) {
                                            viewHolder.displayImage(R.id.iv_img, tipAppraise.photo);
                                        }
                                        viewHolder.setText(R.id.tv_nickname, tipAppraise.nickName);
                                        viewHolder.setText(R.id.tv_time, tipAppraise.createTimeStr);
                                        viewHolder.setText(R.id.tv_details, tipAppraise.content);

                                    }
                                }
                            };
                            lv_detailsComment.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                        detail_refresh.onRefreshComplete();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addComment:
                //传递的type判断是晒单还是爆料添加评论
                startActivity(new Intent(this, AddCommentActivity.class)
                        .putExtra("tipId", id).putExtra("type", whichComment));
                break;
            case R.id.back:
                finish();
                break;
        }

    }


    private PullToRefreshBase.OnRefreshListener2 refresh = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            String label = DateUtils.formatDateTime(
                    getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            // 显示最后更新的时间
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            page = 1;
            // 模拟加载任务
            getData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            // 显示最后更新的时间
            String label = DateUtils.formatDateTime(
                    getApplicationContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
            page += 1;
            // 模拟加载任务
            getData();
        }
    };

}
