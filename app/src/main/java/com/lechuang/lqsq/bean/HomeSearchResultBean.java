package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date   2017/10/3
 * @E-mail 1422947831@qq.com
 * @desc   首页搜索结果即分类
 */
public class HomeSearchResultBean {
    /*"data":{
        "productList":[
        {
            "alipayCouponId":"a3508d7083a04f459b811db988c42394",
                "alipayItemId":"556163171481",
                "alipaySellerId":"2731139683",
                "appraiseCount":222,
                "classTypeId":10,
                "classTypeName":"数码家电",
                "commission":20.3,
                "commissionType":4,
                "couponCount":0,
                "couponEndTime":1507305599,
                "couponMoney":1000,
                "couponStartTime":1506614400,
                "createTime":1507022035000,
                "describeScore":4.8,
                "disposeImg":"http://img.taoyouji666.com/1506795496_1171199.jpg",
                "enshrineCount":1386,
                "errorMessage":"sessionkey已过期",
                "id":"8tc0dskl",
                "img":"http://img.alicdn.com/imgextra/i2/2731139683/TB2RSQDcTZRMeJjSsppXXXrEpXa-2731139683.jpg",
                "imgs":"http://img.alicdn.com/imgextra/i2/2731139683/TB2RSQDcTZRMeJjSsppXXXrEpXa-2731139683.jpg_400x400.jpg",
                "individualScore":0,
                "isCouponStatus":0,
                "isDataoke":0,
                "isHours":0,
                "isJhs":0,
                "isNine":0,
                "isOnehour":0,
                "isPaoliang":0,
                "isStarPP":0,
                "isStatus":0,
                "isTaoyouji":1,
                "isTopHundred":0,
                "isTqq":0,
                "isTwentyNine":0,
                "isTwohour":0,
                "isTyjOwn":0,
                "isYishoudan":0,
                "logisticsCode":4.8,
                "maxOnlineCount":3113,
                "name":"海尔空气净化器除甲醛雾霾",
                "nowCount":0,
                "nowNumber":740,
                "nowOnlineCount":145,
                "orderNum":321,
                "preferentialPrice":"2099.00",
                "price":3099,
                "priceText":"【原价3099.00元】券后【2099.00元】包邮",
                "productName":"海尔空气净化器除甲醛雾霾",
                "productText":"海尔制造，远程WIFI控制，甲醛值高达330，高效除甲醛，净化只需6分钟，快速给家人净化出新鲜空气~",
                "productUrl":"https://detail.tmall.com/item.htm?id=556163171481",
                "programaId4":4,
                "serviceCode":4.8,
                "shopId":"145610763",
                "shopName":"haier海尔润氏专卖店",
                "shopType":"2",
                "softCommissionType":0,
                "startCount":0,
                "startNumber":274,
                "tbCreateTime":1507021715000,
                "tbPrivilegeUrl":"https://uland.taobao.com/coupon/edetail?activityId=a3508d7083a04f459b811db988c42394&pid=mm_111810781_36920844_133724568&itemId=556163171481&src=lc_tczs",
                "twoHourNumber":2
        }
        ]
    },
            "error":"Success",
            "errorCode":200*/
    //商品数据
    public List<HomeSearchResultBean.ProductListBean> productList;
    public static class ProductListBean{
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
        //转链接
        // public String tbPrivilegeUrl;
        public String alipayCouponId;
        //1 淘宝 2天猫
        public String shopType;
        public String shareIntegral;
        //销量
        public int nowNumber;
        //券价格
        public String couponMoney;
        public String zhuanMoney;

        public List<String> smallImages;
        //分享文案
        public String shareText;
    }
}
