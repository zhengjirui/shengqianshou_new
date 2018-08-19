package com.lechuang.lqsq.bean;

/**
 * Created by YRJ
 * Date 2018/3/22.
 */

public class PickRedEnvBean {
    private String info;

    /**
     * 领取红包是否成功
     */
    public boolean isSuccess() {
        return "0".equals(info);
    }

    public RandomRedEnvelope randomRedEnvelope;

    public static class RandomRedEnvelope {
        public double amount;
        public String amountStr;
        public String envelopeDescription;
    }

}
