package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/2.
 */

public class HomeProgramDetailBean {
    /*"data":

       {
           "indexBannerList":[
           {
               "id":"mhdu",
                   "img":"http://img.taoyouji666.com/bde2627b8596051bd4182fb3ff774667",
                   "link":"http://啊是的发生大发",
                   "pname":"爆款单",
                   "programaId":1,
                   "type":1
           }
           ],
           "productList":[
           {
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
           }]
       },
               "error":"Success",
               "errorCode":200*/
    //轮播图
    public List<IndexBannerListBean> indexBannerList;

    public static class IndexBannerListBean {

        /* {
             "id":"mhdu",
                 "img":"http://img.taoyouji666.com/bde2627b8596051bd4182fb3ff774667",
                 "link":"http://啊是的发生大发",
                 "pname":"爆款单",
                 "programaId":1,
                 "type":1
         }*/
        public String id;
        public String img;
        public String link;
        public String pname;

    }

    //商品
    public List<ProductListBean> productList;

    public static class ProductListBean {

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
        //分享图片列表
        public List<String> smallImages;
        public String shareText;

    }

}
