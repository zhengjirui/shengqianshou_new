package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：10:20
 * 描述：意见反馈
 */

public class YJFKActivity extends BaseNormalTitleActivity {
    @BindView(R.id.content)
    EditText content;

    public synchronized static void launchActivity(Context context) {
        context.startActivity(new Intent(context, YJFKActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_yjfk;
    }

    @Override
    public void initView() {
        setTitleName("意见反馈");
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.comlete)
    public void onViewClicked() {
        complete();
    }

    private void complete() {
        String contentValue = content.getText().toString();
        if (TextUtils.isEmpty(contentValue)) {
            showShortToast("请输入要反馈的内容");
            return;
        }
        showWaitDialog("正在提交意见...").setCancelable(false);
        HashMap<String, String> map = new HashMap();
        map.put("opinion", contentValue);
        Network.getInstance().getApi(CommenApi.class)
                .opinion(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(YJFKActivity.this) {
                    @Override
                    protected void success(String data) {
                        showLongToast(data);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == Constant.need_relogin) {
                            RxBus.getDefault().post(Constant.userinfo_close, "");
                            finish();
                        }
                    }
                });


    }
}
