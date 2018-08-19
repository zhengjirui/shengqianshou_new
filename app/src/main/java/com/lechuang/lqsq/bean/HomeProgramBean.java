package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date   2017/10/2
 * @E-mail 1422947831@qq.com
 * @desc   首页图片栏目数据
 */

public class HomeProgramBean {

    /* "data":{
         "programaImgList":[
         {
             "id":"xpub",
                 "img":"http://img.taoyouji666.com/1127651a29e9bd7bfc2f2e8adff78603",
                 "programaId":3,
                 "type":5
         },
         {
             "id":"b8rt",
                 "img":"http://img.taoyouji666.com/b255be1495692537f9dd943ef4b2b5a5",
                 "link":"http://www.baidu.com",
                 "type":7
         }
         ]
     },
     "error":"Success",
     "errorCode":200*/

    public List<ListBean> programaImgList;

    public static class ListBean {
        /**
         * "id":"b8rt",
         * "img":"http://img.taoyouji666.com/b255be1495692537f9dd943ef4b2b5a5",
         * "link":"http://www.baidu.com",
         * "type":7
         */

        public String id;
        public String img;
        public String link;
        public int type;
        public int programaId;
        public String pname;

    }

}
