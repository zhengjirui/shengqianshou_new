package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;

import com.lechuang.lqsq.R;


/**
 * @author yrj
 * @date   2017/10/9
 * @E-mail 1422947831@qq.com
 * @desc   分享赚钱的dialog
 */

public class ShareMoneyDialog extends Dialog{

    private Context context;

    public ShareMoneyDialog(Context context, int layoutID) {
        super(context, R.style.FullScreenDialog);
        setContentView(layoutID);
        this.context = context;
        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }


    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
