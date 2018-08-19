package com.lechuang.lqsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: guoning
 * Date: 2017/10/2
 * Description:
 */

public class AllProductBean implements Serializable{

    public List<ListInfo> productList;

    public static class ListInfo implements Serializable{
        public String alipayCouponId;
        public String alipayItemId;
        public long alipaySellerId;
        public int appraiseCount;
        public double commission;
        public long couponCount;
        public long couponEndTime;
        public String couponMoney;
        public String createTime;
        public double describeScore;
        public String errorMessage;
        public String id;
        public String img;
        public String imgs;
        public int isDataoke;
        public int isHours;
        public int isJhs;
        public int isNine;
        public int isOnehour;
        public int isPaoliang;
        public int isStarPP;
        public int  isTaoyouji;
        public int isTopHundred;
        public int isTqq;
        public int isTwentyNine;
        public int isTwohour;
        public int isTyjOwn;
        public int  isYishoudan;
        public String name;
        public int nowCount;
        public int nowNumber;
        public int orderNum;
        public String preferentialPrice;
        public String price;
        public String priceText;
        public String productName;
        public String productText;
        public String productUrl;
        public int programaId2;
        public int programaId4;
        public String shareIntegral;
        public String shareText;
        public String shopType;
        public long tbCreateTime;
        /*public String tbPrivilegeUrl;*/
        public List<String> smallImages;
        public String zhuanMoney;
    }
}
