package com.lechuang.lqsq.bean;

/**
 * 作者：li on 2017/11/1 10:31
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class GetInfoBean {

    /**
     * data : {"record":{"id":"b8qp","shareZhuanInfo":"<p><b><u><i>ssssss<\/i><\/u><\/b><\/p><p><br><\/p>"}}
     * error : Success
     * errorCode : 200
     */

        /**
         * record : {"id":"b8qp","shareZhuanInfo":"<p><b><u><i>ssssss<\/i><\/u><\/b><\/p><p><br><\/p>"}
         */

        public RecordBean record;

        public static class RecordBean {
            /**
             * id : b8qp
             * shareZhuanInfo : <p><b><u><i>ssssss</i></u></b></p><p><br></p>
             */

            public String id;
            public String shareZhuanInfo;
        }
}
