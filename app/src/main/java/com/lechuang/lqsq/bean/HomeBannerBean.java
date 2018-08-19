package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * @author yrj
 * @date   2017/10/2
 * @E-mail 1422947831@qq.com
 * @desc   首页轮播图实体类
 */

public class HomeBannerBean {
    /**
     * "data":{
     * "indexBannerList0":[
     * {
     * "id":"b8rd",
     * "img":"http://img.taoyouji666.com/564d1786c32d61b086db3a90638d7611",
     * "link":"www.baidu.com",
     * "type":3
     * },
     * {
     * "id":"xpuj",
     * "img":"http://img.taoyouji666.com/d6a953eb0f3c9f2b74c4308827907607",
     * "programaId":1,
     * "type":3
     * },
     * {
     * "id":"18ye4",
     * "img":"http://img.taoyouji666.com/b255be1495692537f9dd943ef4b2b5a5",
     * "programaId":2,
     * "type":3
     * },
     * {
     * "id":"1k6xp",
     * "img":"http://img.taoyouji666.com/10cf89cda9d2ebc7d4111489f63f1daa",
     * "link":"http://www.baidu.com",
     * "type":3
     * }
     * ]
     * },
     * "error":"Success",
     * "errorCode":200
     */


    public List<IndexBannerList> indexBannerList0;

    public static class IndexBannerList {
        //id
        public String id;
        //图片
        public String img;
        //链接
        public String link;
        public int type;
        //programaId  用来判断跳转到那个栏目
        public int programaId;
    }

}
