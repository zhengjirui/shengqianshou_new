package com.lechuang.lqsq.bean;

/**
 * @author yrj
 * @date 2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc 积分信息实体类
 */
public class OwnJiFenInfoBean {

    /**
     * data : {"integralRate":11,"integralTime":1504108800000,"withdrawDate":20,"withdrawIntegral":"0.00","withdrawMinPrice":110,"withdrawPrice":0}
     * error : Success
     * errorCode : 200
     */
    /**
     * integralRate : 11
     * integralTime : 1504108800000
     * withdrawDate : 20
     * withdrawIntegral : 0.00
     * withdrawMinPrice : 110
     * withdrawPrice : 0
     */
    //积分提现规则 1元 = ?积分
    public int integralRate;
    public long integralTime;
    public int withdrawDate;
    //用户可提现积分
    public String withdrawIntegral;
    //最小提现金额
    public int withdrawMinPrice;
    //用户可提现金额
    public double withdrawPrice;
    public String cashDeclaration;


}
