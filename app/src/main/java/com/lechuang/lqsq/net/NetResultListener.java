package com.lechuang.lqsq.net;

import java.util.List;

import okhttp3.Request;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/20
 * 时间：15:36
 */

public interface NetResultListener<T, K> {
    void success(String url, T data, List<K> datas, Request request);

    void error(String url, int errCode, String errorMsg, Request request);
}
