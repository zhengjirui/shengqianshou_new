package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.lechuang.lqsq.R;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/31
 * 时间：16:56
 * 描述：
 */

public class JiFenShuoming extends Dialog {
    public JiFenShuoming(Context context) {
        super(context, R.style.myDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jifenshuoming);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
