package com.lechuang.lqsq.widget.views.refeshview;

/**
 * Created by 键盘 on 2016/1/11.
 * 日期工具类
 */
public class DateTools {
    public static String friendlyTime(long mLastTime) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - mLastTime)/1000);

        if(ct == 0) {
            return "刚刚";
        }

        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

}
