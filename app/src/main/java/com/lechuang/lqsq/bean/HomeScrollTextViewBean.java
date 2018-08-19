package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date   2017/10/2
 * @E-mail 1422947831@qq.com
 * @desc   首页滚动文字实体类
 */

public class HomeScrollTextViewBean {
    /* "data":
     {
         "indexMsgList":[
         {
             "appraiseCount":0,
                 "content":"asdasdsaaaaaaaaaaaaa",
                 "createTime":1506167118000,
                 "createTimeStr":"2017-09-23 19:45:18",
                 "id":"b8r5",
                 "img":
             "http://img.taoyouji666.com/b90772ee0024261c36487a299240b7a1?imageView2/2/w/480/q/90",
                     "img1":[

                 ],
             "nickName":"lalala",
                 "pageViews":7,
                 "photo":
             "http://img.taoyouji666.com/83516695047f4a5cf5040a85bb641d6f?imageView2/2/w/480/q/90",
                     "praiseCount":0,
                 "productId":4,
                 "title":"langligelang"
         }
         ]
     },
     "error":"Success",
     "errorCode":200*/

    public List<ListBean> indexMsgList;

    public static class ListBean {
        /**
         * "appraiseCount":0,
         * "content":"asdasdsaaaaaaaaaaaaa",
         * "createTime":1506167118000,
         * "createTimeStr":"2017-09-23 19:45:18",
         * "id":"b8r5",
         * "img":"http://img.taoyouji666.com/b90772ee0024261c36487a299240b7a1?imageView2/2/w/480/q/90",
         * "img1":[
         * ],
         * "nickName":"lalala",
         * "pageViews":7,
         * "photo":"http://img.taoyouji666.com/83516695047f4a5cf5040a85bb641d6f?imageView2/2/w/480/q/90",
         * "praiseCount":0,
         * "productId":4,
         * "title":"langligelang"
         */

        public int appraiseCount;
        public String content;
        public long createTime;
        public String createTimeStr;
        public String id;
        public String img;
        public String nickName;
        public int pageViews;
        public String photo;
        public int praiseCount;
        public int productId;
        public String title;
        public List<?> img1;

    }

}
