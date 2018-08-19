package com.lechuang.lqsq.widget.views.loadmorelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lechuang.lqsq.R;


/**
 * Created by YST on 2016/9/9.
 */
public class FooterView extends LinearLayout {
    private View mContentView;

    public FooterView(Context context) {
        super(context);
        initView(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    private void initView(Context context) {
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.noscrolllistview_loadmore_footerview, null);
        addView(moreView);
        mContentView = moreView.findViewById(R.id.contentView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }


}
