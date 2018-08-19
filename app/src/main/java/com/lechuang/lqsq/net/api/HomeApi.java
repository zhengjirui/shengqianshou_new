package com.lechuang.lqsq.net.api;

import com.lechuang.lqsq.bean.*;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.QUrl;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author yrj
 * @date 2017/9/29
 * @E-mail 1422947831@qq.com
 * 首页api
 */
public interface HomeApi {

    @FormUrlEncoded
    @POST(QUrl.homeTodayProduct)
    Observable<NetResult<HomeTodayProductBean.ProudList>> homeTodayProduct(@Field("page") int page);

    @POST(QUrl.soufanli_programaImg)
    Observable<NetResult<SoufanliProgremBean>> soufanliProgramaImg();

    @POST(QUrl.countSum)
    Observable<NetResult<CountBean>> countSum();

    @FormUrlEncoded
    @POST(QUrl.soufanliProduct)
    Observable<NetResult<SoufanliResultBean>> soufanliProduct(@Field("page") int page);

    /**
     * 首页轮播图接口
     * 入参 无
     */
    @POST(QUrl.homePageBanner)
    Observable<NetResult<HomeBannerBean>> homeBanner();

    /**
     * 分类接口
     * 入参 无
     */
    @POST(QUrl.home_classify)
    Observable<NetResult<HomeKindBean>> homeClassify();

    @POST(QUrl.classify)
    Observable<NetResult<FenLeiBean>> getClassify();


    /**
     * 滚动条接口(滚动的文字)
     * 入参 无
     */
    @POST(QUrl.home_scrollTextView)
    Observable<NetResult<HomeScrollTextViewBean>> homeScrollTextView();

    /**
     * 首页四个栏目图片接口
     * 入参 无
     */
    @POST(QUrl.home_programaImg)
    Observable<NetResult<HomeProgramBean>> homeProgramaImg();

    /**
     * 首页最下栏目接口
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.home_lastPage)
    Observable<NetResult<HomeLastProgramBean>> homeLastPage(@Field("page") int page);

    /**
     * 首页栏目1详情接口
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.recommend1)
    Observable<NetResult<HomeProgramDetailBean>> program1(@Field("page") int page);

    /**
     * 首页栏目2详情接口
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.recommend2)
    Observable<NetResult<HomeProgramDetailBean>> program2(@Field("page") int page);

    /**
     * 首页栏目3详情接口
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.recommend3)
    Observable<NetResult<HomeProgramDetailBean>> program3(@Field("page") int page);

    /**
     * 首页栏目4详情接口
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.recommend4)
    Observable<NetResult<HomeProgramDetailBean>> program4(@Field("page") int page);

    /**
     * 过夜单
     *
     * @param page 分页加载页数
     */
    @FormUrlEncoded
    @POST(QUrl.overnightBill)
    Observable<NetResult<HomeSearchResultBean>> overnightBill(@Field("page") int page);

    /**
     * 搜索结果接口
     *
     * @param allParamMap 所有参数
     *                    因为参数名会变化 全部拼接到allParameter中
     *                    <p>
     *                    拼接内容 page + 搜索的种类 + 商品排序方式
     *                    product 搜索的种类,分类页面传递的是classTypeId = 分类的id,搜索页面传递的参数是name = 用户输入的搜索内容
     *                    商品排序方式
     *                    isVolume 1代表按销量排序从高到底
     *                    isAppraise 1好评从高到底
     *                    isPrice  1价格从低到高排序
     *                    isPrice  2价格从高到低排序
     *                    isNew    1新品商品冲最近的往后排序
     */
    @FormUrlEncoded
    @POST(QUrl.home_product)
    Observable<NetResult<HomeSearchResultBean>> searchResult(@FieldMap Map<String, String> allParamMap);

    @FormUrlEncoded
    @POST(QUrl.home_programa)
    Observable<NetResult<HomeSearchResultBean>> getProgramaShowAll(@FieldMap Map<String, String> allParamMap);


    /**
     * 首页最下栏目分类商品集合接口
     * 入参  page  分页    classTypeId  分类id,全部栏目不传        is_official   全部传1,其他不传
     */
    @FormUrlEncoded
    @POST(QUrl.home_lastPage)
    Observable<NetResult<HomeLastProgramBean>> homeLastProgram(@FieldMap HashMap<String, String> allParamMap);

    /**
     * 首页最下栏目 标题数据
     *
     * @return
     */
    @POST(QUrl.getTopTabList)
    Observable<NetResult<GetBean>> lastTabList();

    /**
     * 首页最下栏目 标题数据
     *
     * @return
     */
    @POST(QUrl.gunDongText)
    Observable<NetResult<HomeGunDongTextBean>> gunDongText();

    @GET(QUrl.programSwitch)
    Observable<NetResult<ProgramSwitch>> getProgramSwitch();

    @GET(QUrl.getHourTime)
    Observable<NetResult<HourTime>> getHourTime();

    /**
     * 获取活动信息
     */
    @POST(QUrl.envelopeactivity)
    Observable<NetResult<EnvelopeActivityBean>> envelopeactivity();

    /**
     * 拆红包
     */
    @POST(QUrl.pick_red_env)
    Observable<NetResult<PickRedEnvBean>> pickRedEnv();

    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST(QUrl.queryProductDetails)
    Observable<NetResult<ProductDetailsBean>> queryProductDetails(@Field("productId") String  productId,@Field("userId") String  userId);
    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST(QUrl.queryProductDetails)
    Observable<NetResult<ProductDetailsBean>> queryProductDetails(@Field("productId") String  productId);
    /**
     * 商品详情
     */
    @FormUrlEncoded
    @POST(QUrl.queryProductDetails)
    Observable<NetResult<ProductDetailsBean>> queryProductDetails(@FieldMap HashMap<String, String> allParamMap);

}
