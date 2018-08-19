package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date 2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc 消息列表
 */

public class OwnNewsListBean {

    /**
     * data : {"list":[{"content":"恭喜您在淘宝购买的订单编号为 57707191015412637的商品成功，预估积分将稍后到账","createTime":1505211189000,"createTimeStr":"2017-09-12 18:13:09","haveIntegral":1,"id":"1k6y5","integralStatus":0,"orderNum":"57707191015412637","status":0,"userId":135},{"content":"恭喜您在淘宝购买的订单编号为 57707191015412637的商品成功，预估积分将稍后到账","createTime":1505211189000,"createTimeStr":"2017-09-12 18:13:09","haveIntegral":1,"id":"1vfhq","integralStatus":0,"orderNum":"57707191015412637","status":0,"userId":135},{"content":"恭喜您在淘宝购买的订单编号为 57707191015412637的商品成功，预估积分将稍后到账","createTime":1505211189000,"createTimeStr":"2017-09-12 18:13:09","haveIntegral":1,"id":"26o1b","integralStatus":0,"orderNum":"57707191015412637","status":0,"userId":135}]}
     * error : Success
     * errorCode : 200
     */
    public List<ListBean> list;

    public static class ListBean {
        /**
         * content : 恭喜您在淘宝购买的订单编号为 57707191015412637的商品成功，预估积分将稍后到账
         * createTime : 1505211189000
         * createTimeStr : 2017-09-12 18:13:09
         * haveIntegral : 1
         * id : 1k6y5
         * integralStatus : 0
         * orderNum : 57707191015412637
         * status : 0
         * userId : 135
         */

        public String content;
        public long createTime;
        public String createTimeStr;
        public int haveIntegral;
        public String id;
        public int integralStatus;
        public String orderNum;
        public int status;
        public int userId;
    }

}
