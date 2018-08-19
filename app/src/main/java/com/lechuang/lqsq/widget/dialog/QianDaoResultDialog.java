package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lechuang.lqsq.R;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/31
 * 时间：17:18
 * 描述：
 */

public class QianDaoResultDialog extends Dialog {
    private Context context;

    public QianDaoResultDialog(Context context) {
        super(context, R.style.myDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_qiandao);
    }
}
