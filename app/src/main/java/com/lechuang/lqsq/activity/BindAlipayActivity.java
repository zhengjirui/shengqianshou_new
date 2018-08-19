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
 * 时间：13:59
 * 描述：绑定支付宝
 */

public class BindAlipayActivity extends BaseNormalTitleActivity {
    @BindView(R.id.zfb_et)
    EditText zfbEt;
    @BindView(R.id.name_et)
    EditText nameEt;

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, BindAlipayActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_alipay;
    }

    @Override
    public void initView() {
        setTitleName("绑定支付宝");
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.comlete)
    public void onViewClicked() {
        complete();
    }

    private void complete() {
        final String zfb = zfbEt.getText().toString().trim();
        if (TextUtils.isEmpty(zfb)) {
            showLongToast("请正确舒服支付宝账号");
            return;
        }
        String name = nameEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showLongToast("请正确输入姓名");
            return;
        }
        showWaitDialog("正在绑定...").setCancelable(false);
        Map<String, String> map = new HashMap<>();
        map.put("alipayNumber", zfb);   //支付宝账号
        map.put("alipayRealName", name);   //姓名
        Network.getInstance().getApi(CommenApi.class)
                .updataInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpdataInfoBean>(BindAlipayActivity.this) {
                    @Override
                    protected void success(UpdataInfoBean data) {
                        if (data == null)
                            return;
                        showLongToast("绑定支付宝成功!");
                        UserInfo userInfo = UserHelper.getUserInfo(BindAlipayActivity.this);
                        userInfo.alipayNumber = zfb;
                        UserHelper.saveUserInfo(BindAlipayActivity.this, userInfo);
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
