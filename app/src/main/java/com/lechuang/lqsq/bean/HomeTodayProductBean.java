package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * Created by xianren on 2017/12/5.
 */

public class HomeTodayProductBean {
    public List<ProudList> allproductList;
//
//    public static class ListBean {
//        public int adverSort4;
//        public int adverSort5;
//        public String alipayCouponId;
//        public String alipayItemId;
//        public String alipaySellerId;
//        public int classTypeId;
//        public int commission;
//        public int commissionType;
//        public int couponMoney;
//        public String id;
//        public String img;
//        public String imgs;
//        public int isStatus;
//        public int nowCount;
//        public int nowNumber;
//        public String preferentialPrice;
//        public int price;
//        public String productText;
//        public String productUrl;
//        public int programaId4;
//        public String shareIntegral;
//        public String shopType;
//        public int startNumber;
//        //图片
//        public List<String> smallImages;
//    }
//
//    public static class ListBean2 {
//        public String alipayCouponId;
//        public String alipayItemId;
//        public String alipaySellerId;
//        public int appraiseCount;
//        public int classTypeId;
//        public String classTypeName;
//        public int commission;
//        public int couponCount;
//        public String couponEndTime;
//        public String couponEndTimeStr;
//        public int couponMoney;
//        public String couponStartTime;
//        public int couponStartTimeStr;
//        public int couponTimeStr;
//        public int createTime;
//        public String describeScore;
//        public int disposeImg;
//        public String enshrineCount;
//        public String id;
//        public String img;
//        public String imgs;
//        public int individualScore;
//        public int isCouponStatus;
//        public int isDataoke;
//        public int isHours;
//        public int isJhs;
//        public int isNine;
//        public int isPaoliang;
//        public int isStarPP;
//        public int isStatus;
//        public int isTqq;
//        public int isTwentyNine;
//        public int isTwohour;
//        public int isTyjOwn;
//        public int isYishoudan;
//        public int logisticsCode;
//        public int maxOnlineCount;
//        public String name;
//        public int nowCount;
//        public int nowNumber;
//        public int nowOnlineCount;
//        public String preferentialPrice;
//        public String priceText;
//        public int price;
//        public String productName;
//        public String productUrl;
//        public String productText;
//        public int programaId2;
//        public int programaId5;
//        public int serviceCode;
//        public String shareIntegral;
//        public String shopName;
//        public String shopType;
//        //图片
//        public List<String> smallImages;
//        public int CommissionType;
//        public int startCount;
//        public int startNumber;
//        public int twoHourNumber;
//        public String zhuanMoney;
//
//    }

    public static class adProductList {

        /*{
            "alipayCouponId":"2860491910564bea8cbd3884aa4d5819",
                "alipayItemId":"556073549962",
                "alipaySellerId":"407450207",
                "appraiseCount":0,
                "commission":30.5,
                "couponCount":50000,
                "couponEndTime":1507219199,
                "couponMoney":25,
                "createTime":1506945002000,
                "describeScore":4.8,
                "errorMessage":"sessionkey已过期",
                "id":"cblub23j",
                "img":
            "https://img.alicdn.com/imgextra/i3/1050131635/TB2jGIjcLNZWeJjSZFpXXXjBFXa_!!1050131635.jpg",
                    "imgs":
            "https://img.alicdn.com/imgextra/i3/1050131635/TB2jGIjcLNZWeJjSZFpXXXjBFXa_!!1050131635.jpg_400x400.jpg",
                    "isDataoke":1,
                "isHours":0,
                "isJhs":0,
                "isNine":0,
                "isOnehour":0,
                "isPaoliang":1,
                "isStarPP":0,
                "isTaoyouji":0,
                "isTopHundred":1,
                "isTqq":0,
                "isTwentyNine":1,
                "isTwohour":0,
                "isTyjOwn":0,
                "isYishoudan":1,
                "name":"【Don’t Stay】韩版百搭九分小脚裤",
                "nowCount":0,
                "nowNumber":8375,
                "preferentialPrice":"14.90",
                "price":39.9,
                "priceText":"【原价39.90元】券后【14.90元】包邮",
                "productName":"【Don’t Stay】韩版百搭九分小脚裤",
                "productText":"女神都爱穿的裤子！360°弹力面料，柔软舒适，提臀收腹，修身显瘦，让双腿更加纤细立体，轻松穿出S形！",
                "productUrl":"https://detail.tmall.com/item.htm?id=556073549962",
                "programaId1":1,
                "programaId2":2,
                "programaId3":3,
                "shopType":"2",
                "tbCreateTime":1506908617000,
                "tbPrivilegeUrl":
            "https://uland.taobao.com/coupon/edetail?activityId=2860491910564bea8cbd3884aa4d5819&pid=mm_111810781_36920844_133724568&itemId=556073549962&src=lc_tczs"
        }*/
        //商品id
        public String alipayItemId;
        public String id;
        //商品图
        public String imgs;
        //商品名
        public String name;
        //原价
        public String price;
        //优惠价
        public String preferentialPrice;

