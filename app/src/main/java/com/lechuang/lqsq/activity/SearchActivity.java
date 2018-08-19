package com.lechuang.lqsq.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.HotSearchWord;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.utils.StringUtils;
import com.lechuang.lqsq.widget.views.ClearEditText;
import com.lechuang.lqsq.widget.views.TagFlowLayout.FlowLayout;
import com.lechuang.lqsq.widget.views.TagFlowLayout.TagAdapter;
import com.lechuang.lqsq.widget.views.TagFlowLayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Cache;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/03
 * 时间：13:51
 * 描述：
 */

public class SearchActivity extends BaseActivity implements TagFlowLayout.OnTagClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.et_product)
    ClearEditText etProduct;
    @BindView(R.id.flow_layout)
    TagFlowLayout flowLayout;
    @BindView(R.id.flow_layout0)
    TagFlowLayout flowLayout0;
    private static int whereSearch = 1;
    private List<String> historys;
    public final static String SEARCH_HISTORY = "serarchHistory";
    private TagAdapter<String> tagAdapter;
    private TagAdapter<String> tagAdapter0;
    private List<String> hot;
    private HotSearchWord srcData;

    public static void lanuchActivity(Context context, int where) {
        whereSearch = where;
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        etProduct.setOnEditorActionListener(this);
        String searchContent = getIntent().getStringExtra("searchContent");
        if (!TextUtils.isEmpty(searchContent)){
            etProduct.setText(searchContent);
        }
    }

    @Override
    public void initData() {
        historys = CacheManager.getInstance(this).getArray(SEARCH_HISTORY);
        if (historys == null) {
            historys = new ArrayList<>();
        }
        tagAdapter = new TagAdapter<String>(historys) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.tag_textview, flowLayout, false);
                tv.setText(s);
                return tv;
            }
        };

        flowLayout.setAdapter(tagAdapter);
        flowLayout.setOnTagClickListener(this);

        hot = new ArrayList<>();
        tagAdapter0 = new TagAdapter<String>(hot) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.tag_textview, flowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        flowLayout0.setAdapter(tagAdapter0);
        flowLayout0.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etProduct.setText(srcData.hswList.get(position).searchWord);
                search(srcData.hswList.get(position).searchWord);
                return true;
            }
        });
        subscription = Network.getInstance().getApi(CommenApi.class)
                .hotSearchWord()
                .onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<HotSearchWord>(SearchActivity.this) {
                    @Override
                    protected void success(HotSearchWord data) {
                        if (data.hswList == null || data.hswList.isEmpty()) return;
                        srcData = data;
                        for (int i = 0; i < (data.hswList.size() > 10 ? 10 : data.hswList.size()); i++) {
                            String searchWord = data.hswList.get(i).searchWord;
                            if (searchWord.length() > 5) {
                                searchWord = searchWord.substring(0, 4) + "...";
                            }
                            hot.add(searchWord);
                        }
                        tagAdapter0.notifyDataChanged();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }


    @OnClick({R.id.tv_search, R.id.iv_shanchu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                String search = etProduct.getText().toString().trim();
                search(search);
                break;
            case R.id.iv_shanchu:
                historys.clear();
                CacheManager.getInstance(this).remove(SEARCH_HISTORY);
                tagAdapter.notifyDataChanged();
                break;
        }
    }

    private void search(String search) {
        if (search == null || search.isEmpty()) {
            showShortToast("搜索内容不能为空");
            return;
        }
        if (StringUtils.containsEmoji(search)) {
            showShortToast("输入内容不能包含表情等字符");
            return;
        }
        if (!historys.contains(search)) {
            historys.add(0, search);
            if (historys.size() > 10) {
                historys.remove(10);
            }
            tagAdapter.notifyDataChanged();
        }

        if (whereSearch == 0) {
            Intent intent = new Intent();
            intent.putExtra("content", etProduct.getText().toString().trim());
            this.setResult(1, intent);
        } else {
            Intent intent = new Intent(this, SearchResultNewActivity.class);
            //传递一个值,搜索结果页面用来判断是从分类还是搜索跳过去的 1:分类 2:搜索界面
            intent.putExtra("type", 2);
            //rootName传递过去显示在搜索框上
            intent.putExtra("rootName", search);
            //rootId传递过去入参
            intent.putExtra("rootId", search);
            startActivity(intent);

        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        CacheManager.getInstance(this).put(SEARCH_HISTORY, historys);
        super.onDestroy();
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        etProduct.setText(historys.get(position));
        search(historys.get(position));
        return true;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    String search = etProduct.getText().toString().trim();
                    search(search);
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }
}
