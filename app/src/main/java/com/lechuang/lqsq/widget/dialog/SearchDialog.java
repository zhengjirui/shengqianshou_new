package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lechuang.lqsq.R;


/**
 * @author yrj
 * @date 2017/10/9
 * @E-mail 1422947831@qq.com
 * @desc 版本更新的dialog
 */

public class SearchDialog extends Dialog {
    private TextView textView;
    private String mDesc;
    private Context mContext;

    public SearchDialog(Context context, String text) {
        super(context, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_search);
        this.mContext = context;
        this.mDesc = text;
        init();

        //setCancelable(true);
        //设置该属性之后点击dialog之外的地方不消失
        //setCanceledOnTouchOutside(false);
    }


    private void init() {
        textView = (TextView) findViewById(R.id.tv_desc);
        textView.setText(mDesc);
        //取消
        findViewById(R.id.tv_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public void setConfirmClickLisenter(View.OnClickListener onClickListener){
        if(onClickListener == null){
            return ;
        }
        findViewById(R.id.tv_sure).setOnClickListener(onClickListener);
    }

    public void setCancelClickLisenter(View.OnClickListener onClickListener){
        if(onClickListener == null){
            return ;
        }
        findViewById(R.id.tv_quxiao).setOnClickListener(onClickListener);
    }

}
