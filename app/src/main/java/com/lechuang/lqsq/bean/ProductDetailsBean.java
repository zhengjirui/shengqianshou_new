package com.lechuang.lqsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YRJ
 * Date 2018/3/22.
 */

public class ProductDetailsBean implements Serializable {

    public List<ScrollbarObjectBean> list;

    public DetailsBean productWithBLOBs;

    /**
     * 滚动条对象
     */
    public static class ScrollbarObjectBean implements Serializable {
        public long createTime;
        public String createTimeStr;
        public String id;
        public String nickName;
        public String shareIntegral;
        public String shareIntegralText;
    }

    public static class DetailsBean implements Serializable {
        public String alipayCouponId;
        public String alipayItemId;
        public String alipaySellerId;
        public int appraiseCount;
        public int classTypeId;
        public String classTypeName;
        public double commission;
        public double commissionRecord;
        public int commissionType;
        public int couponCount;
        public String couponEndTime;
        public String couponEndTimeStr;
        public int couponMoney;
        public String couponStartTime;
        public String couponStartTimeStr;
        public String couponTimeStr;
        public long createTime;
        public double describeScore;
        public List<String> detailImages;
        public String disposeImg;
        public int enshrineCount;
        public int fansNumber;
        public String id;
        public String img;
        public String imgs;
        public double individualScore;
        public int isCollection;
        public int isCouponStatus;
        public int isDataoke;
        public int isHours;
        public int isJhs;
        public int isNine;
        public int isOnehour;
        public int isPaoliang;
        public int isStarPP;
        public int isStatus;
        public int isTaoyouji;
        public int isTopHundred;
        public int isTqq;
        public int isTwentyNine;
        public int isTwohour;
        public int isTyjOwn;
        public int isYishoudan;
        public double logisticsCode;
        public int maxOnlineCount;
        public String name;
        public int nowCount;
        public int nowNumber;
        public int nowOnlineCount;
        public double preferentialPrice;
        public double price;
        public String priceText;
        public String productName;
        public String productText;
        public String productUrl;
        public int programaId5;
        public int programaId6;
        public int programaId7;
        public double serviceCode;
        public String shareIntegral;
        public String shareText;
        public String shopId;
        public String shopName;
        public int shopType;
        public List<String> smallImages;
        public int softCommissionType;
        public int startCount;
        public int startNumber;
        public long tbCreateTime;
        public int twoHourNumber;
    }
}
