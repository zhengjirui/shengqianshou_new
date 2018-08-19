package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date 2017/10/10
 * @E-mail 1422947831@qq.com
 * @desc 我的代理实体类
 */
public class OwnMyAgentBean {

    /**
     * data : {"record":{"agencyMoney":"798.04","list":[{"contribution":"0.00","joinTime":"2017-09-05 10:38:35.0","nextAgentCount":5,"nickname":"用户11","ownIncome":"76.22","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1131320284&width=160&height=160&type=sns","userId":23},{"contribution":"0.00","joinTime":"2017-09-05 10:38:38.0","nextAgentCount":5,"ownIncome":"3.10","photo":"http://img.taoyouji666.com/D416FFE07A73DEF74492B018B0E2B9B2?imageView2/2/w/480/q/90","userId":22},{"contribution":"0.00","joinTime":"2017-09-04 15:25:46.0","nextAgentCount":5,"nickname":"维生素","ownIncome":"244.87","photo":"http://img.taoyouji666.com/B0AD1B66DAF90BD9840AACEA0E0D8C22?imageView2/2/w/480/q/90","userId":20},{"contribution":"0.00","joinTime":"2017-09-05 10:38:46.0","nextAgentCount":5,"ownIncome":"13.00","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=3008254949&width=160&height=160&type=sns","userId":17},{"contribution":"0.00","joinTime":"2017-09-01 10:35:43.0","nextAgentCount":5,"nickname":"1234","ownIncome":"1539.49","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90","userId":16}],"nickname":"给你们地","photo":"http://img.taoyouji666.com/1EB83B563314B2E8E6FDD91E1F175919?imageView2/2/w/150/q/90","returnMoney":"92.24","sumIncome":"-4499109.72","withdrawMoney":"4500000.00"}}
     * error : Success
     * errorCode : 200
     */
    /**
     * record : {"agencyMoney":"798.04","list":[{"contribution":"0.00","joinTime":"2017-09-05 10:38:35.0","nextAgentCount":5,"nickname":"用户11","ownIncome":"76.22","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1131320284&width=160&height=160&type=sns","userId":23},{"contribution":"0.00","joinTime":"2017-09-05 10:38:38.0","nextAgentCount":5,"ownIncome":"3.10","photo":"http://img.taoyouji666.com/D416FFE07A73DEF74492B018B0E2B9B2?imageView2/2/w/480/q/90","userId":22},{"contribution":"0.00","joinTime":"2017-09-04 15:25:46.0","nextAgentCount":5,"nickname":"维生素","ownIncome":"244.87","photo":"http://img.taoyouji666.com/B0AD1B66DAF90BD9840AACEA0E0D8C22?imageView2/2/w/480/q/90","userId":20},{"contribution":"0.00","joinTime":"2017-09-05 10:38:46.0","nextAgentCount":5,"ownIncome":"13.00","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=3008254949&width=160&height=160&type=sns","userId":17},{"contribution":"0.00","joinTime":"2017-09-01 10:35:43.0","nextAgentCount":5,"nickname":"1234","ownIncome":"1539.49","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90","userId":16}],"nickname":"给你们地","photo":"http://img.taoyouji666.com/1EB83B563314B2E8E6FDD91E1F175919?imageView2/2/w/150/q/90","returnMoney":"92.24","sumIncome":"-4499109.72","withdrawMoney":"4500000.00"}
     */

    public RecordBean record;

    public static class RecordBean {
        /**
         * agencyMoney : 798.04
         * list : [{"contribution":"0.00","joinTime":"2017-09-05 10:38:35.0","nextAgentCount":5,"nickname":"用户11","ownIncome":"76.22","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1131320284&width=160&height=160&type=sns","userId":23},{"contribution":"0.00","joinTime":"2017-09-05 10:38:38.0","nextAgentCount":5,"ownIncome":"3.10","photo":"http://img.taoyouji666.com/D416FFE07A73DEF74492B018B0E2B9B2?imageView2/2/w/480/q/90","userId":22},{"contribution":"0.00","joinTime":"2017-09-04 15:25:46.0","nextAgentCount":5,"nickname":"维生素","ownIncome":"244.87","photo":"http://img.taoyouji666.com/B0AD1B66DAF90BD9840AACEA0E0D8C22?imageView2/2/w/480/q/90","userId":20},{"contribution":"0.00","joinTime":"2017-09-05 10:38:46.0","nextAgentCount":5,"ownIncome":"13.00","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=3008254949&width=160&height=160&type=sns","userId":17},{"contribution":"0.00","joinTime":"2017-09-01 10:35:43.0","nextAgentCount":5,"nickname":"1234","ownIncome":"1539.49","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90","userId":16}]
         * nickname : 给你们地
         * photo : http://img.taoyouji666.com/1EB83B563314B2E8E6FDD91E1F175919?imageView2/2/w/150/q/90
         * returnMoney : 92.24
         * sumIncome : -4499109.72
         * withdrawMoney : 4500000.00
         */

        public String agencyMoney;
        public String nickname;
        public String photo;
        public String returnMoney;
        public String sumIncome;
        public String withdrawMoney;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * contribution : 0.00
             * joinTime : 2017-09-05 10:38:35.0
             * nextAgentCount : 5
             * nickname : 用户11
             * ownIncome : 76.22
             * photo : http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1131320284&width=160&height=160&type=sns
             * userId : 23
             */

            public String contribution;
            public String joinTime;
            public int nextAgentCount;
            public String nickname;
            public String ownIncome;
            public String photo;
            public int userId;
            public String phone;
        }
    }
}
