package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UpdataInfoBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：13:34
 * 描述：修改昵称
 */

public class UpdateNichengActivity extends BaseNormalTitleActivity {
    @BindView(R.id.nc_et)
    EditText ncEt;

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, UpdateNichengActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_nicheng;
    }

    @Override
    public void initView() {
        setTitleName("修改昵称");
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.comlete)
    public void onViewClicked() {
        complete();
    }

    private void complete() {
        final String nc = ncEt.getText().toString().trim();
        if (TextUtils.isEmpty(nc) || nc.length() < 4 || nc.length() > 20) {
            showLongToast("请注意昵称规则");
            return;
        }
        showWaitDialog("正在修改...").setCancelable(false);
        Map<String, String> map = new HashMap<>();
        map.put("nickName", nc);
        subscription = Network.getInstance().getApi(CommenApi.class)
                .updataInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpdataInfoBean>(UpdateNichengActivity.this) {
                    @Override
                    protected void success(UpdataInfoBean data) {
                        showLongToast("修改成功!");
                        UserInfo userInfo = UserHelper.getUserInfo(UpdateNichengActivity.this);
                        userInfo.nickName = nc;
                        UserHelper.saveUserInfo(UpdateNichengActivity.this, userInfo);
                        RxBus.getDefault().post(Constant.login_success, "");
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
