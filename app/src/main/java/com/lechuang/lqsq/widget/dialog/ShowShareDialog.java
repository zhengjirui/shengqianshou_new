package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.lechuang.lqsq.R;
import com.lechuang.lqsq.utils.ShareUtils;

import java.io.File;


/**
 * @author: LGH
 * @since: 2018/3/16
 * @describe:
 */

public class ShowShareDialog extends Dialog implements View.OnClickListener {

    private Context mContent;
    private File[] mImgUrlFiles;
    private String mImgUrl;
    private LinearLayout mShareParentLayout;
    private ProgressBar mShareLoading;

    public ShowShareDialog(Context context) {
        super(context, R.style.BottomDialogStyle);
        this.mContent = context;
        setContentView(R.layout.show_share_dialog);
        init();
    }

    public ShowShareDialog(Context context, File[] imgUrlFiles) {
        super(context, R.style.BottomDialogStyle);
        setContentView(R.layout.show_share_dialog);
        this.mContent = context;
        this.mImgUrlFiles = imgUrlFiles;
        init();
        //后期准备吧处理图片的过程放在弹出框里面进行，避免点击分享时的等待时间过长

        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }

    public ShowShareDialog(Context context, String imgUrl) {
        super(context, R.style.BottomDialogStyle);
        setContentView(R.layout.show_share_dialog);
        this.mContent = context;
        this.mImgUrl = imgUrl;
        init();

        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }

    private void init() {
        mShareParentLayout = (LinearLayout) findViewById( R.id.share_parent_layout);
        findViewById( R.id.share_qq_kongjian).setOnClickListener(this);
        findViewById( R.id.share_qq).setOnClickListener(this);
        findViewById( R.id.share_weixin).setOnClickListener(this);
        findViewById( R.id.share_friends).setOnClickListener(this);
        findViewById( R.id.share_sinawbo).setOnClickListener(this);
        mShareLoading = (ProgressBar) findViewById( R.id.share_loading);

        //处理图片前显示加载等待，处理完图片逻辑后显示可点击分享的渠道
        mShareParentLayout.setVisibility(View.GONE);
        mShareLoading.setVisibility(View.VISIBLE);
        createImage();//处理图片的逻辑

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);

    }

    /**
     * 处理图片问题
     */
    private void createImage(){



        //处理完图片后显示可点击的分享渠道
        mShareParentLayout.setVisibility(View.VISIBLE);
        mShareLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.share_qq_kongjian) {
            ShareUtils.shareToQQZ(mContent,mImgUrlFiles,mImgUrl);
        } else if (id == R.id.share_qq) {
            ShareUtils.shareToQQ(mContent,mImgUrlFiles,mImgUrl);
        } else if (id == R.id.share_weixin) {
            ShareUtils.shareToWChart(mContent,mImgUrlFiles,mImgUrl);
        } else if (id == R.id.share_friends) {
            ShareUtils.shareToWChartFs(mContent,mImgUrlFiles,mImgUrl);
        } else if(id == R.id.share_sinawbo){
            ShareUtils.shareToSinaWeiBo(mContent,mImgUrlFiles,mImgUrl);
        }
        this.dismiss();

    }


    public File[] getImgUrlFiles() {
        return mImgUrlFiles;
    }

    public void setImgUrlFiles(File[] imgUrlFiles) {
        mImgUrlFiles = imgUrlFiles;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }
}
