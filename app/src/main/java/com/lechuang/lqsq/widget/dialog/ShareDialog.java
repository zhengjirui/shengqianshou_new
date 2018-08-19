package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.utils.Utils;


/**
 * @author yrj
 * @date   2017/10/9
 * @E-mail 1422947831@qq.com
 * @desc   分享淘口令到微信的dialog
 */

public class ShareDialog extends Dialog{
    private TextView textView;
    //分享文案
    private String textShare;
    //微信
    private TextView tv_weixin;
    private Context context;
    public ShareDialog(Context context, String text) {
        super(context, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_weixin_share);
        this.context = context;
        this.textShare = text;

        init();

        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }

    private void init() {
        textView = (TextView) findViewById(R.id.tv_share);
        textView.setText(textShare);
        //点击打开微信
        findViewById(R.id.tv_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否安装微信
                if(Utils.isWeixinAvilible(context)){
                    //打开微信
                    Utils.openWeiXin(context);
                }else {
                    Utils.show(context,"未安装微信,不支持分享");
                }
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setText(String text) {
        this.textShare = text;

        textView.setText(text);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
