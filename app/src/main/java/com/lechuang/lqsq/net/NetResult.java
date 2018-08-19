package com.lechuang.lqsq.net;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/20
 * 时间：10:08
 */

public class NetResult<T> {
    public String error;
    public int errorCode;
    public T data;
    public String moreInfo;
}
