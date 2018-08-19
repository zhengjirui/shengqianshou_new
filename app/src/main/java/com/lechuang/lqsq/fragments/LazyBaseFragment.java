package com.lechuang.lqsq.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/09
 * 时间：14:08
 * 描述：
 */

public abstract class LazyBaseFragment extends BaseFragment {
    private boolean hasCreateView;
    private boolean isFragmentVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mRoot == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        initVariable();
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    protected void onFragmentVisibleChange(boolean isVisible) {

    }
}
