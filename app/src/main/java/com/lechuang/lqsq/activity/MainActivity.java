package com.lechuang.lqsq.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.GetHostUrlBean;
import com.lechuang.lqsq.bean.OwnCheckVersionBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.fragments.FenLeiFragment;
import com.lechuang.lqsq.fragments.ShouYeFragment;
import com.lechuang.lqsq.fragments.ShoujiFragment;
import com.lechuang.lqsq.fragments.WoDeFragment;
import com.lechuang.lqsq.fragments.ZbFragment;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.manage.ToastManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.widget.dialog.SearchDialog;
import com.lechuang.lqsq.widget.dialog.VersionUpdateDialog;
import com.umeng.socialize.UMShareAPI;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/27
 * 时间：15:10
 * 描述：
 */

public class MainActivity extends BaseActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private final static int selectedColor = Color.parseColor("#ff5c19");
    private final static int normalColor = Color.parseColor("#757575");
    private Fragment[] fragments;
    private ImageView[] imageButtons;
    private TextView[] textViews;
    private int index, currentIndex;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        regiUserState();
//        fragments = new Fragment[]{new ShouYeFragment(), new FenLeiFragment(), new ShoujiFragment(),new ChaoJiSouFragment(), new WoDeFragment()};
        fragments = new Fragment[]{new ShouYeFragment(), new FenLeiFragment(), new ShoujiFragment(), new ZbFragment(), new WoDeFragment()};
        imageButtons = new ImageView[5];
        textViews = new TextView[5];
        imageButtons[0] = (ImageView) findViewById(R.id.iv_shouye);
        imageButtons[1] = (ImageView) findViewById(R.id.iv_fenlei);
        imageButtons[2] = (ImageView) findViewById(R.id.iv_yaoqing);
        imageButtons[3] = (ImageView) findViewById(R.id.iv_chaoisou);
        imageButtons[4] = (ImageView) findViewById(R.id.iv_wode);
        textViews[0] = (TextView) findViewById(R.id.tv_shouye);
        textViews[1] = (TextView) findViewById(R.id.tv_fenlei);
        textViews[2] = (TextView) findViewById(R.id.tv_yaoqing);
        textViews[3] = (TextView) findViewById(R.id.tv_chaojisou);
        textViews[4] = (TextView) findViewById(R.id.tv_wode);
        imageButtons[index].setSelected(true);
        textViews[index].setTextColor(selectedColor);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .add(R.id.fragment_container, fragments[1])
                .add(R.id.fragment_container, fragments[2])
                .add(R.id.fragment_container, fragments[3])
                .add(R.id.fragment_container, fragments[4])
                .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3]).hide(fragments[4])
                .show(fragments[0])
                .commit();

    }

    @Override
    public void initData() {
        if (UserHelper.isLogin())
            subscription = Network.getInstance().getApi(OwnApi.class)
                    .userInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<UserInfo>(MainActivity.this) {
                        @Override
                        protected void success(UserInfo data) {
                            if (data != null) {
                                UserHelper.saveUserInfo(MainActivity.this, data);
//                                RxBus.getDefault().post(Constant.login_success, "");
                                switchFragment(0);
                            }
                        }

                        @Override
                        public void error(int code, String msg) {

                        }

                        @Override
                        public void onNext(NetResult<UserInfo> result) {
                            if (result.errorCode == 200) {
                                success(result.data);
                            } else if (result.errorCode == 401 || result.errorCode == 303) {  //错误码401 303 登录
                                if (UserHelper.isLogin())
                                    RxBus.getDefault().post(Constant.logout, "");
                                UserHelper.clearUserToken(MainActivity.this);
                                switchFragment(0);
                                if (!TextUtils.isEmpty(result.moreInfo)) {
                                    ToastManager.getInstance().showLongToast(result.moreInfo);
                                }
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
                    });

        Network.getInstance().getApi(CommenApi.class)
                .getShareProductUrl()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<GetHostUrlBean>() {
                    @Override
                    public void success(GetHostUrlBean result) {
                        CacheManager.getInstance(MainActivity.this).put(Constant.getShareProductHost, result.show.appHost);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

        Network.getInstance().getApi(CommenApi.class)
                .updataVersion("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnCheckVersionBean>() {
                    @Override
                    public void success(OwnCheckVersionBean result) {
                        // ((TextView) findViewById(R.id.tv_versiondesc)).setText(result.app.versionDescribe);
                        if (!BuildConfig.VERSION_NAME.contains(result.maxApp.versionNumber)) {//版本号
                           /* UpdateVersion version = new UpdateVersion(mContext);
                            version.setDescribe(result.maxApp.versionDescribe);//版本描述
                            if (result.maxApp.downloadUrl != null)
                                version.setUrl(QUrl.url + result.maxApp.downloadUrl);//下载地址
                            version.showUpdateDialog();*/
                            VersionUpdateDialog version = new VersionUpdateDialog(MainActivity.this, result.maxApp.versionDescribe);
                            //下载地址
                            if (result.maxApp.downloadUrl != null)
                                version.setUrl(QUrl.url + result.maxApp.downloadUrl);
                            version.show();
                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserHelper.isLogin()) {
            if (index == 4) {
                switchFragment(0);
            }
        }
        if (currentIndex != 2){
            dialogClipSearch();
        }


    }

    public void dialogClipSearch() {
        ClipboardManager mClipboard = (ClipboardManager) MyApplication.getContext().getSystemService(CLIPBOARD_SERVICE);
        //无数据时直接返回
        if (!mClipboard.hasPrimaryClip()) {
            return;
        }
        //如果是文本信息
        if (mClipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = mClipboard.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            //此处是TEXT文本信息
            final String clipStr = item.coerceToText(MyApplication.getContext()).toString();
            if (clipStr != null && !clipStr.equalsIgnoreCase("")) {
                //说明剪切板中有内容，可以进行搜索
                final SearchDialog searchDialog = new SearchDialog(this, clipStr);
                searchDialog.setConfirmClickLisenter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SearchResultNewActivity.class);
                        intent.putExtra("type", 2);
                        //rootName传递过去显示在搜索框上
                        intent.putExtra("rootName", clipStr);
                        //rootId传递过去入参
                        intent.putExtra("rootId", clipStr);
                        startActivity(intent);
                        searchDialog.dismiss();
                    }
                });
                searchDialog.show();
                ClipData clipData = ClipData.newPlainText("Label", "");
                mClipboard.setPrimaryClip(clipData);
            }
        }
    }

    public void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.rv_showye:
                index = 0;
                switchFragment(index);
                break;
            case R.id.rv_fenlei:
                index = 1;
                switchFragment(index);
                break;
            case R.id.rv_yaoqing:
//                if (UserHelper.isLogin()){
//                    startActivity(new Intent(this, ShareMoneyActivity.class));
//                }else {
//                    startActivity(new Intent(this, LoginActivity.class));
//                }
                index = 2;
                switchFragment(index);
                break;
            case R.id.rv_chaojisou:
                index = 3;
                switchFragment(index);
                break;
            case R.id.rv_wode:
                index = 4;
                switchFragment(index);
                break;
        }


    }

    private void switchFragment(int index) {
        if (index == 4 && !UserHelper.isLogin()) {
            LoginActivity.launchActivity(this);
            return;
        }
        if (currentIndex != index) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                transaction.add(R.id.fragment_container, fragments[index]);
            }
            transaction.show(fragments[index]).commit();
        }
        imageButtons[currentIndex].setSelected(false);
        imageButtons[index].setSelected(true);
        textViews[currentIndex].setTextColor(normalColor);
        textViews[index].setTextColor(selectedColor);
        currentIndex = index;
        if (index == 4 && UserHelper.isLogin() && UserHelper.getUserInfo(this).firstLoginFlag == 0) {
            ((WoDeFragment) fragments[4]).showFirstLoginDialog();
        }
    }

    private Observable<String> login;
    private Observable<String> logout;

//    private void regiUserState() {
//        login = RxBus.getDefault().register(Constant.login_success, String.class);
//        login.onTerminateDetach()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        switchFragment(0);
//                    }
//                });
//        logout = RxBus.getDefault().register(Constant.logout, String.class);
//        logout.onTerminateDetach()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        switchFragment(0);
//                    }
//                });
//    }
//
//    @Override
//    protected void onDestroy() {
//        RxBus.getDefault().unregister(Constant.login_success, login);
//        RxBus.getDefault().unregister(Constant.logout, logout);
//        super.onDestroy();
//    }


}
