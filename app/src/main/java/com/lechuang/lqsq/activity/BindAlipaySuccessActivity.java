package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.UserHelper;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：14:19
 * 描述：支付宝更换绑定
 */

public class BindAlipaySuccessActivity extends BaseNormalTitleActivity {
    @BindView(R.id.info)
    TextView info;

    public synchronized static void launchActivity(Context context) {
        context.startActivity(new Intent(context, BindAlipaySuccessActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_alipay_success;
    }

    @Override
    public void initView() {
        setTitleName("支付宝更换绑定");
    }

    @Override
    public void initData() {
        UserInfo userInfo = UserHelper.getUserInfo(this);
        info.setText("已绑定支付宝账号：" + userInfo.alipayNumber);
    }

    @OnClick(R.id.comlete)
    public void onViewClicked() {
        complete();
    }

    private void complete() {
        BindAlipayActivity.launchActivity(this);
        finish();
    }
}
