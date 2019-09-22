package com.vboss.okline.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vboss.okline.data.adapter.AutoValueGsonTypeAdapterFactory;

import java.lang.reflect.Type;

/**
 * 请使用{@link com.okline.vboss.http.GsonUtils}
 * <p>
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/29 <br/>
 * Summary : Gson工具
 * <p>
 */
@Deprecated
public class GsonUtils {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
            .create();

    public static Gson getGson() {
        return GSON;
    }

    public static <T> String toJson(Object src, Class<T> clazz) {
        return GSON.toJson(src, clazz);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }
}
