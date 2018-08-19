package com.lechuang.lqsq.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.AllProductBean;
import com.lechuang.lqsq.bean.GetHostUrlBean;
import com.lechuang.lqsq.bean.ShareImageBean;
import com.lechuang.lqsq.bean.TaobaoUrlBean;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.QRCodeUtils;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.dialog.ShareDialog;
import com.lechuang.lqsq.widget.views.SpannelTextViewShareProduct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Cache;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：16:38
 * 描述：
 */

public class ProductShareActivity extends BaseActivity {

    private TextView clipText;
    private TextView mSelectTextView;
    private RecyclerView mImageRecyclerView;
    private int mSelectCount = 1;//默认选中第一个
    private List<ShareImageBean> shareImageBeanList = new ArrayList<>();
    private AllProductBean.ListInfo listInfo;
    private ArrayList<String> smallImages;
    //用户选中分享出去的图片
    private HashMap<String, String> shareImgsMap;
//    private String imgUrl;

    private String productUrl;
    private String tkl;
    private String shareText;

    private RelativeLayout commonLoading;
    //带二维码图片的文件名
//    private String mLocalUrl;
    private View content;
    //存储Map键的集合
    private ArrayList<String> list;
    private ShareHandler shareHandler;
    private long picName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_get_share;
    }

    @Override
    public void initView() {
        shareHandler = new ShareHandler(this);
        listInfo = (AllProductBean.ListInfo) getIntent().getSerializableExtra("listInfo");
        //分享文案
        clipText = (TextView) findViewById(R.id.clipText);
        clipText.setText(listInfo.shareText);
        TextView shareIntegral = (TextView) findViewById(R.id.shareIntegral);
        shareIntegral.setText(listInfo.shareIntegral);
        findViewById(R.id.back).setVisibility(View.GONE);
        ImageView mTitleBack = (ImageView) findViewById(R.id.back);
        commonLoading = (RelativeLayout) findViewById(R.id.common_loading_all);
        mTitleBack.setVisibility(View.VISIBLE);
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.titleName)).setText("创建分享");
        mImageRecyclerView = (RecyclerView) findViewById(R.id.image_recyclerView);
        mSelectTextView = (TextView) findViewById(R.id.tv_select_image);
        mSelectTextView.setText(String.format(Locale.CHINESE, "已选%d张", mSelectCount));
        smallImages = (ArrayList<String>) listInfo.smallImages;
        shareImgsMap = new HashMap<>();
        //默认第一张
        shareImgsMap.put("0", smallImages.get(0));
        ShareImageBean shareImageBean;
        if (smallImages != null && smallImages.size() > 0) {
            for (int x = 0; x < smallImages.size(); x++) {
                shareImageBean = new ShareImageBean();
                shareImageBean.setUrl(smallImages.get(x));
                shareImageBean.setmSelected(x == 0);
                shareImageBeanList.add(shareImageBean);
            }
            //取集合第一张图
//            imgUrl = smallImages.get(0);
            listener.load(shareImageBeanList);
        }
    }

    @Override
    public void initData() {

    }

    /**
     * 把View绘制到Bitmap上
     *
     * @param comBitmap 需要绘制的View
     * @param width     该View的宽度
     * @param height    该View的高度
     * @return 返回Bitmap对象
     * add by csj 13-11-6
     */
    public Bitmap getViewBitmap(View comBitmap, int width, int height) {
        Bitmap bitmap = null;
        if (comBitmap != null) {
            comBitmap.clearFocus();
            comBitmap.setPressed(false);

            boolean willNotCache = comBitmap.willNotCacheDrawing();
            comBitmap.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = comBitmap.getDrawingCacheBackgroundColor();
            comBitmap.setDrawingCacheBackgroundColor(0);
            float alpha = comBitmap.getAlpha();
            comBitmap.setAlpha(1.0f);

            if (color != 0) {
                comBitmap.destroyDrawingCache();
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            comBitmap.measure(widthSpec, heightSpec);
            comBitmap.layout(0, 0, width, height);

            comBitmap.buildDrawingCache();
            Bitmap cacheBitmap = comBitmap.getDrawingCache();
            if (cacheBitmap == null) {
                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",
                        new RuntimeException());
                return null;
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            comBitmap.setAlpha(alpha);
            comBitmap.destroyDrawingCache();
            comBitmap.setWillNotCacheDrawing(willNotCache);
            comBitmap.setDrawingCacheBackgroundColor(color);
        }

        Bitmap bmp = duplicateBitmap(bitmap);
        return bmp;
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

    private File[] files;

    /**
     * 改为系统分享
     */
    private void shareByOneShare() {
        files = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            File f = new File(getExternalCacheDir() + "/" + picName + list.get(i) + ".png");
            files[i] = f;
        }
        //进度对话框 图片完成之后消失
        commonLoading.setVisibility(View.GONE);
        Utils.shareManyImgInSystem(ProductShareActivity.this, files);
//        new ShareModel().gotoShareImage(this, mLocalUrl);
    }

    private ShareDialog shareDialog;

    //剪切板
    public void clip(View view) {
        Network.getInstance().getApi(CommenApi.class)
                .tb_privilegeUrl(listInfo.alipayItemId + "", listInfo.alipayCouponId, listInfo.imgs, listInfo.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TaobaoUrlBean>(ProductShareActivity.this) {
                    @Override
                    public void success(TaobaoUrlBean result) {
                        productUrl = result.productWithBLOBs.tbPrivilegeUrl;
                        tkl = result.productWithBLOBs.tbTpwd;
                        //剪切板
                        if (listInfo.shareText.contains("{选择分享渠道后生成淘口令}")) {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("share", listInfo.shareText.replace("{选择分享渠道后生成淘口令}", tkl));
                            clipboardManager.setPrimaryClip(clipData);
                            shareDialog = new ShareDialog(ProductShareActivity.this, listInfo.shareText.replace("{选择分享渠道后生成淘口令}", tkl));
                            shareDialog.show();
                        } else {
                            Utils.show(ProductShareActivity.this, getString(R.string.share_error1));
                        }

                    }

                    @Override
                    public void error(int code, String msg) {

                    }

                });

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
            ImageView codeView = (ImageView) content.findViewById(R.id.code);
            codeView.setImageBitmap((Bitmap) msg.obj);
            final LinearLayout ll_bitmap = (LinearLayout) content.findViewById(R.id.ll_bitmap);

            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            ll_bitmap.measure(w, h);
            int width = ll_bitmap.getMeasuredWidth();
            int height = width + ll_bitmap.getMeasuredHeight();
            //Bitmap bitmap = saveViewBitmap(content);
            /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();*/
            final Bitmap bitmap = getViewBitmap(content, width, height);

            try {
//                if (TextUtils.isEmpty(mLocalUrl)) {
//                    mLocalUrl = getExternalCacheDir() + "/" + picName + list.get(0) + ".png";
//                }
                File dir = new File(getExternalCacheDir() + "/" + picName + list.get(0) + ".png");
                FileOutputStream fileOutputStream = new FileOutputStream(dir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                shareByOneShare();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private interface OnShareImageLoadListener {
        void load(List<ShareImageBean> beanList);
    }

    private interface OnShareImageSelectCallback {
        void select(boolean flag);
    }

    private OnShareImageLoadListener listener = new OnShareImageLoadListener() {
        @Override
        public void load(List<ShareImageBean> beanList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mImageRecyclerView.setLayoutManager(new LinearLayoutManager(ProductShareActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    mImageRecyclerView.addItemDecoration(new DividerItemDecoration(ProductShareActivity.this, DividerItemDecoration.HORIZONTAL));
                    final ImageListAdapter imageListAdapter = new ImageListAdapter();
                    imageListAdapter.addData(shareImageBeanList);
                    mImageRecyclerView.setAdapter(imageListAdapter);
                    imageListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            ShareImageBean bean = imageListAdapter.getData().get(position);
                            if (bean.ismSelected() && mSelectCount == 1) {
                                Utils.show(ProductShareActivity.this, "请至少选择一张图片");
                                return;
                            }
                            callback.select(!bean.ismSelected());
                            bean.setmSelected(!bean.ismSelected());
                            if (bean.ismSelected()) {
                                //被选中的图片
                                shareImgsMap.put(position + "", smallImages.get(position));

                            } else {
                                try {
                                    if (shareImgsMap.get(position + "") != null) {
                                        shareImgsMap.remove(position + "");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            imageListAdapter.notifyItemChanged(position);
                        }
                    });
                }
            });
        }
    };
    private OnShareImageSelectCallback callback = new OnShareImageSelectCallback() {
        @Override
        public void select(boolean flag) {
            if (flag) {
                mSelectCount++;
            } else {
                mSelectCount--;
            }
            mSelectTextView.setText(String.format(Locale.CHINESE, "已选%d张", mSelectCount));
        }
    };

    private class ImageListAdapter extends BaseQuickAdapter<ShareImageBean, BaseViewHolder> {

        ImageListAdapter() {
            super(R.layout.share_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShareImageBean item) {
            String substring = item.getUrl().substring(item.getUrl().indexOf("//") + 2);
            Glide.with(ProductShareActivity.this).load("http://" + substring).into((ImageView) helper.getView(R.id.share_item_iamgeview));
            helper.addOnClickListener(R.id.share_item_select);
            ImageButton mSelectButton = helper.getView(R.id.share_item_select);
            mSelectButton.setSelected(item.ismSelected());
        }
    }

    //转链接
    private void transformUrl(final String appHostUrl) {
        Network.getInstance().getApi(CommenApi.class)
                .tb_privilegeUrl(listInfo.alipayItemId + "", listInfo.alipayCouponId, listInfo.imgs, listInfo.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<TaobaoUrlBean>(ProductShareActivity.this) {
                    @Override
                    public void success(TaobaoUrlBean result) {
                        productUrl = result.productWithBLOBs.tbPrivilegeUrl;
                        tkl = result.productWithBLOBs.tbTpwd;
                        //clipText.setText(listInfo.shareText.replace("{选择分享渠道后生成淘口令}", tkl));
                        createBitmap(appHostUrl);
                    }

                    @Override
                    public void error(int code, String msg) {

                    }


                });
    }

    /**
     * 分享
     *
     * @param view
     */
    public void share(View view) {
        commonLoading.setVisibility(View.VISIBLE);

        //商品分享的域名
        String hostUrl = (String) CacheManager.getInstance(this).get(Constant.getShareProductHost, "");
        if (hostUrl != null && !hostUrl.equals("")) {
            //转链接
            transformUrl(hostUrl);
        } else {
            Network.getInstance().getApi(CommenApi.class)
                    .getShareProductUrl()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultHandler<GetHostUrlBean>(ProductShareActivity.this) {
                        @Override
                        public void success(GetHostUrlBean result) {
                            CacheManager.getInstance(ProductShareActivity.this).put(Constant.getShareProductHost, result.show.appHost);
                            //转链接
                            transformUrl(result.show.appHost);
                        }

                        @Override
                        public void error(int code, String msg) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            commonLoading.setVisibility(View.GONE);
                        }
                    });
        }


//        shareByOneShare();
    }

    private void createBitmap(final String hostUrl) {
        picName = System.currentTimeMillis();
        list = new ArrayList<>();
        for (String s : shareImgsMap.keySet()) {
            list.add(s);
        }
        content = getLayoutInflater().inflate(R.layout.bitmap_share, null);
        final ImageView imageView = (ImageView) content.findViewById(R.id.share_image);
        TextView preferentialPrice = (TextView) content.findViewById(R.id.preferentialPrice);
        TextView price = (TextView) content.findViewById(R.id.price);
        final SpannelTextViewShareProduct spannelTextView = (SpannelTextViewShareProduct) content.findViewById(R.id.spannelTextView);
        spannelTextView.setShopType(listInfo.shopType == null ? 1 : Integer.parseInt(listInfo.shopType));
        spannelTextView.setDrawText(listInfo.name);
        preferentialPrice.setText("¥" + listInfo.preferentialPrice + "元");
        price.setText(listInfo.price + "元");
        //商品的url
        //取第一张加二维码
        Glide.with(this).load(shareImgsMap.get(list.get(0))).asBitmap()
                //压缩图片大小
                .override(300, 300)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);

                        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
                /*Bitmap qrImage = QRCodeUtils.createQRImage(QUrl.productShare + "?i=" + listInfo.alipayItemId + "&tkl=" + tkl
                        , (int) scaledDensity * 80, (int) scaledDensity * 80);*/
                        //生成二维码
                        Bitmap qrImage = QRCodeUtils.createQRImage(hostUrl + "/appH/html/details_share.html" + "?i=" + listInfo.alipayItemId + "&tkl=" + tkl
                                , (int) scaledDensity * 80, (int) scaledDensity * 80);
                        Message message = Message.obtain();
                        message.obj = qrImage;
                        shareHandler.sendMessage(message);

                    }
                });

        //下标为0的图片申请二维码 不需要额外分享
        for (int i = 1; i < list.size(); i++) {
            final String s = shareImgsMap.get(list.get(i));
            final String name = getExternalCacheDir() + "/" + picName + list.get(i) + ".png";
            //获取图片真正的宽高
            Glide.with(MyApplication.getContext())
                    .load(s)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    //压缩图片大小
                    .override(300, 300)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            //将除第一张之外的图存入本地
                            File dirs = new File(name);
                            FileOutputStream fileOutputStreams = null;
                            try {
                                fileOutputStreams = new FileOutputStream(dirs);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStreams);
                        }
                    });

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shareDialog != null) {
            shareDialog.dismiss();
            shareDialog = null;
        }
    }
}
