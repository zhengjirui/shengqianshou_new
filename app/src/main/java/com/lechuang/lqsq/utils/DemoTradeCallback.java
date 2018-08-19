package com.lechuang.lqsq.utils;

import android.widget.Toast;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.ResultType;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.lechuang.lqsq.MyApplication;


/**
 * @author li
 *         邮箱：961567115@qq.com
 * @time 2017/9/26  14:27
 * @describe 阿里百川接口回调了
 */
public class DemoTradeCallback implements AlibcTradeCallback {

    /*public void onTradeSuccess(AlibcTradeResult tradeResult) {
        //当addCartPage加购成功和其他page支付成功的时候会回调
        if (tradeResult.resultType.equals(AlibcResultType.TYPECART)) {
            //加购成功
            Toast.makeText(BaseApplication.instance, "加购成功", Toast.LENGTH_SHORT).show();
        } else if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)) {
            //支付成功
            Utils.E("支付成功:" + tradeResult.payResult.paySuccessOrders);
            //订单编号list集合遍历String，如：111,222,333
            String orderId = "";
            for (String order : tradeResult.payResult.paySuccessOrders) {
                orderId += order + ",";
            }
            orderNumber(orderId.substring(0, orderId.length() - 1));
            //Toast.makeText(BaseApplication.instance, "支付成功,成功订单号为" + tradeResult.payResult.paySuccessOrders, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int errCode, String errMsg) {
        Toast.makeText(MyApplication.getInstance(), "错误码=" + errCode + " / 错误消息=" + errMsg, Toast.LENGTH_SHORT).show();
    }*/

    private void orderNumber(String order) {
       /* new OrderNumberImpl().orderNumber(MyApplication.getInstance(), order, new ApiRequest() {
            @Override
            public void onSuccess(int status, Object... object) throws JSONException {
                if (status == Constants.STATE) {
                    Toast.makeText(MyApplication.getInstance(), (String) object[0], Toast.LENGTH_SHORT).show();
                }else if(status ==300){
                    try {
                        Toast.makeText(BaseApplication.instance, (String) object[0], Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }  else {
                    Toast.makeText(BaseApplication.instance, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(BaseApplication.instance, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onTradeSuccess(TradeResult tradeResult) {
        //当addCartPage加购成功和其他page支付成功的时候会回调
        if (tradeResult.resultType.equals(ResultType.TYPECART)) {
            //加购成功
            Toast.makeText(MyApplication.getContext(), "加购成功", Toast.LENGTH_SHORT).show();
        } else if (tradeResult.resultType.equals(ResultType.TYPEPAY)) {
            //支付成功
            Utils.E("支付成功:" + tradeResult.payResult.paySuccessOrders);
            //订单编号list集合遍历String，如：111,222,333
            String orderId = "";
            for (Object order : tradeResult.payResult.paySuccessOrders) {
                orderId += order + ",";
            }
            orderNumber(orderId.substring(0, orderId.length() - 1));
            //Toast.makeText(BaseApplication.instance, "支付成功,成功订单号为" + tradeResult.payResult.paySuccessOrders, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(int i, String s) {
        Toast.makeText(MyApplication.getContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
    }
}
