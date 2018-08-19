package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * Author: LGH
 * Date: 2017/11/18
 * Description: 订单明细
 */

public class OrderBean {

//    {
//        "orderList": [
//        {
//                "clearingPrice": "6.9",
//                "clearingTime": "2017-08-26 17:39:48",
//                "commisiom": "16.20%",
//                "createTime": "2017-08-24 00:45:16",
//                "forecastIncome": "2.79",
//                "goodsMsg": "SIMEITOL/姿美堂 维生素C咀嚼片（香橙味） 1.2g/片*60片",
//                "img": "http://img1.tbcdn.cn/tfscom/i4/2733412062/TB2EcfftgFkpuFjSspnXXb4qFXa_!!2733412062.jpg",
//                "incomeRate": "40.50 %",
//                "orderNum": "50647334861802700",
//                "orderStatus": "已结算",
//                "payClearingIncome": "1.12",
//                "payClearingMoney": "6.9",
//                "payPrice": "6.9",
//                "resultForecast": "2.79",
//                "shopName": "京姿美堂旗舰店",
//                "shopType": "1",
//                "source": "自购"
//        }
//      ]
//    }
    public String ownIncome;
    public String agencyIncome;
    public String nextAgencyIncome;
    public String sumIncome;

    public List<OrderDetail> orderList;

    public class OrderDetail {

        /**
         * 结算金额
         */
        public String clearingPrice;
        /**
         * 订单结算时间
         */
        public String clearingTime;
        /**
         * 提成
         */
        public String commisiom;
        /**
         * 创建时间
         */
        public String createTime;


        public String forecastIncome;
        /**
         * 商品标题
         */
        public String goodsMsg;


        public String incomeRate;
        /**
         * 订单号
         */
        public String orderNum;
        /**
         * 订单状态
         */
        public String orderStatus;
        /**
         * 结算收入 /成本预估收入
         */
        public String payClearingIncome;
        /**
         * 结算金额 /付款金额
         */
        public String payClearingMoney;


        public String payPrice;


        public String resultForecast;
        /**
         * 店铺名称
         */
        public String shopName;
        /**
         * 来源
         */
        public String source;
        public String sourceText;
        /**
         * 商品主图地址
         */
        public String img;
        /**
         * 1 淘宝 2天猫
         */
        public String shopType;

    }
}
