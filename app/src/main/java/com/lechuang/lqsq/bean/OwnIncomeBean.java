package com.lechuang.lqsq.bean;

/**
 * Author: LGH
 * Date: 2017/11/18
 * Description: 我的收益
 */

public class OwnIncomeBean {

    public IncomeBean record;

    public class IncomeBean {
        /**
         * 代理贡献收入
         */
        public String agencyIncome;
        /**
         * 代理贡献量
         */
        public String agencyIncomeNum;
        /**
         * 本月总结算预估收入
         */
        public String currentMonthIncome;
        /**
         * 自身贡献收入
         */
        public String ownIncome;
        /**
         * 自身贡献量
         */
        public String ownIncomeNum;
        /**
         * 今日预估收入
         */
        public String todayEstimatedIncome;
        /**
         * 今日成交量
         */
        public String todayVolum;
        /**
         * 上月总结算预估收入
         */
        public String totalIncome;
        /**
         * 总积分
         */
        public String sumIntegral;
        /**
         * 可提现积分
         */
        public String withdrawalIntegral;
        /**
         * 不可提现积分
         */
        public String notWithdrawalIntegral;
        public String inviteRewards;
        public String newTeamNum;

    }
}
