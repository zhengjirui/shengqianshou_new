package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/9/29 20:21
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class SunShowBean {
        public List<BannerListBean> bannerList;
        public List<ListBean> list;

        public static class BannerListBean {
            /**
             * id : 80
             * img : http://img.taoyouji666.com/496ab6d735ebd69dce0d181c52f38ac7
             * type : 2
             */

            public String id;
            public String img;
            public int type;
        }

        public static class ListBean {
            /**
             * alipayNumber :
             * appraiseCount : 4
             * content : 好刺激好聚聚聚餐
             * createTime : 1505481347000
             * createTimeStr : 2017-09-15 21:15:47
             * id : mhc2
             * img : qn|taoyouji2|A8CD9F1839C255859E6A8DEB4B05DBDD,qn|taoyouji2|C41A1564E73525E98830C80D0B3FE897
             * img1 : [{"imgUrl":"http://img.taoyouji666.com/A8CD9F1839C255859E6A8DEB4B05DBDD?imageView2/2/w/480/q/90"},{"imgUrl":"http://img.taoyouji666.com/C41A1564E73525E98830C80D0B3FE897?imageView2/2/w/480/q/90"}]
             * nickName : 18248434899
             * orderNumber : 51707177822555615
             * phone : 18248434899
             * photo : http://img.taoyouji666.com/4B4EBA71645AE10C3CE0E3DBB761CD48?imageView2/2/w/480/q/90
             * praiseCount : 1
             * shareCount : 0
             * starLevel : 5
             * status : 0
             * taobaoNumber : 龚明坤gongmingkun
             * userId : 1
             */

            public String alipayNumber;
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
            public String taobaoNumber;
            public int userId;
            public List<Img1Bean> img1;

            public static class Img1Bean {
                /**
                 * imgUrl : http://img.taoyouji666.com/A8CD9F1839C255859E6A8DEB4B05DBDD?imageView2/2/w/480/q/90
                 */

                public String imgUrl;
            }
        }
}
