package com.lechuang.lqsq.net.converters;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by YST on 2016/7/19.
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody responseBody) throws IOException {
        return responseBody.string();
    }
}
