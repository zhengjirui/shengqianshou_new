package com.lechuang.lqsq.net.api;

import com.lechuang.lqsq.bean.ShouJiBean;
import com.lechuang.lqsq.bean.ShouJiShareBean;
import com.lechuang.lqsq.bean.TipoffDetail;
import com.lechuang.lqsq.bean.TipoffListBean;
import com.lechuang.lqsq.bean.TipoffShowBean;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.QUrl;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


/**
 * 作者：li on 2017/9/28 15:52
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public interface ShouJiApi {

    //商品列表接口
    @FormUrlEncoded
    @POST(QUrl.productCircleShow)
    Observable<NetResult<ShouJiBean>> productCircleShow(@Field("page") String page);

    //物料列表接口
    @FormUrlEncoded
    @POST(QUrl.posterCircleShow)
    Observable<NetResult<ShouJiBean>> posterCircleShow(@Field("page") String page);

    //获取分享图片的接口
    @FormUrlEncoded
    @POST(QUrl.shareProductFriendCircle)
    Observable<NetResult<ShouJiShareBean>> shareProductFriendCircle(@FieldMap Map<String, String> allParamMap);

}
