package com.lechuang.lqsq.bean;

/**
 * Created by YRJ
 * Date 2018/3/22.
 */

public class EnvelopeActivityBean {
    public ActivityDesc activityDesc;

    public static class ActivityDesc {
        public String activeImage;
        public String activeImageUrl;
        /**
         * 活动时才有的地址
         */
        public String activeUrl;
        /**
         * 商品才有的商品ID
         */
        public String alipayItemId;
        /**
         * appuserid存在标识已经领取过这次随机红包
         */
        public String appuserId;
        public String id;
        public int number;
        /**
         * 类型。0关闭，1活动，2随机红包，3商品
         */
        public int status;
    }
}
