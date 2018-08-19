package com.lechuang.lqsq.net;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/9/26
 * 时间：10:11
 */

public interface Constant {
    String login_success = "login_success";
    String logout = "logout";
    int need_relogin = 1;
    String userinfo_close = "userinfo_close";
    String qiandao = "qiandao";
    String KEY_ROOT_ID = "rootId";





















    int ERROR_NET = 400;
    int END_NET = 100;
    int SUCCESSED =200 ;
    //最大的图片选择数
    String MAX_SELECT_COUNT = "max_select_count";
    //是否单选
    String IS_SINGLE = "is_single";
    //初始位置
    String POSITION = "position";

    //初始位置
    String IS_CONFIRM = "is_confirm";
    int RESULT_CODE = 0x00000012;
    //头像名称
    String HEAD = "/head.jpg";
    //60秒倒计时
    int TIME = 60;
    //广告图6秒倒计时
    int ADVERTISEMENT_TIME = 6;
    //网络请求状态码
    int STATE = 200;
    // sp文件的名称
    String SP_NAME = "configuration";
    // 保存用户信息的sp文件的名称
    String USERINFO = "loginInfo";
    //淘宝pid(百川用户pid   非常重要  注意替换)
    String PID = "mm_30431596_0_0";
    //appkey  (百川的appkey 用于客服功能  注意替换)
    String APP_KEY = "24551638";
    //app  package_fileProvider
    String package_fileProvider = "com.lechuang.shengxinyoupin.fileProvider";
    //是否是代理
    String isAgencyStatus = "isAgencyStatus";
    //传递商品信息到商品详情的键
    String listInfo = "listInfo";
    //存商品分享域名的key
    String getShareProductHost = "getShareProductHost";
    String KEY_PAY_PRICE = "payPriceStr";
}
