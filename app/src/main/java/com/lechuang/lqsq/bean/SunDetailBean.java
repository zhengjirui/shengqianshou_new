package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/7 19:17
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class SunDetailBean {

    /**
     * data : {"baskOrder":{"appraiseCount":0,"content":"258","createTime":1507371821000,"createTimeStr":"2017-10-07 18:23:41","id":"1k6zh","img":"qn|taoyouji2|824AF07ABDEE6C9F5BF63F2E3A745E52","img1":[{"imgUrl":"http://img.taoyouji666.com/824AF07ABDEE6C9F5BF63F2E3A745E52?imageView2/2/w/480/q/90"}],"nickName":"123456789","orderNumber":"50725164841395000","phone":"13569419956","photo":"http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90","praiseCount":0,"shareCount":0,"starLevel":5,"status":0,"title":"2送1【关家庄园辣么烧约38个】怀旧辣条 麻辣面筋制品 大刀肉零食","userId":140},"status":1}
     * error : Success
     * errorCode : 200
     */


        /**
         * baskOrder : {"appraiseCount":0,"content":"258","createTime":1507371821000,"createTimeStr":"2017-10-07 18:23:41","id":"1k6zh","img":"qn|taoyouji2|824AF07ABDEE6C9F5BF63F2E3A745E52","img1":[{"imgUrl":"http://img.taoyouji666.com/824AF07ABDEE6C9F5BF63F2E3A745E52?imageView2/2/w/480/q/90"}],"nickName":"123456789","orderNumber":"50725164841395000","phone":"13569419956","photo":"http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90","praiseCount":0,"shareCount":0,"starLevel":5,"status":0,"title":"2送1【关家庄园辣么烧约38个】怀旧辣条 麻辣面筋制品 大刀肉零食","userId":140}
         * status : 1
         */

        public BaskOrderBean baskOrder;
        public int status;

        public static class BaskOrderBean {
            /**
             * appraiseCount : 0
             * content : 258
             * createTime : 1507371821000
             * createTimeStr : 2017-10-07 18:23:41
             * id : 1k6zh
             * img : qn|taoyouji2|824AF07ABDEE6C9F5BF63F2E3A745E52
             * img1 : [{"imgUrl":"http://img.taoyouji666.com/824AF07ABDEE6C9F5BF63F2E3A745E52?imageView2/2/w/480/q/90"}]
             * nickName : 123456789
             * orderNumber : 50725164841395000
             * phone : 13569419956
             * photo : http://img.taoyouji666.com/B23D6709379DE3F412D4C2FDA8F22B52?imageView2/2/w/480/q/90
             * praiseCount : 0
             * shareCount : 0
             * starLevel : 5
             * status : 0
             * title : 2送1【关家庄园辣么烧约38个】怀旧辣条 麻辣面筋制品 大刀肉零食
             * userId : 140
             */

            public int appraiseCount;
            public String content;
            public long createTime;
            public String createTimeStr;
            public String id;
            public String img;
            public String nickName;
            public String orderNumber;
            public String phone;
            public String photo;
            public int praiseCount;
            public int shareCount;
            public int starLevel;
            public int status;
            public String title;
            public int userId;
            public List<Img1Bean> img1;

            public static class Img1Bean {
                /**
                 * imgUrl : http://img.taoyouji666.com/824AF07ABDEE6C9F5BF63F2E3A745E52?imageView2/2/w/480/q/90
                 */

                public String imgUrl;
            }
        }
}
