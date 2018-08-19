package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date 2017/10/2
 * @E-mail 1422947831@qq.com
 * @desc 首页最下栏目实体类
 */
public class HomeLastProgramBean {

   /* "data":

    {
        "productList":[
        {
            "alipayCouponId":"ff00bd0348514789a5a889c050cba636",
                "alipayItemId":"557346600329",
                "alipaySellerId":"3363593646",
                "commission":30.5,
                "commissionType":4,
                "couponEndTime":1506355199,
                "couponMoney":20,
                "couponStartTime":1505750400,
                "errorMessage":"sessionkey已过期",
                "id":"ak11ilcu",
                "img":
            "https://img.alicdn.com/imgextra/i2/895280930/TB2dxu2X2BNTKJjSszeXXcu2VXa_!!895280930.jpg",
                    "imgs":
            "https://img.alicdn.com/imgextra/i2/895280930/TB2dxu2X2BNTKJjSszeXXcu2VXa_!!895280930.jpg_400x400.jpg",
                    "nowNumber":1466,
                "preferentialPrice":"29.00",
                "price":49,
                "productName":"【6块装】红色小象婴儿洗衣皂",
                "productText":"婴儿洗衣皂6块，实惠装温和配方，深层清洁护手精华亮白柔护，安全无刺激，安全到可以嚼的肥皂，赵薇倾情代言~",
                "productUrl":"https://detail.tmall.com/item.htm?id=557346600329",
                "programaId1":1,
                "programaId3":3,
                "programaId5":5,
                "shopType":"2",
                "tbCreateTime":1506761640445,
                "tbPrivilegeUrl":
            "https://uland.taobao.com/coupon/edetail?activityId=ff00bd0348514789a5a889c050cba636&pid=mm_111810781_36920844_133724568&itemId=557346600329&src=lc_tczs"
        }
        ],
        "programa":{
        "dataId":2,
                "id":"1k6x1",
                "indexBannerList":[
        {
            "id":"mhbe",
                "img":"http://img.taoyouji666.com/564d1786c32d61b086db3a90638d7611",
                "link":"http://www.baidu.com",
                "type":8
        }
            ],
        "lookAll":"查看全部",
                "name":"逛商城",
                "progNick":"栏目五"
    }
    },
    "error":"Success",
    "errorCode":200*/

    //public DataBean data;
    //public String error;
    //public int errorCode;


    //public static class DataBean {
    //商品集合
    public List<ListBean> productList;

    public static class ListBean {
        public String id;
        //图片
        public String imgs;
        //商品标题
        public String name;
        //价格
        public Double price;
        //券后价
        public String preferentialPrice;
        //商品id,传给H5页面的参数
        public String alipayItemId;
        /* //转链接之后的链接
         public String tbPrivilegeUrl;*/
        public String alipayCouponId;
        // 1 淘宝 2天猫
        public String shopType;
        public String shareIntegral;
        // 赚金额
        public String zhuanMoney;
        // 销量
        public int nowNumber;
        // 券金额
        public String couponMoney;
        //分享图片列表
        public List<String> smallImages;
        public String shareText;
        public String productText;
        public String productName;
    }

    public programaBean programa;

    //广告图
    public static class programaBean {
        public int dataId;
        public String id;
        public List<ListBean> indexBannerList;

        public static class ListBean {
            public String id;
            //图片
            public String img;
            //链接
            public String link;
            public int type;

        }
    }
    //}


}
