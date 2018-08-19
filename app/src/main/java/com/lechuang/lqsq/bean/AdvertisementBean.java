package com.lechuang.lqsq.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author yrj
 * @date 2017/10/30
 * @E-mail 1422947831@qq.com
 * @desc 进入app时的广告图
 */

public class AdvertisementBean implements Serializable{

    /**
     * data : {"advertisingImg":{"adImage":"http://img.taoyouji666.com/589d6bc7e5774b9c0ebc2bee7040a7f3","adSort":3,"alipayItemId":"557616027842","alipaySellerId":"228784630","commission":0,"commissionType":0,"couponMoney":300,"couponUrl":"https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6","couponUrlType":0,"img":"//img.alicdn.com/imgextra/i2/228784630/TB267F4XEtWMKJjy0FaXXcCDpXa_!!228784630.jpg","isDelete":0,"isStatus":0,"nowCount":0,"nowNumber":780,"price":389,"productName":"裂帛2017秋冬新款V领中长款刺绣外套宽松毛衣针织开衫女51170527","productUrl":"https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6","startNumber":0,"type":1}}
     * error : Success
     * errorCode : 200
     */


    /**
     * advertisingImg : {"adImage":"http://img.taoyouji666.com/589d6bc7e5774b9c0ebc2bee7040a7f3","adSort":3,"alipayItemId":"557616027842","alipaySellerId":"228784630","commission":0,"commissionType":0,"couponMoney":300,"couponUrl":"https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6","couponUrlType":0,"img":"//img.alicdn.com/imgextra/i2/228784630/TB267F4XEtWMKJjy0FaXXcCDpXa_!!228784630.jpg","isDelete":0,"isStatus":0,"nowCount":0,"nowNumber":780,"price":389,"productName":"裂帛2017秋冬新款V领中长款刺绣外套宽松毛衣针织开衫女51170527","productUrl":"https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6","startNumber":0,"type":1}
     */

    public AdvertisingImgBean advertisingImg;

    public static class AdvertisingImgBean implements Serializable {
        /**
         * adImage : http://img.taoyouji666.com/589d6bc7e5774b9c0ebc2bee7040a7f3
         * adSort : 3
         * alipayItemId : 557616027842
         * alipaySellerId : 228784630
         * commission : 0
         * commissionType : 0
         * couponMoney : 300
         * couponUrl : https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6
         * couponUrlType : 0
         * img : //img.alicdn.com/imgextra/i2/228784630/TB267F4XEtWMKJjy0FaXXcCDpXa_!!228784630.jpg
         * isDelete : 0
         * isStatus : 0
         * nowCount : 0
         * nowNumber : 780
         * price : 389
         * productName : 裂帛2017秋冬新款V领中长款刺绣外套宽松毛衣针织开衫女51170527
         * productUrl : https://detail.tmall.com/item.htm?spm=a230r.1.14.43.29ef9cadJCAIZV&id=557616027842&ns=1&abbucket=6
         * startNumber : 0
         * type : 1
         */

        public String adImage;
        public String adSort;
        public String alipayItemId;
        public String alipayCouponId;
        public String alipaySellerId;
        public String couponMoney;
        public String couponUrl;
        public String couponUrlType;
        public String img;
        public String isDelete;
        public String isStatus;
        public String nowCount;
        public int nowNumber;
        public String price;
        public String preferentialPrice;
        public String productName;
        public String productUrl;
        public int startNumber;
        public int type;
        //广告的跳转链接
        public String adUrl;
        public String shopType;
        public String shareIntegral;
        public List<String> smallImages;
        public String shareText;
    }

}
