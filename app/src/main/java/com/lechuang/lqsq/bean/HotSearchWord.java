package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/04
 * 时间：09:22
 * 描述：
 */

public class HotSearchWord {
    public List<HotSearchWord.HswList> hswList;

    public static class HswList {
        public int number;
        public String id;
        public long insertDate;
        public String searchWord;
    }

}
