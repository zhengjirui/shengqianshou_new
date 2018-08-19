package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/11/13 14:57
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class SoufanliProgremBean {

    /**
     * data : {"searchImgList":[{"id":"xpyz","img":"http://img.taoyouji666.com/ssg_zj2.png","link":"","name":"官方推荐","programType":1,"type":55},{"id":"18yik","img":"http://img.taoyouji666.com/ssg_zj3.png","link":"","name":"9.9包邮","programType":2,"type":56},{"id":"1k725","img":"http://img.taoyouji666.com/ssg_zj4.png","link":"","name":"一元试用","programType":3,"type":57},{"id":"1vflq","img":"http://img.taoyouji666.com/ssg_zj1.png","link":"","name":"品牌优选","programType":4,"type":58}]}
     * error : Success
     * errorCode : 200
     */
        public List<SearchImgListBean> searchImgList;

        public static class SearchImgListBean {
            /**
             * id : xpyz
             * img : http://img.taoyouji666.com/ssg_zj2.png
             * link :
             * name : 官方推荐
             * programType : 1
             * type : 55
             */

            public String id;
            public String img;
            public String link;
            public String name;
            public int programType;
            public int type;
        }
}
