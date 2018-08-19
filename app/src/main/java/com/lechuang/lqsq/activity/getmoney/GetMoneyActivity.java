package com.lechuang.lqsq.activity.getmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.GetInfoActivity;
import com.lechuang.lqsq.activity.SearchActivity;
import com.lechuang.lqsq.adapter.CommonPagerAdapter;
import com.lechuang.lqsq.bean.GetBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.GetApi;
import com.lechuang.lqsq.utils.TDevice;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.CustomTabLayout;
import com.lechuang.lqsq.widget.views.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/05
 * 时间：13:29
 * 描述：
 */

public class GetMoneyActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @BindView(R.id.tablayout_get)
    TabLayout tablayoutGet;
    @BindView(R.id.vp_get)
    ViewPager vpGet;
    @BindView(R.id.et_product)
    ClearEditText etProduct;
    @BindView(R.id.ll_noNet)
    LinearLayout llNoNet; //没有网络
    @BindView(R.id.iv_tryAgain)
    ImageView tryAgain;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.iv_shuoming)
    ImageView ivShuoming;
    //fragments集合
    private List<Fragment> fragments;
    //viewpage标题
    private List<GetBean.TopTab> topTabList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_get;
    }

    @Override
    public void initView() {
        ivShuoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetMoneyActivity.this, GetInfoActivity.class));
            }
        });
        etProduct.setOnEditorActionListener(this);
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        if (Utils.isNetworkAvailable(this)) {
//            refreshScrollView.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.VISIBLE);
            llNoNet.setVisibility(View.GONE);
            Network.getInstance().getApi(GetApi.class)
                    .topTabList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<GetBean>(GetMoneyActivity.this) {
                        @Override
                        public void success(GetBean result) {
                            if (result == null) return;
                            GetBean.TopTab topTab = new GetBean.TopTab();
                            topTab.rootId = -1;
                            topTab.rootName = "精选";
                            topTabList.add(topTab);
                            List<GetBean.TopTab> tabList = result.tbclassTypeList;
                            if (tabList != null) {
                                topTabList.addAll(tabList);
                            }
                            CustomTabLayout.reflex(tablayoutGet);
                            initFragment();
                        }

                        @Override
                        public void error(int code, String msg) {

                        }
                    });
        } else {
            llNoNet.setVisibility(View.VISIBLE);
            llContent.setVisibility(View.GONE);
        }
    }

    /**
     * @author li
     * 邮箱：961567115@qq.com
     * @time 2017/9/22  12:22
     * @describe 中间viewPager和fragment联动
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        for (GetBean.TopTab tab : topTabList) {
            fragments.add(setTitle(new GetBaseFragment(), tab.rootId, tab.rootName));
        }
        //设置适配器
        CommonPagerAdapter mPaperAdapter = new CommonPagerAdapter(getSupportFragmentManager());
        mPaperAdapter.addFragment(fragments, topTabList);
//        vpGet.setOffscreenPageLimit(3);
        vpGet.setAdapter(mPaperAdapter);
        //设置tablout 滑动模式
        tablayoutGet.setTabMode(TabLayout.MODE_SCROLLABLE);
        //联系tabLayout和viwpager
        tablayoutGet.setupWithViewPager(vpGet);
    }

    /**
     * 设置头目
     */
    private Fragment setTitle(Fragment fragment, int rootId, String title) {
        Bundle args = new Bundle();
        args.putInt(Constant.KEY_ROOT_ID, rootId);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.iv_tryAgain, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tryAgain:
                if (Utils.isNetworkAvailable(this)) {
                    getData();
                } else {
                    showShortToast(getString(R.string.net_error));
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    TDevice.hideSoftKeyboard(getWindow().getDecorView());
                    ((GetBaseFragment) fragments.get(vpGet.getCurrentItem())).search(etProduct.getText().toString().trim());
                    etProduct.setText("");
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }


}
