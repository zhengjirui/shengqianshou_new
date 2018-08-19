package com.lechuang.lqsq.net.converters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by YST on 2016/8/11.
 */
public class StringGsonConverFactory extends Converter.Factory {
    public static StringGsonConverFactory create() {
        return create(new Gson());
    }
    public static StringGsonConverFactory create(Gson gson) {
        return new StringGsonConverFactory(gson);
    }
    private final Gson gson;

    private StringGsonConverFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new StringRequestBodyConverter();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter);
    }

}
