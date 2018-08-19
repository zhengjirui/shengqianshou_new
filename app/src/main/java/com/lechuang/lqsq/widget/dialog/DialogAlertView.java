package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author zhangfeng 2017-08-07
 * 对话框
 * */
public class DialogAlertView extends Dialog {
    private Context mContext;
    private LayoutInflater inflater;
    private View dialog;
    private int gravity= Gravity.CENTER_HORIZONTAL;

    public DialogAlertView(Context context) {
        super(context);
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
    }

    public DialogAlertView(int gravity , Context context) {
        super(context);
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.gravity = gravity;
    }

    public DialogAlertView(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
    }

    public void setView(int layoutId) {
        dialog = inflater.inflate(layoutId, null);
    }

    public View getLayoutView(){
        return this.dialog;
    }

    public View findViewById(int view){
        return this.dialog.findViewById(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(dialog);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.alpha = 0.9f;
        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        dialogWindow.setGravity(gravity);
        dialogWindow.setAttributes(lp);
    }
}
