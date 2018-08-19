package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * Author: guoning
 * Date: 2017/11/20
 * Description:
 */

public class TeamBean {

    public MineTeamBean record;

    public class MineTeamBean {

        public List<TeamSubBean> list;

        public class TeamSubBean {
            /**
             * 下级成员近3月贡献
             */
            public String contribution3;
            /**
             * 是否是代理
             */
            public String isAgencyStatus;
            /**
             * 成员数量
             */
            public String nextAgentCount;
            /**
             * 成员名称
             */
            public String nickname;
            /**
             * 头像 地址
             */
            public String photo;
            /**
             * 用户id(加密后)
             */
            public String userIdStr;
            public String inviteRewards;
        }

        /**
         * 一级成员数量(注册成功)
         */
        public String members1;
        /**
         * 二级成员数量
         */
        public String members2;
        /**
         * 未激活成员数量(未注册成功)
         */
        public String members3;
        /**
         * 一级成员数量(包含注册成功和未成功的)
         */
        public String nextAgentCount;
        /**
         * 用户名称
         */
        public String nickname;
        /**
         * 用户头像地址
         */
        public String photo;
        public String sumContribution3;
    }
}
