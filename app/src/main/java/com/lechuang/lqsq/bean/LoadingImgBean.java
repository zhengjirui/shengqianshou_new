package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/13 18:19
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class LoadingImgBean {


    /**
     * data : {"list":[{"id":"b8r5","orderNum":1,"startImage":"http://img.taoyouji666.com/f8f308b8c86604bdc8ae03a8ed0db182?imageView2/2/w/480/q/90","startImg":"qn|taoyouji2|f8f308b8c86604bdc8ae03a8ed0db182","type":5},{"id":"mhaq","orderNum":2,"startImage":"http://img.taoyouji666.com/1deca2d05307f5896b8cc2a0dd254c22?imageView2/2/w/480/q/90","startImg":"qn|taoyouji2|1deca2d05307f5896b8cc2a0dd254c22","type":5},{"id":"xpub","orderNum":3,"startImage":"http://img.taoyouji666.com/bf2ab1137409c94f4350e05ec1b0e529?imageView2/2/w/480/q/90","startImg":"qn|taoyouji2|bf2ab1137409c94f4350e05ec1b0e529","type":5},{"id":"18ydw","orderNum":4,"startImage":"http://img.taoyouji666.com/f042c46ba7247e5ef414432dca855be5?imageView2/2/w/480/q/90","startImg":"qn|taoyouji2|f042c46ba7247e5ef414432dca855be5","type":5}]}
     * error : Success
     * errorCode : 200
     */
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : b8r5
             * orderNum : 1
             * startImage : http://img.taoyouji666.com/f8f308b8c86604bdc8ae03a8ed0db182?imageView2/2/w/480/q/90
             * startImg : qn|taoyouji2|f8f308b8c86604bdc8ae03a8ed0db182
             * type : 5
             */

            public String id;
            public int orderNum;
            public String startImage;
            public String startImg;
            public int type;
        }
}
