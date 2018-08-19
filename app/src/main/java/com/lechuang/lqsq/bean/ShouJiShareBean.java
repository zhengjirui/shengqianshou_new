package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author: zhengjr
 * @since: 2018/7/2
 * @describe:
 */

public class ShouJiShareBean {

    /**
     * circleFriend : {"cfComments":"39W好评，4.9高分！【卫龙旗舰店】大面筋106g*5 包\n重要的事情说三遍：全是好吃的大面筋！大面筋！大面筋！\n","cfCouponAfterPrice":19.9,"cfCouponUrl":"https://uland.taobao.com/quan/detail?sellerId=2200070681&activityId=a02b29dd0c4c42988b33b2e9caf57804","cfCreateTime":1530460800000,"cfMasterId":26,"cfMasterImg":"http://img.taoyouji666.com/12efbef75bf6fa1bb306b53e296ad1b8?imageView2/2/w/480/q/90","cfMasterName":"省钱手发单员","cfPrice":22.9,"cfProductId":"521497533935","cfProductTitle":"卫龙辣条，小时候的味道","cfProductUrl":"https://detail.tmall.com/item.htm?id=521497533935","cfShareCopy":"第一次旗舰店活动！\n超市一包7.8元，咱5包【券后19.9元】包邮","cfType":"0","detailImages":["http://img.taoyouji666.com/71E02DD80F8540346A452BE3350E9443?imageView2/2/w/480/q/90"],"id":"b8tl"}
     */

    public CircleFriendBean circleFriend;

    public static class CircleFriendBean {
        /**
         * cfComments : 39W好评，4.9高分！【卫龙旗舰店】大面筋106g*5 包
         重要的事情说三遍：全是好吃的大面筋！大面筋！大面筋！

         * cfCouponAfterPrice : 19.9
         * cfCouponUrl : https://uland.taobao.com/quan/detail?sellerId=2200070681&activityId=a02b29dd0c4c42988b33b2e9caf57804
         * cfCreateTime : 1530460800000
         * cfMasterId : 26
         * cfMasterImg : http://img.taoyouji666.com/12efbef75bf6fa1bb306b53e296ad1b8?imageView2/2/w/480/q/90
         * cfMasterName : 省钱手发单员
         * cfPrice : 22.9
         * cfProductId : 521497533935
         * cfProductTitle : 卫龙辣条，小时候的味道
         * cfProductUrl : https://detail.tmall.com/item.htm?id=521497533935
         * cfShareCopy : 第一次旗舰店活动！
         超市一包7.8元，咱5包【券后19.9元】包邮
         * cfType : 0
         * detailImages : ["http://img.taoyouji666.com/71E02DD80F8540346A452BE3350E9443?imageView2/2/w/480/q/90"]
         * id : b8tl
         */

        public String cfComments;
        public double cfCouponAfterPrice;
        public String cfCouponUrl;
        public long cfCreateTime;
        public int cfMasterId;
        public String cfMasterImg;
        public String cfMasterName;
        public double cfPrice;
        public String cfProductId;
        public String cfProductTitle;
        public String cfProductUrl;
        public String cfShareCopy;
        public String cfType;
        public String id;
        public List<String> detailImages;
    }
}
