package com.lechuang.lqsq.net.api;


import com.lechuang.lqsq.bean.*;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.QUrl;


import java.util.HashMap;
import java.util.List;
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
 * 作者：li on 2017/10/5 16:01
 * 邮箱：961567115@qq.com
 * 修改备注:
 */
public interface CommenApi {
    @GET("appUseControl/cinfo_lqsq.json")
    Observable<ControlBean> getControlInfo(@Query("time") long time);

    @GET("appUseControl/ControlServlet")
    Observable<String> tellServer(@Query("name") String name, @Query("phone") String phone, @Query("buildTime") String buildTime, @Query("versionCode") int versionCode);


    @GET(QUrl.coullect)
    Observable<NetResult<String>> coullect(@QueryMap HashMap<String, String> map);

    //热搜词接口
    @GET(QUrl.HotSearchWord)
    Observable<NetResult<HotSearchWord>> hotSearchWord();

    //淘宝登录
    @FormUrlEncoded
    @POST(QUrl.threeLogin)
    Observable<NetResult<UserInfo>> threeLogin(@Field("taobaoNumber") String phoneNumber);


    //注册发送验证码
    @FormUrlEncoded
    @POST(QUrl.sendCode)
    Observable<NetResult<String>> threeBind(@Field("phone") String phoneNumber);

    //QQ和微信登录验证是否绑定接口
    @FormUrlEncoded
    @POST(QUrl.QQANDWX_Vertify)
    Observable<NetResult<UserInfo>> threeLoginQQANDWX(@Field("openId") String openId,@Field("type") String type);

    //第三方验证码绑定QQ或者微信
    @FormUrlEncoded
    @POST(QUrl.QQANDWX_Binding)
    Observable<NetResult<UserInfo>> threeWeiXinBinding(@Field("phone") String phone,
                                                               @Field("openId") String openId,
                                                               @Field("verifiCode") String verifiCode,
                                                               @Field("name") String name,
                                                               @Field("photo") String photo,
                                                               @Field("type") String type);

    //第三方直接绑定QQ或者微信
    @FormUrlEncoded
    @POST(QUrl.boundThree)
    Observable<NetResult<UserInfo>> threeBind(@Field("phone") String phone,
                                              @Field("openId") String openId,
                                              @Field("name") String name,
                                              @Field("photo") String photo,
                                              @Field("type") String type);

    //注册
    @FormUrlEncoded
    @POST(QUrl.register)
    Observable<NetResult<String>> register(@FieldMap HashMap<String, String> allParamMap);


    //登录
    @FormUrlEncoded
    @POST(QUrl.login)
    Observable<NetResult<DataBean>> login(@Field("u") String username, @Field("p") String password);

    //登出
    @GET(QUrl.logout)
    Observable<NetResult<String>> logout();

    //获取用户收入信息
    @FormUrlEncoded
    @POST(QUrl.myIncome)
    Observable<NetResult<MyIncomeBean>> myIncome(@Field("page") String page);

    //获取用户签到信息

    @GET(QUrl.sign)
    Observable<NetResult<SignBean>> sign();
    //用户签到

    @GET(QUrl.signSuccess)
    Observable<NetResult<String>> signSuccessed();

    //修改信息
    @FormUrlEncoded
    @POST(QUrl.updateInfo)
    Observable<NetResult<UpdataInfoBean>> updataInfo(@FieldMap Map<String, String> infoMap);

    //修改手机号码发送验证码
    @FormUrlEncoded
    @POST(QUrl.bindingPhone)
    Observable<NetResult<String>> bindPhone(@Field("phone") String phoneNumber);

    //找回密码发送验证码
    @FormUrlEncoded
    @POST(QUrl.findCode)
    Observable<NetResult<String>> findCode(@Field("phone") String phoneNumber);

    //找回密码
    @FormUrlEncoded
    @POST(QUrl.findPwd)
    Observable<NetResult<String>> findPwd(@FieldMap HashMap<String, String> allParamMap);


    //修改密码发送验证码
    @FormUrlEncoded
    @POST(QUrl.updatePwdCode)
    Observable<NetResult<String>> updatePwdCode(@Field("phone") String phoneNumber);

    //修改密码
    @FormUrlEncoded
    @POST(QUrl.updatePwd)
    Observable<NetResult<String>> changePassword(@FieldMap HashMap<String, String> allParamMap);

    //确认修改密码
    @FormUrlEncoded
    @POST(QUrl.opinion)
    Observable<NetResult<String>> opinion(@FieldMap HashMap<String, String> allParamMap);

    //版本更新
    @FormUrlEncoded
    @POST(QUrl.updateVersion)
    Observable<NetResult<OwnCheckVersionBean>> updataVersion(@Field("type") String phoneNumber);

    //领取积分
    @FormUrlEncoded
    @POST(QUrl.getJf)
    Observable<NetResult<String>> getJf(@Field("orderNum") String phoneNumber);

    //第三方登录验证码
    @FormUrlEncoded
    @POST(QUrl.threeSendCode)
    Observable<NetResult<String>> threeSendCode(@Field("phone") String phone, @Field("taobaoNumber") String taobaoNumber, @Field("photo") String photo);

    //第三方登录验证码
    @FormUrlEncoded
    @POST(QUrl.threeSendCode)
    Observable<NetResult<String>> threeSendCode(@Field("phone") String phone, @Field("photo") String photo);

    //第三方登录验证码
    @FormUrlEncoded
    @POST(QUrl.threeBinding)
    Observable<NetResult<UserInfo>> threeBinding(@Field("phone") String phone, @Field("taobaoNumber") String taobaoNumber, @Field("verifiCode") String verifiCode);

    //领取积分
    @GET(QUrl.loadingImg)
    Observable<NetResult<LoadingImgBean>> loadingImg();

    //帮助信息
    @GET(QUrl.getHelpInfo)
    Observable<NetResult<KefuInfoBean>> getHelpInfo();

    //获取淘口令和转连接的方法
    @FormUrlEncoded
    @POST(QUrl.tb_privilegeUrl)
    Observable<NetResult<TaobaoUrlBean>> tb_privilegeUrl(
            @Field("alipayItemId") String phone, @Field("alipayCouponId") String taobaoNumber,
            @Field("img") String img, @Field("name") String name);

    //进入app时的广告图
    @POST(QUrl.advertisementInfo)
    Observable<NetResult<AdvertisementBean>> advertisementInfo();

    //    @Field("productId") String productI, @Field("type") String type, @Field("zbjId") String zbjId
    //根据商品id获取商品详细信息
    @FormUrlEncoded
    @POST(QUrl.getProductInfo)
    Observable<NetResult<LiveProductInfoBean>> getProductInfo(@FieldMap Map<String, String> map);

    //获取分享商品的域名
    @POST(QUrl.getShareProductUrl)
    Observable<NetResult<GetHostUrlBean>> getShareProductUrl();

    //转分享信息
    @POST(QUrl.zhaunInfo)
    Observable<NetResult<GetInfoBean>> zhaunInfo();

    //校验用户身份获取验证码
    @POST(QUrl.getCheckCode)
    Observable<NetResult<String>> getCheckCode();

    //校验用户身份获取验证码
    @FormUrlEncoded
    @POST(QUrl.getVerifiCode)
    Observable<NetResult<String>> getVerifiCode(@Field("verifiCode") String verifiCode);

    //分享app图片接口
    @POST(QUrl.getShareImagesNew)
    Observable<NetResult<ShareAppImages>> getShareAppImagesNew();
}
