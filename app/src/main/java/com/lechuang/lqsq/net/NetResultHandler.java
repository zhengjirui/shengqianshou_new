package com.lechuang.lqsq.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.LoginActivity;
import com.lechuang.lqsq.fragments.BaseFragment;
import com.lechuang.lqsq.manage.ToastManager;

import java.util.List;

import okhttp3.Request;
import rx.Observer;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/20
 * 时间：10:10
 */

public abstract class NetResultHandler<T> implements Observer<NetResult<T>> {
    private BaseActivity activity;
    private BaseFragment fragment;

    public NetResultHandler(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    public NetResultHandler(@NonNull BaseFragment fragment) {
        this.fragment = fragment;
    }
    public NetResultHandler(){}
    @Override
    public void onCompleted() {
        if (activity != null)
            activity.hideWaitDialog();
        if (fragment != null)
            fragment.hideWaitDialog();
    }

    @Override
    public void onError(Throwable e) {
        if (activity != null)
            activity.hideWaitDialog();
        if (fragment != null)
            fragment.hideWaitDialog();
        ToastManager.getInstance().showLongToast("网络不给力，请稍后重试");
    }

    @Override
    public void onNext(NetResult<T> result) {
        if (result.errorCode == 200) {
            success(result.data);
        } else if (result.errorCode == 401 || result.errorCode == 303) {  //错误码401 303 登录
            error(Constant.need_relogin, result.moreInfo);
            if (!TextUtils.isEmpty(result.moreInfo)) {
                ToastManager.getInstance().showLongToast(result.moreInfo);
            }
            reLogin();
        } else if (result.errorCode == 300) {
            error(result.errorCode, result.moreInfo);
            if (!TextUtils.isEmpty(result.moreInfo)) {
                ToastManager.getInstance().showLongToast(result.moreInfo);
            }
        } else {
            if (!TextUtils.isEmpty(result.moreInfo)) {
                ToastManager.getInstance().showLongToast(result.moreInfo);
            }
        }

    }

    protected abstract void success(T data);

    public abstract void error(int code, String msg);

    private synchronized void reLogin() {
        LoginActivity.launchActivity(activity == null ? fragment.getActivity() : activity);
    }
}
