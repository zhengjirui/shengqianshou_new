package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/7 20:12
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class SunDetailListBean {

    /**
     * data : {"appraiseList":[{"alipayNumber":"(๑\u2022ั็ω\u2022็ั๑)","content":"呦西","counts":2,"createTime":1507106440000,"createTimeStr":"2017-10-04 16:40:40","id":"1ka2t","nickName":"1234","phone":"18737024370","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90","praiseCount":0,"status":0,"taobaoNumber":"hehe5241","tipOffId":65,"type":2,"userId":16},{"content":"哈哈","createTime":1505562500000,"createTimeStr":"2017-09-16 19:48:20","id":"b8w1","nickName":"13714173082","phone":"13714173082","photo":"http://img.taoyouji666.com/6CA9AD4233FDED593ADE9AB4BBFB54C1?imageView2/2/w/480/q/90","praiseCount":1,"status":0,"tipOffId":65,"type":2,"userId":98}],"counts":2}
     * error : Success
     * errorCode : 200
     */

        /**
         * appraiseList : [{"alipayNumber":"(๑\u2022ั็ω\u2022็ั๑)","content":"呦西","counts":2,"createTime":1507106440000,"createTimeStr":"2017-10-04 16:40:40","id":"1ka2t","nickName":"1234","phone":"18737024370","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90","praiseCount":0,"status":0,"taobaoNumber":"hehe5241","tipOffId":65,"type":2,"userId":16},{"content":"哈哈","createTime":1505562500000,"createTimeStr":"2017-09-16 19:48:20","id":"b8w1","nickName":"13714173082","phone":"13714173082","photo":"http://img.taoyouji666.com/6CA9AD4233FDED593ADE9AB4BBFB54C1?imageView2/2/w/480/q/90","praiseCount":1,"status":0,"tipOffId":65,"type":2,"userId":98}]
         * counts : 2
         */

        public int counts;
        public List<AppraiseListBean> appraiseList;

        public static class AppraiseListBean {
            /**
             * alipayNumber : (๑•ั็ω•็ั๑)
             * content : 呦西
             * counts : 2
             * createTime : 1507106440000
             * createTimeStr : 2017-10-04 16:40:40
             * id : 1ka2t
             * nickName : 1234
             * phone : 18737024370
             * photo : http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/480/q/90
             * praiseCount : 0
             * status : 0
             * taobaoNumber : hehe5241
             * tipOffId : 65
             * type : 2
             * userId : 16
             */

            public String alipayNumber;
            public String content;
            public int counts;
            public long createTime;
            public String createTimeStr;
            public String id;
            public String nickName;
            public String phone;
            public String photo;
            public int praiseCount;
            public int status;
            public String taobaoNumber;
            public int tipOffId;
            public int type;
            public int userId;
        }
}
