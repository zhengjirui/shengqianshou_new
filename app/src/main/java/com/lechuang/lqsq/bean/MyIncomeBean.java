package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date   2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc   我的收益实体类
 */
public class MyIncomeBean {

        /**
         * data : {"record":{"currentMonthIncome":"8.00","estimatedIncome":"0.00","list":[{"createTime":1507598052000,"createTimeStr":"2017-10-10 09:14:12","id":"1vlvi","integralDetails":200,"integralDetailsStr":"2.00","type":0,"typeId":303,"typeStr":"签到收益","userId":135},{"createTime":1507552009000,"createTimeStr":"2017-10-09 20:26:49","id":"1kdbx","integralDetails":500,"integralDetailsStr":"5.00","type":10,"typeStr":"完善昵称返积分","userId":135},{"createTime":1507512650000,"createTimeStr":"2017-10-09 09:30:50","id":"xw8j","integralDetails":100,"integralDetailsStr":"1.00","type":0,"typeId":294,"typeStr":"签到收益","userId":135},{"createTime":1505874987000,"createTimeStr":"2017-09-20 10:36:27","id":"mkkq","integralDetails":5000,"integralDetailsStr":"50.00","type":0,"typeId":234,"typeStr":"签到收益","userId":135}],"rewardIncome":"3.00","totalIncome":"50.00","withdrawIntegral":"0.00"}}
         * error : Success
         * errorCode : 200
         */
            /**
             * record : {"currentMonthIncome":"8.00","estimatedIncome":"0.00","list":[{"createTime":1507598052000,"createTimeStr":"2017-10-10 09:14:12","id":"1vlvi","integralDetails":200,"integralDetailsStr":"2.00","type":0,"typeId":303,"typeStr":"签到收益","userId":135},{"createTime":1507552009000,"createTimeStr":"2017-10-09 20:26:49","id":"1kdbx","integralDetails":500,"integralDetailsStr":"5.00","type":10,"typeStr":"完善昵称返积分","userId":135},{"createTime":1507512650000,"createTimeStr":"2017-10-09 09:30:50","id":"xw8j","integralDetails":100,"integralDetailsStr":"1.00","type":0,"typeId":294,"typeStr":"签到收益","userId":135},{"createTime":1505874987000,"createTimeStr":"2017-09-20 10:36:27","id":"mkkq","integralDetails":5000,"integralDetailsStr":"50.00","type":0,"typeId":234,"typeStr":"签到收益","userId":135}],"rewardIncome":"3.00","totalIncome":"50.00","withdrawIntegral":"0.00"}
             */

            public RecordBean record;

            public static class RecordBean {
                /**
                 * currentMonthIncome : 8.00
                 * estimatedIncome : 0.00
                 * list : [{"createTime":1507598052000,"createTimeStr":"2017-10-10 09:14:12","id":"1vlvi","integralDetails":200,"integralDetailsStr":"2.00","type":0,"typeId":303,"typeStr":"签到收益","userId":135},{"createTime":1507552009000,"createTimeStr":"2017-10-09 20:26:49","id":"1kdbx","integralDetails":500,"integralDetailsStr":"5.00","type":10,"typeStr":"完善昵称返积分","userId":135},{"createTime":1507512650000,"createTimeStr":"2017-10-09 09:30:50","id":"xw8j","integralDetails":100,"integralDetailsStr":"1.00","type":0,"typeId":294,"typeStr":"签到收益","userId":135},{"createTime":1505874987000,"createTimeStr":"2017-09-20 10:36:27","id":"mkkq","integralDetails":5000,"integralDetailsStr":"50.00","type":0,"typeId":234,"typeStr":"签到收益","userId":135}]
                 * rewardIncome : 3.00
                 * totalIncome : 50.00
                 * withdrawIntegral : 0.00
                 */
                //当月总结算收入
                public String currentMonthIncome;
                //总预估收入
                public String estimatedIncome;
                //奖励收入
                public String rewardIncome;
                //总结算收入
                public String totalIncome;
                //可提现积分
                public String withdrawIntegral;
                //积分明细列表数据
                public List<ListBean> list;

                //积分明细列表数据
                public static class ListBean {
                    /**
                     * createTime : 1507598052000
                     * createTimeStr : 2017-10-10 09:14:12
                     * id : 1vlvi
                     * integralDetails : 200
                     * integralDetailsStr : 2.00
                     * type : 0
                     * typeId : 303
                     * typeStr : 签到收益
                     * userId : 135
                     */

                    public long createTime;
                    //时间
                    public String createTimeStr;
                    public String id;
                    public int integralDetails;
                    //积分明细
                    public String integralDetailsStr;
                    public int type;
                    public int typeId;
                    //收益中文描述
                    public String typeStr;
                    public int userId;
                }
            }



}
