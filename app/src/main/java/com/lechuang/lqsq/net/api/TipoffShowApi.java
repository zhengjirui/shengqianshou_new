package com.lechuang.lqsq.net.api;

import com.lechuang.lqsq.bean.*;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.QUrl;

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
public interface TipoffShowApi {
    //    @Field("condition") String data, @Field("conditionType") int type, @Field("page") int page
    //爆料详情接口
    @FormUrlEncoded
    @POST(QUrl.tipOff)
    Observable<NetResult<TipoffShowBean>> getTipoff(@FieldMap Map<String, String> map);

    //爆料不分时间全部展示
    @FormUrlEncoded
    @POST(QUrl.tipOff)
    Observable<NetResult<TipoffShowBean>> getTipoffs(@Field("page") int page);

    //爆料详细接口
    @FormUrlEncoded
    @POST(QUrl.tipOffDetail)
    Observable<NetResult<TipoffDetail>> getTipoffDetail(@Field("id") String id);

    //爆料评论内容接口
    @FormUrlEncoded
    @POST(QUrl.tipOffList)
    Observable<NetResult<TipoffListBean>> getTipoffList(@Field("tipOffId") String id, @Field("page") int page);

    //发表爆料评论接口
    @FormUrlEncoded
    @POST(QUrl.tipContent)
    Observable<NetResult<String>> sendContent(@Field("id") String id, @Field("type") int page, @Field("content") String content);

    //为爆料点赞
    @FormUrlEncoded
    @POST(QUrl.tipPraise)
    Observable<NetResult<String>> tipPraise(@Field("id") String id, @Field("type") int page);

}
