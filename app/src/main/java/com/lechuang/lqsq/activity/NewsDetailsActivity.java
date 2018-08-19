package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.lechuang.lqsq.R;

import butterknife.BindView;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：15:59
 * 描述：
 */

public class NewsDetailsActivity extends BaseNormalTitleActivity {
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public synchronized static void launchActivity(Context context, String title, String time, String content) {
        Intent intent = new Intent(context, NewsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    public void initView() {
        setTitleName("消息详情");
    }

    @Override
    public void initData() {
        tvNewsTitle.setText(getIntent().getStringExtra("title"));
        tvTime.setText(getIntent().getStringExtra("time"));
        tvContent.setText(getIntent().getStringExtra("content"));
    }
}
