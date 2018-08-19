package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.OwnCheckVersionBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.QUrl;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.widget.dialog.VersionUpdateDialog;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：10:35
 * 描述：版本信息
 */

public class VersionInfoActivity extends BaseNormalTitleActivity {
    @BindView(R.id.versionInfo)
    TextView versionInfo;
    @BindView(R.id.currentVersion)
    TextView currentVersion;
    @BindView(R.id.currentVersionAlert)
    TextView currentVersionAlert;
    @BindView(R.id.versionDesc)
    TextView versionDesc;

    public synchronized static void launchActivity(Context context) {
        context.startActivity(new Intent(context, VersionInfoActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_info;
    }

    @Override
    public void initView() {
        setTitleName("版本信息");
        versionInfo.setText("版本号 " + BuildConfig.VERSION_NAME);
        currentVersion.setText("当前版本：" + BuildConfig.VERSION_NAME);
    }

    @Override
    public void initData() {
        Network.getInstance().getApi(CommenApi.class)
                .updataVersion("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<OwnCheckVersionBean>() {
                    @Override
                    public void success(OwnCheckVersionBean result) {
                        versionDesc.setText(result.maxApp.versionDescribe);
                        if (!BuildConfig.VERSION_NAME.contains(result.maxApp.versionNumber)) {//版本号
                            /*UpdateVersion version = new UpdateVersion(mContext);
                            version.setDescribe(result.maxApp.versionDescribe);//版本描述
                            if (result.maxApp.downloadUrl != null)
                                version.setUrl(QUrl.url + result.maxApp.downloadUrl);//下载地址
                            version.showUpdateDialog();*/
                            VersionUpdateDialog version = new VersionUpdateDialog(VersionInfoActivity.this, result.maxApp.versionDescribe);
                            //下载地址
                            if (result.maxApp.downloadUrl != null)
                                version.setUrl(QUrl.url + result.maxApp.downloadUrl);
                            version.show();
                        }
                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

}
