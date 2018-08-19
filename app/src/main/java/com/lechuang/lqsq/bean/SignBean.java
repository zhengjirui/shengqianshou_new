package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/8 13:56
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class SignBean {

    /**
     * data : {"continuousSigned":0,"list":[{"continuousSigned":2,"id":"mhaa","rewardIntegral":1,"signStartIntegral":1},{"continuousSigned":5,"id":"xptv","rewardIntegral":5,"signStartIntegral":1},{"continuousSigned":3,"id":"18ydg","rewardIntegral":3,"signStartIntegral":1},{"continuousSigned":4,"id":"1k6x1","rewardIntegral":4,"signStartIntegral":1},{"continuousSigned":6,"id":"1vfgm","rewardIntegral":6,"signStartIntegral":1},{"continuousSigned":7,"id":"26o07","rewardIntegral":7,"signStartIntegral":1},{"continuousSigned":1,"signStartIntegral":1}],"signStatus":0}
     * error : Success
     * errorCode : 200
     */

        /**
         * continuousSigned : 0
         * list : [{"continuousSigned":2,"id":"mhaa","rewardIntegral":1,"signStartIntegral":1},{"continuousSigned":5,"id":"xptv","rewardIntegral":5,"signStartIntegral":1},{"continuousSigned":3,"id":"18ydg","rewardIntegral":3,"signStartIntegral":1},{"continuousSigned":4,"id":"1k6x1","rewardIntegral":4,"signStartIntegral":1},{"continuousSigned":6,"id":"1vfgm","rewardIntegral":6,"signStartIntegral":1},{"continuousSigned":7,"id":"26o07","rewardIntegral":7,"signStartIntegral":1},{"continuousSigned":1,"signStartIntegral":1}]
         * signStatus : 0
         */

        public int continuousSigned;
        public int signStatus;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * continuousSigned : 2
             * id : mhaa
             * rewardIntegral : 1
             * signStartIntegral : 1
             */

            public int continuousSigned;
            public String id;
            public int rewardIntegral;
            public int signStartIntegral;
        }
    }
