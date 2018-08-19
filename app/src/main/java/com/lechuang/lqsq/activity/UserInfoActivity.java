package com.lechuang.lqsq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.UpFileBean;
import com.lechuang.lqsq.bean.UpdataInfoBean;
import com.lechuang.lqsq.bean.UserInfo;
import com.lechuang.lqsq.glide.CropCircleTransformation;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.TheSunApi;
import com.lechuang.lqsq.rxbus.RxBus;
import com.lechuang.lqsq.utils.ImageUtils;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.dialog.ListDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：11:08
 * 描述：个人信息
 */

public class UserInfoActivity extends BaseNormalTitleActivity {
    @BindView(R.id.tx)
    ImageView tx;
    @BindView(R.id.yhm)
    TextView yhm;
    @BindView(R.id.dh)
    TextView dh;
    @BindView(R.id.tb)
    TextView tb;
    @BindView(R.id.zfb)
    TextView zfb;
    @BindView(R.id.tv_userweixin)
    TextView tvUserWeiXin;
    @BindView(R.id.tv_userQQ)
    TextView tvUserQQ;
    private Uri uri;
    private String avatarPath;
    private Observable<String> close;

    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        setTitleName("个人信息");
    }

    @Override
    public void initData() {
        close = RxBus.getDefault().register(Constant.userinfo_close, String.class);
        close.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserData();
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(Constant.userinfo_close, close);
        super.onDestroy();
    }

    private void setUserData() {
        if (!UserHelper.isLogin()) return;
        UserInfo userInfo = UserHelper.getUserInfo(this);
        dh.setText(userInfo.phone == null ? "---" : userInfo.phone);
        if (!TextUtils.isEmpty(userInfo.photo)) {
            Glide.with(this).load(userInfo.photo)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .error(R.mipmap.touxiang)
                    .placeholder(R.mipmap.touxiang).into(tx);

        }
        yhm.setText(userInfo.nickName == null ? userInfo.phone : userInfo.nickName);
        tb.setText(TextUtils.isEmpty(userInfo.taobaoNumber) ? "绑定淘宝账号" : userInfo.taobaoNumber);
        zfb.setText(TextUtils.isEmpty(userInfo.alipayNumber) ? "绑定支付宝" : userInfo.alipayNumber);
        tvUserQQ.setText(TextUtils.isEmpty(userInfo.qqName) ? "未授权" : userInfo.qqName);
        tvUserWeiXin.setText(TextUtils.isEmpty(userInfo.weixinName) ? "未授权" : userInfo.weixinName);
    }

    @OnClick({R.id.txrl, R.id.yhmrl, R.id.dhrl, R.id.tbrl, R.id.zfbrl, R.id.line_QQ, R.id.line_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txrl:
                selectAvatar();
                break;
            case R.id.yhmrl:
                UpdateNichengActivity.launchActivity(this);
                break;
            case R.id.dhrl:
                CheckUserActivity.launchActivity(this, 1);
                break;
            case R.id.tbrl:
                /*final AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(this, new AlibcLoginCallback() {
                    @Override
                    public void onSuccess() {
                        Session taobao = alibcLogin.getSession();
                        updateInfoTaobao(taobao.nick);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showLongToast(s);

                    }
                });*/
                break;
            case R.id.zfbrl:
                CheckUserActivity.launchActivity(this, 2);
            case R.id.line_weixin://授权微信
                if (TextUtils.isEmpty(UserHelper.getUserInfo(this).weixinName)) {
                    WeiXin();
                } else {
                    Utils.show(this, "微信账号已绑定！");
                }

                break;

            case R.id.line_QQ://授权QQ
                if (TextUtils.isEmpty(UserHelper.getUserInfo(this).qqName)) {
                    qqLogin();
                } else {
                    Utils.show(this, "QQ账号已绑定！");
                }
                break;
        }
    }

    private void selectAvatar() {
        ListDialog dialog = new ListDialog(this, new String[]{"拍照", "选择上传"});
        dialog.setItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    uri = ImageUtils.startTakePhoto(UserInfoActivity.this);
                } else {
                    ImageUtils.startImagePick(UserInfoActivity.this);
                }
            }
        });
        dialog.show();
    }

    private void uploadPhoto() {
        showWaitDialog("正在上传头像...").setCancelable(false);
        List<MultipartBody.Part> parts = new ArrayList<>();
        File file = new File(avatarPath);
        RequestBody requestFile = RequestBody
                .create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody
                .Part.createFormData("file", file.getName(), requestFile);
        parts.add(part);
        Network.getInstance().getApi(TheSunApi.class)
                .fileUpload(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpFileBean>(UserInfoActivity.this) {
                    @Override
                    protected void success(UpFileBean data) {
                        String imageId = data.imageId;
                        updateInfoPhoto(imageId);
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == Constant.need_relogin) {
                            finish();
                        }
                    }
                });
    }

    private void updateInfoPhoto(final String s) {
        Map<String, String> map = new HashMap<>();
        map.put("photo", s);
        Network.getInstance().getApi(CommenApi.class)
                .updataInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpdataInfoBean>(UserInfoActivity.this) {
                    @Override
                    protected void success(UpdataInfoBean data) {
                        showLongToast("修改成功!");
                        UserInfo userInfo = UserHelper.getUserInfo(UserInfoActivity.this);
                        userInfo.photo = data.photo;
                        UserHelper.saveUserInfo(UserInfoActivity.this, userInfo);
                        setUserData();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == Constant.need_relogin) {
                            finish();
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                    if (uri == null || TextUtils.isEmpty(uri.toString())) {
                        showShortToast("保存文件失败，请重新拍照");
                        return;
                    }
                    uri = ImageUtils.startActionCrop(this, uri, 200, 200);
                    break;
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                    if (data.getData() != null)
                        uri = ImageUtils.startActionCrop(this, data.getData(), 200, 200);
                    break;
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                    if (uri == null || TextUtils.isEmpty(uri.toString())) {
                        showShortToast("保存文件失败，请重新拍照");
                        return;
                    }
                    avatarPath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
                    if (TextUtils.isEmpty(avatarPath)) {
                        avatarPath = ImageUtils.getImagePath(uri, this);
                    }
                    if (!TextUtils.isEmpty(avatarPath)) {
                        Glide.with(this)
                                .load(avatarPath)
                                .bitmapTransform(new CropCircleTransformation(this))
                                .placeholder(R.mipmap.touxiang)
                                .into(tx);
                    } else {
                        showLongToast("未找到文件请重试");
                    }

                    uploadPhoto();
                    break;
            }
        }
    }

    private void updateInfoTaobao(final String nick) {
        Map<String, String> map = new HashMap<>();
        map.put("taobaoNumber", nick);
        Network.getInstance().getApi(CommenApi.class)
                .updataInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UpdataInfoBean>(UserInfoActivity.this) {
                    @Override
                    public void success(UpdataInfoBean result) {
                        UserInfo userInfo = UserHelper.getUserInfo(UserInfoActivity.this);
                        userInfo.taobaoNumber = nick;
                        tb.setText(nick);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });

    }

    private String photo ="";
    private String nickName = "";
    private String openid = "";
    private int boundChannel;
    /**
     * 微信QQ的登录监听
     */
    private UMAuthListener mUMAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("----", data.toString());
            Toast.makeText(MyApplication.getContext(), "成功了", Toast.LENGTH_LONG).show();

            photo = data.get("iconurl");
            nickName = data.get("name");
            openid = data.get("openid");
            vertifyIsBound(openid);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(MyApplication.getContext(), "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MyApplication.getContext(), "取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * qq登录
     */
    private void qqLogin() {
        boundChannel = 2;
        UMShareAPI.get(MyApplication.getContext()).getPlatformInfo(UserInfoActivity.this, SHARE_MEDIA.QQ, mUMAuthListener);
    }

    /**
     * 微信登录
     */
    private void WeiXin() {
        boundChannel = 1;
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(MyApplication.getContext()).setShareConfig(config);
        UMShareAPI.get(MyApplication.getContext()).getPlatformInfo(UserInfoActivity.this, SHARE_MEDIA.WEIXIN, mUMAuthListener);
    }

    private void vertifyIsBound(String openId) {
        Network.getInstance().getApi(CommenApi.class)
                .threeLoginQQANDWX(openId, boundChannel + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>() {
                    @Override
                    protected void success(UserInfo data) {
                        //用户信息
                        //登录状态设为true
                        if (data == null)
                            return;
                        UserHelper.saveUserInfo(UserInfoActivity.this, data);
                        setUserData();
                    }

                    @Override
                    public void error(int code, String msg) {
                        if (code == 300) {    //绑定手机号
                            threeBind();//直接绑定手机号
                        }
                    }
                });

    }

    //绑定账号
    private void threeBind(){
        Network.getInstance().getApi(CommenApi.class)
                .threeBind(UserHelper.getUserInfo(this).phone,openid,nickName,photo,boundChannel + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<UserInfo>(this) {

                    @Override
                    protected void success(UserInfo data) {
                        //用户信息
                        //登录状态设为true
                        if (data == null)
                            return;
                        UserHelper.saveUserInfo(UserInfoActivity.this, data);
                        setUserData();
//                        showLongToast("登陆成功");
//                        RxBus.getDefault().post(Constant.login_success, "");
//                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {
                    }
                });
    }

}
