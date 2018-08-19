package com.lechuang.lqsq.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.ShareAppImages;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.ShareUtils;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.loop.adapter.MyViewpagerAdapter;
import com.lechuang.lqsq.widget.loop.transformer.ScalePageTransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ShareAppActivity extends BaseActivity {

    @BindView(R.id.page_container)
    RelativeLayout mPageContainer;
    @BindView(R.id.loop_middle_view)
    ViewPager mLoopMiddleVp;

    @BindView(R.id.share_qq_kongjian)
    TextView mQQkongjian;
    @BindView(R.id.share_qq)
    TextView mQQ;
    @BindView(R.id.share_weixin)
    TextView mWeiXin;
    @BindView(R.id.share_friends)
    TextView mFriends;

    private List<String> mLoopData = new ArrayList<>();
    private Bitmap shareCode;
    private MyViewpagerAdapter mLoopAdapter;
    private ScalePageTransformer scalePageTransformer;



    @Override
    public int getLayoutId() {
        return R.layout.activity_share_app;
    }

    @Override
    public void initView() {
    }


    @Override
    public void initData() {
        //设置轮播图
        setLoopMiddleView();

        getData();
    }

    private void getData() {
        showWaitDialog("");
        Network.getInstance().getApi(CommenApi.class)
                .getShareAppImagesNew()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<ShareAppImages>() {
                    @Override
                    protected void success(ShareAppImages data) {
                        setLoopData(data.image, data.shareUrl);
                        hideWaitDialog();
                    }

                    @Override
                    public void error(int code, String msg) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        super.onError(e);
                        hideWaitDialog();
                    }
                });
    }

    private void setLoopMiddleView() {
        mPageContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mLoopMiddleVp.dispatchTouchEvent(event);
            }
        });
        mLoopAdapter = new MyViewpagerAdapter(this);
        scalePageTransformer = new ScalePageTransformer();
        mLoopMiddleVp.setPageMargin(-Utils.dp2px(this, 25));
        mLoopMiddleVp.setPageTransformer(true, scalePageTransformer);
        mLoopMiddleVp.setAdapter(mLoopAdapter);
    }

    private void setLoopData(List<String> images, String erCodeUri) {
//        if (mLoopData == null || images.size() < 0 || erCodeUri.equalsIgnoreCase("")) {
        if (mLoopData == null || images.size() < 0) {
            return;
        }
//        images.clear();
//        List<String> strings = new ArrayList<>();
//        strings.add("http://pic.qiantucdn.com/58pic/22/06/55/57b2d98e109c6_1024.jpg");
//        strings.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524121249955&di=3d9f96d4ede6f185770139a5d367ed47&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2014%2F316%2F51%2FZ369GEY67L78.jpg");
//        strings.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524121249955&di=869b50fd7c39eefdf91aea18f33eaab2&imgtype=0&src=http%3A%2F%2Fa3.topitme.com%2F8%2F9c%2Ff6%2F112910054167bf69c8o.jpg");
//        images.addAll(strings);

        //设置数据
        mLoopData.clear();
        mLoopData.addAll(images);
        //设置完数据后更新数据
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
//        shareCode = QRCodeUtils.createQRImage(erCodeUri, width / 5, width / 5);
        mLoopMiddleVp.setOffscreenPageLimit(mLoopData.size());
        mLoopAdapter.addAll(mLoopData, shareCode);

        if (mLoopAdapter.getCount() > 1) {
            scalePageTransformer.transformPage(mLoopMiddleVp.getChildAt(1), 1);
        }
    }

    @OnClick({R.id.iv_back, R.id.share_qq_kongjian, R.id.share_qq, R.id.share_weixin, R.id.share_friends, R.id.save_local})
    public void onViewClicked(View view) {

        showWaitDialog("");
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.share_qq_kongjian:
                File[] files1 = getFiles();
                if (files1 == null || files1.length <= 0)
                    return;
                ShareUtils.shareToQQZ(this, files1, "");
                break;
            case R.id.share_qq:
                File[] files2 = getFiles();
                if (files2 == null || files2.length <= 0)
                    return;
                ShareUtils.shareToQQ(this, files2, "");
                break;
            case R.id.share_weixin:
                File[] files3 = getFiles();
                if (files3 == null || files3.length <= 0)
                    return;
                ShareUtils.shareToWChart(this, files3, "");
                break;
            case R.id.share_friends:
                File[] files4 = getFiles();
                if (files4 == null || files4.length <= 0)
                    return;
                ShareUtils.shareToWChartFs(this, files4, "");
                break;
            case R.id.save_local:
                String s = saveToLocation();
                if (s != null && !s.equalsIgnoreCase("")) {
                    Utils.show(this, "保存成功！");
                }else {
                    Utils.show(this, "保存失败！");
                }
                hideWaitDialog();
                break;
        }
    }

    private File[] getFiles() {
        File[] mImgUrlFiles = new File[1];
        String file = gotoShare();
        if (file == null || file.equalsIgnoreCase("")) {
            Utils.show(this, "图片为空！");
            hideWaitDialog();
            return null;
        }
        mImgUrlFiles[0] = new File(file);
        hideWaitDialog();
        return mImgUrlFiles;
    }

    private String gotoShare() {
        Bitmap bgBitmap = mLoopAdapter.getBitMap(mLoopMiddleVp.getCurrentItem());
        if(bgBitmap == null){
            return "";
        }
        View content = LayoutInflater.from(this).inflate(R.layout.content_item_share, null);
        ImageView ivShareBg = (ImageView) content.findViewById(R.id.iv_share_bg);
        ImageView ivShareErcode = (ImageView) content.findViewById(R.id.iv_share_ercode);
        if (bgBitmap != null) {
            ivShareBg.setImageBitmap(bgBitmap);
        }
        if (shareCode != null) {
            ivShareErcode.setImageBitmap(shareCode);
        }
        Bitmap bitmap = creatBitmap(bgBitmap);
        try {
            String mLocalUrl = getExternalCacheDir() + "/" + System.currentTimeMillis() + ".png";
            File dir = new File(mLocalUrl);
            FileOutputStream fileOutputStream = new FileOutputStream(dir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            bitmap.recycle();
            return mLocalUrl;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveToLocation() {
        Bitmap bgBitmap = mLoopAdapter.getBitMap(mLoopMiddleVp.getCurrentItem());
        if (bgBitmap == null) {
            return "";
        }
        View content = LayoutInflater.from(this).inflate(R.layout.content_item_share, null);
        ImageView ivShareBg = (ImageView) content.findViewById(R.id.iv_share_bg);
        ImageView ivShareErcode = (ImageView) content.findViewById(R.id.iv_share_ercode);
        if (bgBitmap != null) {
            ivShareBg.setImageBitmap(bgBitmap);
        }
        if (shareCode != null) {
            ivShareErcode.setImageBitmap(shareCode);
        }
        Bitmap bitmap = creatBitmap(bgBitmap);
        try {
//            String mLocalUrl = getExternalCacheDir() + "/" + System.currentTimeMillis() + ".png";
            String mLocalUrl = Environment.getExternalStorageDirectory()
                    + File.separator + Environment.DIRECTORY_DCIM
                    + File.separator + "Camera" + File.separator + System.currentTimeMillis() + ".png";
            File dir = new File(mLocalUrl);
            FileOutputStream fileOutputStream = new FileOutputStream(dir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            bitmap.recycle();
            fileOutputStream.flush();
            fileOutputStream.close();

            //https://www.cnblogs.com/Sharley/p/7942142.html
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    bitmap, mLocalUrl, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(dir);
            intent.setData(uri);
            sendBroadcast(intent);

            return mLocalUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 绘制view
     */

    private Bitmap creatBitmap(Bitmap bgBitmap) {

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        //得到屏幕的宽和高
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        //缩放背景的大小
        bgBitmap = zoomImg(bgBitmap, width);

        //新建一个bitmap
        Bitmap newBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(newBitmap);// 设置canvas画布背景为白色
        canvas.drawColor(Color.BLACK);

        Rect srcbg = new Rect(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());//代表要绘制的bitmap 区域
        Rect dstbg = new Rect(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());//将bitmap 绘制在屏幕的什么地方
        canvas.drawBitmap(bgBitmap, srcbg, dstbg, null);

        //释放得到二维码绘制
//        Bitmap shareImg = zoomImg(shareCode, width / 3);//将二维码宽缩放至屏幕的1/3

//        int leftshareImg = width / 3;
//        int topshareImg = bgBitmap.getHeight() / 13 * 9;

//        Rect srcshareImg = new Rect(0, 0, shareImg.getWidth(), shareImg.getHeight());//代表要绘制的bitmap 区域
//        Rect dstshareImg = new Rect(leftshareImg, topshareImg,
//                leftshareImg + shareImg.getWidth(),
//                topshareImg + shareImg.getHeight());//将bitmap 绘制在屏幕的什么地方
//        canvas.drawBitmap(shareImg, srcshareImg, dstshareImg, null);
//        drawText(canvas, "立即扫码，早买早优惠", 0, topshareImg + shareImg.getHeight() + Utils.dp2px(this, 20),
//                width, topshareImg + shareImg.getHeight() + Utils.dp2px(this, 30));

        return newBitmap;

    }

    private void drawText(Canvas canvas, String content, int left, int top, int right, int bottom) {
        Rect targetRect = new Rect(left, top, right, bottom);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setTextSize(Utils.dp2px(this, 16));
        paint.setColor(Color.WHITE);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, targetRect.centerX(), baseline, paint);
    }

    // 等比缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}
