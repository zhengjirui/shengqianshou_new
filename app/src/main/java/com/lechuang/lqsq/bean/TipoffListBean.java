package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/7 10:38
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class TipoffListBean {


    /**
     * data : {"appraiseList":[{"content":"higig","counts":1,"createTime":1506849428000,"createTimeStr":"2017-10-01 17:17:08","id":"3cw","nickName":"123456789","phone":"13569419956","photo":"http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90","praiseCount":0,"status":0,"tipOffId":19,"type":1,"userId":140}],"counts":1}
     * error : Success
     * errorCode : 200
     */
        /**
         * appraiseList : [{"content":"higig","counts":1,"createTime":1506849428000,"createTimeStr":"2017-10-01 17:17:08","id":"3cw","nickName":"123456789","phone":"13569419956","photo":"http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90","praiseCount":0,"status":0,"tipOffId":19,"type":1,"userId":140}]
         * counts : 1
         */

        public int counts;
        public List<AppraiseListBean> appraiseList;

        public static class AppraiseListBean {
            /**
             * content : higig
             * counts : 1
             * createTime : 1506849428000
             * createTimeStr : 2017-10-01 17:17:08
             * id : 3cw
             * nickName : 123456789
             * phone : 13569419956
             * photo : http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90
             * praiseCount : 0
             * status : 0
             * tipOffId : 19
             * type : 1
             * userId : 140
             */

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
            public int tipOffId;
            public int type;
            public int userId;
        }
}
