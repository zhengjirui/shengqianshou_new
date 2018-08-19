package com.lechuang.lqsq.widget.views.loadmorelistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lechuang.lqsq.utils.LogUtils;


/**
 * Created by YST on 2016/9/9.
 */
public class LoadMoreListView extends ListView implements AdapterView.OnItemClickListener {
    private static final String TAG = LoadMoreListView.class.getSimpleName();
    private FooterView footerView;
    private boolean isLoading;
    private boolean isLoadMore;
    private LoadListener loadListener;
    private OnItemClickListener itemClickListener;

    public LoadMoreListView(Context context) {
        super(context);
        initView();
    }


    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        footerView = new FooterView(getContext());
        addFooterView(footerView);
        footerView.hide();
        setOnScrollListener(listener);
        super.setOnItemClickListener(this);
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
        if (!loadMore)
            footerView.hide();
    }

    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (!isLoadMore) {
                return;
            }
            if (firstVisibleItem + visibleItemCount + 2 == totalItemCount) {
                LogUtils.e(TAG, "loadMore");
                loadMore();
            }
        }
    };

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        itemClickListener = listener;
    }

    private void loadMore() {
        if (isLoading) {
            return;
        }
        if (loadListener != null) {
            footerView.show();
            isLoading = true;
            loadListener.loadMore();
        }
    }

    public void loadDown(boolean isSucess) {
        if (isSucess)
            footerView.hide();
        isLoading = false;
    }


    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count = getAdapter().getCount() - getFooterViewsCount();
        if (isLoadMore && position == count) {
            loadMore();
            return;
        }
        if (itemClickListener != null) {
            itemClickListener.onItemClick(parent, view, position, id);
        }

    }

    public interface LoadListener {
        void loadMore();
    }


}
