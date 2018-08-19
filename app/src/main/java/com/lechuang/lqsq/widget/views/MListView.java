package com.lechuang.lqsq.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 韩雪松 on 2016/12/8.
 */

public class MListView extends ListView {
    public MListView(Context context) {
        super(context);
    }
    public MListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expanSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expanSpec);
    }
}
