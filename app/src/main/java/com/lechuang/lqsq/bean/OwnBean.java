package com.lechuang.lqsq.bean;

/**
 * 作者：li on 2017/10/6 15:48
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class OwnBean {

    public  Agency agency;

    public static class Agency{

        public int agencyShareRate;
        public String allianceLicenseId;
        public long androidAppkey;
        public String androidSecret;
        public int avtiveAward;
        public int  avtiveSourceAward;
        public int baskAward;
        public int discountRate;
        public int exchangeInterval;
        public String id;
        public String img;
        public String integralName;
        public int integralRate;
        public long iosAppkey;
        public String iosSecret;
        public double payPrice;
        public double payPriceStr;
        public int perfectAlipayAward;
        public int perfectMaterialAward;
        public int perfectNicknameAward;
        public int perfectPhoneAward;
        public int perfectTaobaoTward;
        public double pushMoneyRate;
        public int signAward;
        public  int uploadImgAward;
        public  double upushMoneyRate;
        public int withdrawDate;
        public double withdrawMinPrice;
        public int type;
    }

    public Pay pay;

    public static class Pay{
        public String sign;
    }

}
