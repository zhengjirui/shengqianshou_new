package com.lechuang.lqsq.net;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.bean.ProductDetailsBean;
import com.lechuang.lqsq.net.api.HomeApi;

import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date 2018/3/22.
 */
public class NetwordUtil {
    /**
     * 查询商品信息
     */
    public static void queryProductDetails(String productID, NetResultHandler<ProductDetailsBean> resultBack) {
        HomeApi homeApi = Network.getInstance().getApi(HomeApi.class);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        //用户id
        String userId = sp.getString("id", "");
        Observable<NetResult<ProductDetailsBean>> observable;
        if (userId.length() == 0) {
            observable = homeApi.queryProductDetails(productID);
        } else {
            observable = homeApi.queryProductDetails(productID, userId);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBack);
    }

    /**
     * 查询商品信息
     */
    public static void queryProductDetails(HashMap<String, String> paramMap, NetResultHandler<ProductDetailsBean> resultBack) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        //用户id
        String userId = sp.getString("id", "");
        if (userId.length() != 0) {
            paramMap.put("userId", userId);
        }
        Network.getInstance().getApi(HomeApi.class)
                .queryProductDetails(paramMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBack);
    }
}
