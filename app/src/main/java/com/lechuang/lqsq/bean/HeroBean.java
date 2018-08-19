package com.lechuang.lqsq.bean;

import java.util.List;

/**
 * 作者：li on 2017/10/24 10:45
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class HeroBean {


    /**
     * data : {"list":[{"nickName":"心赠予你","photo":"http://img.taoyouji666.com/91C5B47F513CC06CD895A94E26A1CDD2?imageView2/2/w/150/q/90","sumIntegral":32713,"sumIntegralStr":"327.13","userId":140},{"nickName":"语不惊人死不休","photo":"http://img.taoyouji666.com/FE97CEB6AFFB37FFE5E7E0FD0B3778A2?imageView2/2/w/150/q/90","sumIntegral":23199,"sumIntegralStr":"231.99","userId":159},{"nickName":"1234","photo":"http://img.taoyouji666.com/D0088BC146842BFF8E066C23842CE522?imageView2/2/w/150/q/90","sumIntegral":5696,"sumIntegralStr":"56.96","userId":16},{"photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1048397817&width=160&height=160&type=sns","sumIntegral":4800,"sumIntegralStr":"48.00","userId":38},{"nickName":"给你们地","photo":"http://img.taoyouji666.com/7A505BC8C904AA084CFA1508F7BA063C?imageView2/2/w/150/q/90","sumIntegral":3994,"sumIntegralStr":"39.94","userId":133},{"nickName":"987654","photo":"http://img.taoyouji666.com/CFF0B6538666BB29209D5D7162871711?imageView2/2/w/150/q/90","sumIntegral":2800,"sumIntegralStr":"28.00","userId":201},{"nickName":"西门吹雪","photo":"http://img.taoyouji666.com/EAA29659138C822C51C8C70019CAB0E1?imageView2/2/w/150/q/90","sumIntegral":1300,"sumIntegralStr":"13.00","userId":189},{"nickName":"篱笆528","photo":"http://img.taoyouji666.com/A360BB3715A0387B6F25B67063B3AAA7?imageView2/2/w/150/q/90","sumIntegral":1100,"sumIntegralStr":"11.00","userId":190},{"nickName":"王家福二号","photo":"http://img.taoyouji666.com/48f3acc16d3fff7402283944f7f4d313?imageView2/2/w/150/q/90","sumIntegral":940,"sumIntegralStr":"9.40","userId":3},{"nickName":"用户11","photo":"http://115.236.11.98:8082/_f/https://wwc.alicdn.com/avatar/getAvatar.do?userId=1131320284&width=160&height=160&type=sns","sumIntegral":800,"sumIntegralStr":"8.00","userId":23}]}
     * error : Success
     * errorCode : 200
     */

    public List<ListBean> list;

    public static class ListBean {
        /**
         * nickName : 心赠予你
         * photo : http://img.taoyouji666.com/91C5B47F513CC06CD895A94E26A1CDD2?imageView2/2/w/150/q/90
         * sumIntegral : 32713
         * sumIntegralStr : 327.13
         * userId : 140
         */

        public String nickName;
        public String photo;
        public String phone;
        public String sumIntegral;
        public String sumIntegralStr;
        public int userId;
    }
}
