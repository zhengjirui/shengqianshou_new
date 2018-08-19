package com.lechuang.lqsq.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengjr
 * @since: 2018/6/28
 * @describe:
 */

public class ShouJiBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * cfComments : 12312123
         * cfCreateTime : 1528838700000
         * cfMasterId : 2
         * cfMasterImg : http://img.taoyouji666.com/2D5AA2C20FF7BFED9F47766DA7405EAE?imageView2/2/w/480/q/90
         * cfMasterName : 橄榄枝@
         * cfNumber : 1168058878
         * cfProductId : 546937091246
         * cfProductTitle : 121212
         * cfShareCopy : 12123123
         * cfState : 1
         * cfType : 1
         * id : xpwj
         * product : [{"cfCouponAfterPrice":1,"cfCouponUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfCreateTime":1528838700000,"cfMasterId":2,"cfNumber":1168058878,"cfPrice":1,"cfProductId":"546937091246","cfProductTitle":"121212","cfProductUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfShareCopy":"1231","id":"xpwj","imgUrl":"http://img.taoyouji666.com/6880b3f5cd38896b619cb35de5c0d478?imageView2/2/w/480/q/90"},{"cfCouponAfterPrice":1231,"cfCouponUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfMasterId":0,"cfNumber":1168058878,"cfPrice":123123,"cfProductId":"546937091246","cfProductTitle":"123123","cfProductUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfShareCopy":"123123","id":"18yg4","imgUrl":"http://img.taoyouji666.com/6880b3f5cd38896b619cb35de5c0d478?imageView2/2/w/480/q/90"},{"cfCouponAfterPrice":123,"cfCouponUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfMasterId":0,"cfNumber":1168058878,"cfPrice":312,"cfProductId":"546937091246","cfProductTitle":"123123","cfProductUrl":"https://detail.tmall.com/item.htm?id=546937091246","cfShareCopy":"12312","id":"1vfja","imgUrl":"http://img.taoyouji666.com/6880b3f5cd38896b619cb35de5c0d478?imageView2/2/w/480/q/90"}]
         * cfCouponAfterPrice : 1.0
         * cfCouponUrl : https://detail.tmall.com/item.htm?id=546937091246
         * cfPrice : 12.0
         * cfProductUrl : https://detail.tmall.com/item.htm?id=546937091246
         * detailImages : ["http://img.taoyouji666.com/16a7c57b9aebc9a95f9914a71308ae3d?imageView2/2/w/480/q/90"]
         */

        public String cfComments;
        public long cfCreateTime;
        public int cfMasterId;
        public String cfMasterImg;
        public String cfMasterName;
        public int cfNumber;
        public String cfProductId;
        public String cfProductTitle;
        public String cfShareCopy;
        public String cfState;
        public String cfType;
        public String id;
        public double cfCouponAfterPrice;
        public String cfCouponUrl;
        public double cfPrice;
        public String cfProductUrl;
        public List<ProductBean> product;
        public ArrayList<String> detailImages;

        public static class ProductBean {
            /**
             * cfCouponAfterPrice : 1.0
             * cfCouponUrl : https://detail.tmall.com/item.htm?id=546937091246
             * cfCreateTime : 1528838700000
             * cfMasterId : 2
             * cfNumber : 1168058878
             * cfPrice : 1.0
             * cfProductId : 546937091246
             * cfProductTitle : 121212
             * cfProductUrl : https://detail.tmall.com/item.htm?id=546937091246
             * cfShareCopy : 1231
             * id : xpwj
             * imgUrl : http://img.taoyouji666.com/6880b3f5cd38896b619cb35de5c0d478?imageView2/2/w/480/q/90
             */

            public double cfCouponAfterPrice;
            public String cfCouponUrl;
            public long cfCreateTime;
            public int cfMasterId;
            public int cfNumber;
            public double cfPrice;
            public String cfProductId;
            public String cfProductTitle;
            public String cfProductUrl;
            public String cfShareCopy;
            public String id;
            public String imgUrl;
        }
    }

    public static class ListImageInfo{
        public ArrayList<String> detailImages;
        public String cfPrice;
    }
}
