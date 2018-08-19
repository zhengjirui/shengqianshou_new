package com.lechuang.lqsq.net.api;


import com.lechuang.lqsq.bean.*;
import com.lechuang.lqsq.net.NetResult;
import com.lechuang.lqsq.net.QUrl;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author: guoning
 * Date: 2017/10/2
 * Description:
 */

public interface GetApi {

    /**
     * 顶部tab列表
     * @return
     */
    @POST(QUrl.getTopTabList)
    Observable<NetResult<GetBean>> topTabList();

    /**
     * 顶部广告图
     * @return
     */
    @POST(QUrl.getTopBanner)
    Observable<NetResult<GetBean>> topBanner();

    /**
     *
     * @param name 搜索框查询内容
     * @param page 页数
     * @param classTypeId 分类id(精选传空)
     * @return
     */
    @FormUrlEncoded
    @POST(QUrl.getListInfo)
    Observable<NetResult<GetBean>> listInfo(@FieldMap Map<String, Object> map);





}
