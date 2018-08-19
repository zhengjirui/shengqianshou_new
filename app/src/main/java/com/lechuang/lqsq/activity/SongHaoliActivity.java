package com.lechuang.lqsq.activity;

import android.content.Intent;
import android.view.View;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.fragments.WoDeFragment;
import com.lechuang.lqsq.manage.UserHelper;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：12:26
 * 描述：
 */

public class SongHaoliActivity extends BaseNormalTitleActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_songhaoli;
    }

    @Override
    public void initView() {
        setTitleName("新用户送好礼");
    }

    @Override
    public void initData() {

    }

    public void lingqu(View v) {
        if (UserHelper.isLogin() && UserHelper.getUserInfo(this).firstLoginFlag == 0) {
            startActivity(new Intent(this, ProfitActivity.class));
            finish();
        } else {
            showLongToast("您已经领取过该奖励");
        }

    }
}
