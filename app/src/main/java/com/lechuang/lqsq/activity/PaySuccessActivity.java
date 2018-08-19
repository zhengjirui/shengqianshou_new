package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.os.Bundle;

import com.lechuang.lqsq.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/08
 * 时间：10:45
 * 描述：
 */

public class PaySuccessActivity extends BaseNormalTitleActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initView() {
        setTitleName("支付成功");
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.btn_invite)
    public void onViewClicked() {
        startActivity(new Intent(this, ShareMoneyActivity.class));
        finish();
    }
}
