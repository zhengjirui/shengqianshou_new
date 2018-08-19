package com.lechuang.lqsq.bean;

/**
 * @author yrj
 * @date 2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc 用户信息实体类
 */
public class OwnUserInfoBean {

    /**
     * data : {"alipayNumber":"123456","createTime":1505873938000,"createTimeStr":"2017-09-20 10:18:58","id":"26o3r","nickName":"红胡明明","password":"cb1dbaf2a552df2ea9877c6d5e46f2b294bbf4e2d585b0090e7ac2618e74e59f87cc09f0fdd94d5c","phone":"15384067637","photo":"http://img.taoyouji666.com/B441F5F9274AE5E13990840DC9C6880F?imageView2/2/w/150/q/90","safeToken":"B372EA05F590599337EF0E47AF18ADE4","signedStatus":1,"status":1,"taobaoNumber":"春暖静待花开","verifiCode":755000}
     * error : Success
     * errorCode : 200
     */

    /**
     * alipayNumber : 123456
     * createTime : 1505873938000
     * createTimeStr : 2017-09-20 10:18:58
     * id : 26o3r
     * nickName : 红胡明明
     * password : cb1dbaf2a552df2ea9877c6d5e46f2b294bbf4e2d585b0090e7ac2618e74e59f87cc09f0fdd94d5c
     * phone : 15384067637
     * photo : http://img.taoyouji666.com/B441F5F9274AE5E13990840DC9C6880F?imageView2/2/w/150/q/90
     * safeToken : B372EA05F590599337EF0E47AF18ADE4
     * signedStatus : 1
     * status : 1
     * taobaoNumber : 春暖静待花开
     * verifiCode : 755000
     */

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
}
