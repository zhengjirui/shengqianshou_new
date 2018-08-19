package com.lechuang.lqsq.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 韩雪松 on 2016/12/8.
 */

public class MGridView extends GridView {
    public MGridView(Context context) {
        super(context);
    }

    public MGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expanSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expanSpec);
    }
}
