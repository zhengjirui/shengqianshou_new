package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/09
 * 时间：14:29
 * 描述：
 */

public class FenLeiBean {
    public List<ClassTypeList> classTypeList;

    public class ClassTypeList {
        public String img;
        public int rootId;
        public String rootName;
        public List<TBClassTypeList> tbClassTypeList;
    }

    public class TBClassTypeList {
        public String img;
        public String name;
        public int rootId;
        public String rootName;
    }
}
