package com.lechuang.lqsq.bean;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/21
 * 时间：09:01
 */

public class UserInfo {
    public String customerServiceId;
    public int integralShowFlag;
    public String openImPassword;
    public float sumIntetral;
    public int firstLoginFlag;//是否是首次登录 0 第一次登录 1 已登录过 (判断是否弹红包用
    public String salt;
    public int superiorId;
    public int vipGradeId;
    public String alipayNumber;
    public long createTime;
    public String createTimeStr;
    public String id;
    public String nickName;
    public String password;
    public String phone;
    public String photo;
    public String safeToken;
    //签到状态
    public int signedStatus;
    public int status;
    public String taobaoNumber;
    public int verifiCode;
    //是否是代理
    public int isAgencyStatus;
    public String vipGradeName;  //可返利数
    public int agencyNum;  //下级人数
    public String withdrawIntegral;
    //已获得积分
    public String haveIntegral;
    public String sumIntegral;
    //微信和QQ登录的相关信息
    public String weixinName;
    public String weixinPhoto;
    public String qqName;
    public String qqPhoto;
    public String openid;//微信或者QQ的openid
}
