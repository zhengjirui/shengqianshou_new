package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.adapter.CommonAdapter;
import com.lechuang.lqsq.adapter.CommonViewHolder;
import com.lechuang.lqsq.bean.OwnNewsListBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：17:30
 * 描述：
 */

public class NewsActivity extends BaseNormalTitleActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, PullToRefreshBase.OnLastItemVisibleListener {
    private int page = 1;
    private List<OwnNewsListBean.ListBean> newsList;
    private CommonAdapter<OwnNewsListBean.ListBean> mAdapter;
    private PullToRefreshListView lv_news;

    public synchronized static void launchActivity(Context context) {
        context.startActivity(new Intent(context, NewsActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    public void initView() {
        setTitleName("消息");
        lv_news = (PullToRefreshListView) findViewById(R.id.lv_news);
        lv_news.setOnRefreshListener(this);
        lv_news.setOnItemClickListener(this);
        lv_news.setEmptyView(findViewById(R.id.emptyView));
        lv_news.setOnLastItemVisibleListener(this);
//        lv_news.setMode(PullToRefreshBase.Mode.DISABLED);
        newsList = new ArrayList<>();
        lv_news.setShowIndicator(false);
//        lv_news.getLoadingLayoutProxy().setLoadingDrawable(null);
    }

    @Override
    public void initData() {
        page = 1;
        getData();
    }

    private void getData() {
        subscription = Network.getInstance().getApi(OwnApi.class)
                .allNws(page)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnNewsListBean>(NewsActivity.this) {
                    @Override
                    protected void success(OwnNewsListBean result) {
                        lv_news.onRefreshComplete();
                        if (result == null) {
                            return;
                        }
                        if (page == 1) {
                            newsList.clear();
                        }
                        List<OwnNewsListBean.ListBean> list = result.list;
                        lv_news.setMode(list.size() > 0 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                        if (list.toString().equals("[]")) {
                            return;
                        }
                        newsList.addAll(list);
                        if (page == 1) {
                            mAdapter = new CommonAdapter<OwnNewsListBean.ListBean>(newsList, NewsActivity.this, R.layout.item_news) {
                                @Override
                                public void setData(CommonViewHolder viewHolder, Object item) {
                                    OwnNewsListBean.ListBean bean = (OwnNewsListBean.ListBean) item;
                                    viewHolder.setText(R.id.tv_time, bean.createTimeStr);
                                    viewHolder.setText(R.id.tv_content, bean.content);
                                    viewHolder.setText(R.id.tv_title, "消息通知");
                                }
                            };
                            lv_news.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void error(int code, String msg) {
                        lv_news.onRefreshComplete();
                    }
                });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 1;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page += 1;
        getData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailsActivity.launchActivity(this, "消息通知",
                newsList.get(position - 1).createTimeStr,
                newsList.get(position - 1).content);
    }

    @Override
    public void onLastItemVisible() {
        if (lv_news.getMode() == PullToRefreshBase.Mode.BOTH) {
            page += 1;
            getData();
        }
    }
}
