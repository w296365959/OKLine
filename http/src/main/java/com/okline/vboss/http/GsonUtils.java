package com.okline.vboss.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/29 <br/>
 * Summary : Gson工具
 */
public class GsonUtils {
    private static Gson GSON = createGson();

    /**
     * 创建Gson对象
     *
     * @param gson
     * @return
     */
    public static Gson initialize(Gson gson) {
        if (gson == null) throw new IllegalStateException("Gson must not be null");
        if (GSON != null) {
            GSON = null;
        }
        return GSON = gson;
    }

    private static Gson createGson() {
        return new GsonBuilder().create();
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
