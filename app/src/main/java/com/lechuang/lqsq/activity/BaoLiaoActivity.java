package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.TipoffShowBean;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.TipoffShowApi;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.lechuang.lqsq.widget.views.refeshview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：12:19
 * 描述：
 */

public class BaoLiaoActivity extends BaseNormalTitleActivity implements OnItemClick {
    @BindView(R.id.common_loading_all)
    RelativeLayout loading;
    @BindView(R.id.ll_noNet)
    LinearLayout error;
    @BindView(R.id.content)
    XRecyclerView content;
    private int page = 1;
    private CommonRecyclerAdapter mAdapter;
    private ArrayList<TipoffShowBean.ListBean> contentList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_baoliao;
    }

    @Override
    public void initView() {
        mAdapter = new CommonRecyclerAdapter(this, R.layout.tipoff_item, contentList) {
            @Override
            public void convert(ViewHolderRecycler viewHolder, Object o) {
                TipoffShowBean.ListBean bean = (TipoffShowBean.ListBean) o;
                viewHolder.displayImageScal(R.id.iv_tipoff, bean.img);
                viewHolder.setText(R.id.title_tipoff, bean.title);
                viewHolder.displayImage(R.id.iv_tipoff_photo, bean.photo);
                viewHolder.setText(R.id.user_name, bean.nickName);
                viewHolder.setText(R.id.tv_comment, bean.pageViews + "");
                viewHolder.setText(R.id.tv_dianzan, bean.praiseCount + "");
            }
        };
        mAdapter.setOnItemClick(this);
        content.setLayoutManager(new LinearLayoutManager(this));
        content.setLoadingMoreEnabled(true);
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
        content.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        subscription = Network.getInstance().getApi(TipoffShowApi.class)
                .getTipoff(map)
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TipoffShowBean>() {
                    @Override
                    protected void success(TipoffShowBean data) {
                        content.refreshComplete();
                        if (data == null || data.list == null || data.list.isEmpty()) {
                            content.noMoreLoading();
                            return;
                        }
                        if (page == 1) {
                            contentList.clear();
                        }
                        page++;
                        contentList.addAll(data.list);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loading.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loading.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.iv_tryAgain, R.id.et_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tryAgain:
                page = 1;
                getData();
                break;
            case R.id.et_product:
                SearchActivity.lanuchActivity(this, 1);
                break;
        }
    }

    @Override
    public void itemClick(View v, int position) {
        if (UserHelper.isLogin()) {
            Intent intent = new Intent(this, StoryDetailActivity.class);
            intent.putExtra("id", contentList.get(position).id);
            startActivity(intent);
        } else {
            LoginActivity.launchActivity(this);
        }
    }


}
