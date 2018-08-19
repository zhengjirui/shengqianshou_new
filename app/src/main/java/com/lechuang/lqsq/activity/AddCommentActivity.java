package com.lechuang.lqsq.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.TipoffShowApi;
import com.lechuang.lqsq.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/07
 * 时间：14:12
 * 描述：添加评论
 */

public class AddCommentActivity extends BaseNormalTitleActivity {
    @BindView(R.id.content)
    EditText content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_comment;
    }

    @Override
    public void initView() {
        setTitleName("添加评论");
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.comlete)
    public void onViewClicked() {
        complete();
    }

    private void complete() {
        String etContent = content.getText().toString().trim();
        if (TextUtils.isEmpty(etContent)) {
            showShortToast("请输入内容");
            return;
        }
        //不能包含emoji表情
        if (Utils.containsEmoji(etContent)) {
            showShortToast(R.string.no_emoji);
            return;
        }
        int type = getIntent().getIntExtra("type", 1);
        String id = getIntent().getStringExtra("tipId");
        Network.getInstance().getApi(TipoffShowApi.class)
                .sendContent(id, type, etContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<String>(AddCommentActivity.this) {
                    @Override
                    public void success(String result) {
                        showShortToast(result);
                        finish();
                    }

                    @Override
                    public void error(int code, String msg) {

                    }
                });
    }
}
