package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.glide.CropCircleTransformation;
import com.lechuang.lqsq.manage.FolderManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.DialogHelp;
import com.lechuang.lqsq.utils.FileUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：08:59
 * 描述：设置
 */

public class SettingActivity extends BaseNormalTitleActivity {
    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.nicheng)
    TextView nicheng;
    @BindView(R.id.zhanghao)
    TextView zhanghao;
    @BindView(R.id.tuichulay)
    View tuichulay;
    @BindView(R.id.userinfo)
    View userinfo;

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setTitleName("设置");
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserData();
    }

    private void setUserData() {
        if (!UserHelper.isLogin()) {
            userinfo.setVisibility(View.GONE);
            tuichulay.setVisibility(View.GONE);
            return;
        }
        UserInfo userInfo = UserHelper.getUserInfo(this);
        zhanghao.setText("账号:" + (userInfo.phone == null ? "---" : userInfo.phone));
        if (!TextUtils.isEmpty(userInfo.photo)) {
            Glide.with(this).load(userInfo.photo)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .error(R.mipmap.touxiang)
                    .placeholder(R.mipmap.touxiang).into(touxiang);
        }
        nicheng.setText(userInfo.nickName == null ? userInfo.phone : userInfo.nickName);
    }


    @OnClick({R.id.userinfo, R.id.xiugaimima, R.id.yijianfankui, R.id.banbenxinxi, R.id.bangzhu, R.id.clearCache, R.id.tuichu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userinfo:
                UserInfoActivity.launchActivity(this);
                break;
            case R.id.xiugaimima:
                UpdatePassActivity.launchActivity(this);
                break;
            case R.id.yijianfankui:
                YJFKActivity.launchActivity(this);
                break;
            case R.id.banbenxinxi:
                VersionInfoActivity.launchActivity(this);
                break;
            case R.id.bangzhu:
                HelpCenterActivity.launchActivity(this);
                break;
            case R.id.clearCache:
                clearCache();
                break;
            case R.id.tuichu:
                tuichu();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void tuichu() {
        showWaitDialog("正在退出...").setCancelable(false);
        subscription = Network.getInstance().getApi(CommenApi.class)
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(SettingActivity.this) {
                    @Override
                    protected void success(String data) {
                        showLongToast(data);
                        UserHelper.clearUserToken(SettingActivity.this);
                        RxBus.getDefault().post(Constant.logout, "");
//                        setUserData();
                        LoginActivity.launchActivity(SettingActivity.this);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    private void clearCache() {
        DialogHelp.getConfirmDialog(this, "确定清除本地缓存?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FileUtil.clearFileWithPath(FolderManager.getPhotoFolder().getAbsolutePath());
                FileUtil.clearFileWithPath(FolderManager.getCrashLogFolder().getAbsolutePath());
                showLongToast("清除成功");
            }
        }).show();


    }
}
