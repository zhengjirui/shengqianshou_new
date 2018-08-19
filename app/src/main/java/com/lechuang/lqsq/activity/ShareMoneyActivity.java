package com.lechuang.lqsq.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.system.ErrnoException;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.ShareMoneyBean;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.OwnApi;
import com.lechuang.lqsq.utils.QRCodeUtils;
import com.lechuang.lqsq.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：08:38
 * 描述：分享APP
 */

public class ShareMoneyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_share;
    private ImageView back;
    private String shareUrl;
    private String image;
    private View shareView;
    private ImageView qrCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_money;
    }

    @Override
    public void initView() {
        shareHandler = new ShareHandler(this);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        back = (ImageView) findViewById(R.id.web_back);
        //访问网络结束后，设置返回键显示。
        findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        findViewById(R.id.web_back).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_right).setOnClickListener(this);
        shareView = findViewById(R.id.shareView);
        qrCode = (ImageView) findViewById(R.id.qrCode);
        back.setOnClickListener(this);
        Network.getInstance().getApi(OwnApi.class)
                .shareMoneyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<ShareMoneyBean>(ShareMoneyActivity.this) {
                    @Override
                    public void success(ShareMoneyBean result) {
                        image = result.image;
                        shareUrl = result.shareUrl;
                        Glide.with(ShareMoneyActivity.this).load(image).into(iv_share);
                        Glide.with(ShareMoneyActivity.this)
                                .load(image)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        shareView.setBackgroundDrawable(new BitmapDrawable(resource));
                                        shareHandler.sendEmptyMessageDelayed(0x10, 500);
                                    }
                                });
                        createBitmap();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }

    @Override
    public void initData() {

    }

    private String mLocalUrl;

    private void createBitmap() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        Bitmap qrImage = QRCodeUtils.createQRImage(shareUrl, width / 6, width / 6);
        qrCode.setImageBitmap(qrImage);
//        shareHandler.send

    }

    private ShareHandler shareHandler;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.web_back:
                finish();
                break;
            case R.id.iv_right:
                shareByOneShare();
                break;
        }
    }

    private class ShareHandler extends Handler {
        WeakReference<Context> weakReference;

        public ShareHandler(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Context context = weakReference.get();
            if (context == null) return;
            Bitmap bitmap = saveViewBitmap(shareView);
            try {
                if (TextUtils.isEmpty(mLocalUrl)) {
                    mLocalUrl = getExternalCacheDir() + "/" + System.currentTimeMillis() + ".png";
                }
                File dir = new File(mLocalUrl);
                FileOutputStream fileOutputStream = new FileOutputStream(dir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                Bitmap bm = BitmapFactory.decodeFile(mLocalUrl);
                //分享
                //shareByOneShare();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 把View转化为bitmap
     *
     * @param v
     * @return
     */
    private Bitmap saveViewBitmap(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    /**
     * 改变像素点
     *
     * @param bmpSrc
     * @return
     */
    public Bitmap duplicateBitmap(Bitmap bmpSrc) {
        if (null == bmpSrc) {
            return null;
        }
        int bmpSrcWidth = bmpSrc.getWidth();
        int bmpSrcHeight = bmpSrc.getHeight();
        Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight, Bitmap.Config.ARGB_8888);
        if (null != bmpDest) {
            Canvas canvas = new Canvas(bmpDest);
            final Rect rect = new Rect(0, 0, bmpSrcWidth, bmpSrcHeight);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            canvas.drawRect(rect, paint);
            canvas.drawBitmap(bmpSrc, 0, 0, paint);
        }
        return bmpDest;
    }

//    private ShareMoneyDialog dialog;

    //分享
    private void shareByOneShare() {
        Utils.shareImgInSystem(this, mLocalUrl);
       /* if (UserHelper.isLogin() && UserHelper.getUserInfo(this).isAgencyStatus == 1) {
            Utils.shareImgInSystem(this, mLocalUrl);
            return;
        }
        dialog = new ShareMoneyDialog(this, R.layout.dialog_share_money);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_daili).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(this, ApplyAgentActivity.class));
            }
        });
        dialog.findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareImgInSystem(ShareMoneyActivity.this, mLocalUrl);
            }
        });
        dialog.show();*/
    }

}
