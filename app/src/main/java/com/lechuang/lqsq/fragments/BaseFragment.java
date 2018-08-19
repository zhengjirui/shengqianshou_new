package com.lechuang.lqsq.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lechuang.lqsq.utils.DialogHelp;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/23
 * 时间：09:59
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    protected Context mContext;
    protected View mRoot;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;
    private boolean _isVisible;
    private ProgressDialog waitDialog;
    protected Subscription subscription;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
            unbinder = ButterKnife.bind(this, mRoot);
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
            onBindViewBefore(mRoot);
            unbinder = ButterKnife.bind(this, mRoot);
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            initView(mRoot);
            _isVisible = true;
            initData();
        }
        return mRoot;
    }

    @Override
    public void onDestroyView() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void onBindViewBefore(View root) {

    }

    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }

    protected void onRestartInstance(Bundle bundle) {

    }

    protected void initView(View root) {
    }

    protected void initData() {
    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible && mContext != null) {
            if (waitDialog == null) {
                waitDialog = DialogHelp.getWaitDialog(mContext, message);
                waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onWaitCancel();
                    }
                });
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

    public void onWaitCancel(){
    }
}
