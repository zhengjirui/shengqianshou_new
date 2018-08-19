package com.lechuang.lqsq.net.api;


import com.lechuang.lqsq.bean.*;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.QUrl;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author: guoning
 * Date: 2017/10/8
 * Description:
 */

public interface OwnApi {
    @FormUrlEncoded
    @POST(QUrl.nextTeam)
    Observable<NetResult<TeamNextBean>> nextTeam(@Field("userId") String userId, @Field("page") int page);

    @FormUrlEncoded
    @POST(QUrl.mineTeam)
    Observable<NetResult<TeamBean>> mineTeam(@Field("page") int page, @Field("type") int type);

    @POST("user/appUsers/agencyDetail")
    Observable<NetResult<OwnBean>> agency();

    /**
     * 申请成为代理时发起支付
     *
     * @return
     */
    @POST("agency/agencySendOrder")
    Observable<NetResult<OwnBean.Pay>> pay();

    /**
     * 申请成功回调
     *
     * @return
     */
    @FormUrlEncoded
    @POST(QUrl.aplySuccess)
    Observable<NetResult<String>> paySuccess(@FieldMap HashMap<String, String> allParamMap);

    @FormUrlEncoded
    @POST(QUrl.ownIncome)
    Observable<NetResult<OwnIncomeBean>> ownIncome(@Field("type") int type);

    /**
     * 积分提现时积分信息
     * 参数无
     */
    @POST(QUrl.txInfo)
    Observable<NetResult<OwnJiFenInfoBean>> jifenInfo();

    /**
     * 积分提现
     *
     * @withdrawPrice 提现金额
     */
    @FormUrlEncoded
    @POST(QUrl.tx)
    Observable<NetResult<String>> jifenTx(@Field("withdrawPrice") Double withdrawPrice);

    /**
     * 判断是否有未读消息
     * 出参 status  是否显示小红点   0：不显示   1：显示
     */
    @POST(QUrl.isUnread)
    Observable<NetResult<OwnNewsBean>> isUnread();

    /**
     * 消息列表
     *
     * @param page 分页
     */
    @FormUrlEncoded
    @POST(QUrl.allNews)
    Observable<NetResult<OwnNewsListBean>> allNws(@Field("page") int page);

    /**
     * 用户信息
     */
    @POST(QUrl.userInfo)
    Observable<NetResult<UserInfo>> userInfo();

    @FormUrlEncoded
    @POST(QUrl.orderDetails)
    Observable<NetResult<OrderBean>> orderDetails(@FieldMap Map<String, String> map);

    /**
     * 我的代理信息
     */
    @FormUrlEncoded
    @POST(QUrl.agent)
    Observable<NetResult<OwnMyAgentBean>> agentInfo(@Field("page") int page);

    /**
     * 我的代理信息
     */
    @POST(QUrl.shareMoneyInfo)
    Observable<NetResult<ShareMoneyBean>> shareMoneyInfo();

    /**
     * 英雄榜信息
     */
    @GET(QUrl.heroAgent)
    Observable<NetResult<HeroBean>> heroAgentInfo(@Query("type") int type);

    /**
     * 自动成为代理
     */
    @POST(QUrl.autoAgent)
    Observable<NetResult<String>> autoAgent();

    /**
     * 足迹
     *
     * @return
     */
    @FormUrlEncoded
    @POST(QUrl.zuji)
    Observable<NetResult<HomeSearchResultBean>> getZuji(@Field("page") int page);

    /**
     * 收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST(QUrl.shoucang)
    Observable<NetResult<HomeSearchResultBean>> getShoucang(@Field("page") int page);

    @GET(QUrl.duiba)
    Observable<NetResult<DuiBaBean>> getduiba();

}
