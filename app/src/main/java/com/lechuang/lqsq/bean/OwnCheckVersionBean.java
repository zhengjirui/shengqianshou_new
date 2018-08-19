package com.lechuang.lqsq.bean;

/**
 * 作者：li on 2017/10/10 15:10
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class OwnCheckVersionBean {

    /**
     * data : {"app":{"appName":"喜乐淘","downloadUrl":"/app/downLoad","id":"b8qp","marketUrl":"http://download.tkcaiji.com/app/downLoad","qRCode":"qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e","type":1,"versionDescribe":"这是一个app","versionNumber":"1.0.0"},"maxApp":{"appName":"喜乐淘","downloadUrl":"/app/downLoad","id":"b8qp","marketUrl":"http://download.tkcaiji.com/app/downLoad","qRCode":"qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e","type":1,"versionDescribe":"这是一个app","versionNumber":"1.0.0"}}
     * error : Success
     * errorCode : 200
     */

        /**
         * app : {"appName":"喜乐淘","downloadUrl":"/app/downLoad","id":"b8qp","marketUrl":"http://download.tkcaiji.com/app/downLoad","qRCode":"qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e","type":1,"versionDescribe":"这是一个app","versionNumber":"1.0.0"}
         * maxApp : {"appName":"喜乐淘","downloadUrl":"/app/downLoad","id":"b8qp","marketUrl":"http://download.tkcaiji.com/app/downLoad","qRCode":"qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e","type":1,"versionDescribe":"这是一个app","versionNumber":"1.0.0"}
         */

        public AppBean app;
        public MaxAppBean maxApp;

        public static class AppBean {
            /**
             * appName : 喜乐淘
             * downloadUrl : /app/downLoad
             * id : b8qp
             * marketUrl : http://download.tkcaiji.com/app/downLoad
             * qRCode : qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e
             * type : 1
             * versionDescribe : 这是一个app
             * versionNumber : 1.0.0
             */

            public String appName;
            public String downloadUrl;
            public String id;
            public String marketUrl;
            public String qRCode;
            public int type;
            public String versionDescribe;
            public String versionNumber;
        }

        public static class MaxAppBean {
            /**
             * appName : 喜乐淘
             * downloadUrl : /app/downLoad
             * id : b8qp
             * marketUrl : http://download.tkcaiji.com/app/downLoad
             * qRCode : qn|taoyouji2|0b132fb09543de102dcfbeacd40c650e
             * type : 1
             * versionDescribe : 这是一个app
             * versionNumber : 1.0.0
             */

            public String appName;
            public String downloadUrl;
            public String id;
            public String marketUrl;
            public String qRCode;
            public int type;
            public String versionDescribe;
            public String versionNumber;
        }
}
