package com.lechuang.lqsq.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.utils.DialogHelp;
import com.lechuang.lqsq.utils.TDevice;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/20
 * 时间：10:13
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {
    private boolean _isVisible;
    private ProgressDialog waitDialog;
    protected Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.activityStack.add(this);
        onBeforeSetContentLayout();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        _isVisible = true;
        initData();
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
        if (isFinishing()) {
            TDevice.hideSoftKeyboard(getCurrentFocus());
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
        MyApplication.activityStack.remove(this);
    }

    protected void onBeforeSetContentLayout() {
    }

    protected void init(Bundle savedInstanceState) {
    }

    public void showLongToast(int id) {
        ToastManager.getInstance().showLongToast(id);
    }

    public void showLongToast(String msg) {
        ToastManager.getInstance().showLongToast(msg);
    }

    public void showShortToast(int id) {
        ToastManager.getInstance().showShortToast(id);
    }

    public void showShortToast(String msg) {
        ToastManager.getInstance().showShortToast(msg);
    }

    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (waitDialog == null) {
                waitDialog = DialogHelp.getWaitDialog(this, message);
            }
            if (waitDialog != null) {
                waitDialog.setMessage(message);
                waitDialog.show();
            }
            return waitDialog;
        }
        return null;
    }

    public void hideWaitDialog() {
        if (_isVisible && waitDialog != null) {
            try {
                if (waitDialog.isShowing())
                    waitDialog.dismiss();
                waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
