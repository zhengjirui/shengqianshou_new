package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.ProfitActivity;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/01/31
 * 时间：13:50
 * 描述：
 */

public class RedPacketDialog extends Dialog implements View.OnClickListener {
    private Context context;

    public RedPacketDialog(Context context) {
        super(context, R.style.myDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_packet);
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.chakan).setOnClickListener(this);
        TextView show = (TextView) findViewById(R.id.num);
        show.setText("5元红包奖励");
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.chakan:
                //
                context.startActivity(new Intent(context, ProfitActivity.class));
                dismiss();
                break;
        }
    }
}
