package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * Author: guoning
 * Date: 2017/11/21
 * Description:
 */

public class TeamNextBean {

    public TeamNext record;

    public class TeamNext {

        public List<TeamMember> list;

        public class TeamMember {
            /**
             * 近3月贡献
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
             * 用户id
             */
            public String userId;
            public String joinTime;
        }

        /**
         * 贡献总和
         */
        public String contributionSum;
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
    }
}