        /* //转链接
         public String tbPrivilegeUrl;*/
        //商品类别 1 淘宝 2天猫
        public String shopType;
        public String alipayCouponId;
        public String shareIntegral;
        //销量
        public String nowNumber;
        public List<String> smallImages;
        public String shareText;


    }

    public static class productList {

        /*{
            "alipayCouponId":"2860491910564bea8cbd3884aa4d5819",
                "alipayItemId":"556073549962",
                "alipaySellerId":"407450207",
                "appraiseCount":0,
                "commission":30.5,
                "couponCount":50000,
                "couponEndTime":1507219199,
                "couponMoney":25,
                "createTime":1506945002000,
                "describeScore":4.8,
                "errorMessage":"sessionkey已过期",
                "id":"cblub23j",
                "img":
            "https://img.alicdn.com/imgextra/i3/1050131635/TB2jGIjcLNZWeJjSZFpXXXjBFXa_!!1050131635.jpg",
                    "imgs":
            "https://img.alicdn.com/imgextra/i3/1050131635/TB2jGIjcLNZWeJjSZFpXXXjBFXa_!!1050131635.jpg_400x400.jpg",
                    "isDataoke":1,
                "isHours":0,
                "isJhs":0,
                "isNine":0,
                "isOnehour":0,
                "isPaoliang":1,
                "isStarPP":0,
                "isTaoyouji":0,
                "isTopHundred":1,
                "isTqq":0,
                "isTwentyNine":1,
                "isTwohour":0,
                "isTyjOwn":0,
                "isYishoudan":1,
                "name":"【Don’t Stay】韩版百搭九分小脚裤",
                "nowCount":0,
                "nowNumber":8375,
                "preferentialPrice":"14.90",
                "price":39.9,
                "priceText":"【原价39.90元】券后【14.90元】包邮",
                "productName":"【Don’t Stay】韩版百搭九分小脚裤",
                "productText":"女神都爱穿的裤子！360°弹力面料，柔软舒适，提臀收腹，修身显瘦，让双腿更加纤细立体，轻松穿出S形！",
                "productUrl":"https://detail.tmall.com/item.htm?id=556073549962",
                "programaId1":1,
                "programaId2":2,
                "programaId3":3,
                "shopType":"2",
                "tbCreateTime":1506908617000,
                "tbPrivilegeUrl":
            "https://uland.taobao.com/coupon/edetail?activityId=2860491910564bea8cbd3884aa4d5819&pid=mm_111810781_36920844_133724568&itemId=556073549962&src=lc_tczs"
        }*/
        //商品id
        public  String id;
        public String alipayItemId;
        public String programaId;
        //商品图
        public String imgs;
        //商品名
        public String name;
        //原价
        public Double price;
        //优惠价
        public String preferentialPrice;
        /* //转链接
         public String tbPrivilegeUrl;*/
        //商品类别 1 淘宝 2天猫
        public String shopType;
        public String alipayCouponId;
        public String shareIntegral;
        //销量
        public String nowNumber;
        public List<String> smallImages;

    }

    public static class ProudList {
        public List<adProductList> adProductList;
        public List<productList> productList;
        public String programName;
    }
    public static class SubroudList {
        public List<IndexBanner> indexBannerList;
        public List<productList> productList;
        public String programName;
    }
    public  class  IndexBanner
    {
        public  String id;
        public  String img;
        public  String link;
        public  String pname;
        public  String programaId;
    }

}
