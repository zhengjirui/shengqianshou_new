package com.lechuang.lqsq.net.rxjava;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/24
 * 时间：10:51
 */

public class NetResultException extends Exception {
    private int httpCode;
    private int resultCode;

    public NetResultException(String message, int httpCode, int resultCode) {
        super(message);
        this.httpCode = httpCode;
        this.resultCode = resultCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
