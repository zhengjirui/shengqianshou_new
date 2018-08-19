package com.lechuang.lqsq.bean;

/**
 * @author yrj
 * @date 2017/10/2
 * @E-mail 1422947831@qq.com
 * @desc 首页分类实体类
 */
public class HomeKindItemBean {

    /**
     * "data":{
     * "list":[
     * {
     * "id":"26o07",
     * "img":"http://img.taoyouji666.com/a239310d597d68ab7b3da7c0d4480f4d?imageView2/2/w/480/q/90",
     * "name":"女鞋",
     * "parentId":7,
     * "rootId":7,
     * "rootName":"鞋包服饰"
     * },
     * {
     * "id":"7c",
     * "img":"http://img.taoyouji666.com/c00ab8bdab42c6db602446957c1825e3?imageView2/2/w/480/q/90",
     * "name":"零食坚果特产",
     * "parentId":8,
     * "rootId":8,
     * "rootName":"美食"
     * },
     * {
     * "id":"b8qx",
     * "img":"http://img.taoyouji666.com/b3711ff174bcaca14a8b57a76e3e5db5?imageView2/2/w/480/q/90",
     * "name":"汽车用品配件改装",
     * "parentId":9,
     * "rootId":9,
     * "rootName":"文体车品"
     * },
     * {
     * "id":"xpu3",
     * "img":"http://img.taoyouji666.com/7f01c2b4d27af5611fc98bf0e54de62d?imageView2/2/w/480/q/90",
     * "name":"电玩配件游戏攻略",
     * "parentId":11,
     * "rootId":11,
     * "rootName":"其他"
     * }
     * ]
     * },
     * "error":"Success",
     * "errorCode":200
     */

    /*public List<ListBean> tbclassTypeList;

    public static class ListBean {*/
    public String id;
    //图片
    public int img;
    //淘宝类目名
    public String name;
    //淘宝父类目ID
    public int parentId;
    //网站根目录
    public int rootId;
    //网站根目录
    public String rootName;
    // }

}
