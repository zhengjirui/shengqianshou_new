package com.lechuang.lqsq.bean;

/**
 * 作者：li on 2017/10/8 18:05
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public class UpdataInfoBean {

    /**
     * data : {"createTime":1507263300000,"createTimeStr":"2017-10-06 12:15:00","id":"1k711","nickName":"接近婆媳","password":"b8ecebe2560a2dc8cb2017a6db32bee5294d58f0ac36780ad37d7705c9b676a086d5945baf751c8a","phone":"18258874191","photo":"http://img.taoyouji666.com/AE6F10A42D4FECC97A8EEBE4552FAB86?imageView2/2/w/150/q/90","signedStatus":1,"status":1,"verifiCode":558805,"vipGradeId":1}
     * error : Success
     * errorCode : 200
     */
        /**
         * createTime : 1507263300000
         * createTimeStr : 2017-10-06 12:15:00
         * id : 1k711
         * nickName : 接近婆媳
         * password : b8ecebe2560a2dc8cb2017a6db32bee5294d58f0ac36780ad37d7705c9b676a086d5945baf751c8a
         * phone : 18258874191
         * photo : http://img.taoyouji666.com/AE6F10A42D4FECC97A8EEBE4552FAB86?imageView2/2/w/150/q/90
         * signedStatus : 1
         * status : 1
         * verifiCode : 558805
         * vipGradeId : 1
         */

        public long createTime;
        public String createTimeStr;
        public String id;
        public String nickName;
        public String password;
        public String phone;
        public String photo;
        public int signedStatus;
        public int status;
        public int verifiCode;
        public int vipGradeId;
}
