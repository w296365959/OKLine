package com.vboss.okline.data.local;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.okline.vboss.http.EncryptUtils;
import com.vboss.okline.data.GsonUtils;
import com.vboss.okline.data.entities.User;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/18 <br/>
 * Summary : 本地文件缓存工具
 */
public class SPUtils {
    private static SharedPreferences sp;

    public static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 缓存用户
     *
     * @param user
     */
    public static void setUser(User user) {
        put("user", user);
    }

    /**
     * 获取用户
     */
    public static User getUser() {
        String str = sp.getString("user", null);
        if (str != null) {
            return GsonUtils.fromJson(EncryptUtils.decodeBase64(str), User.class);
        }

        return null;
    }

    /**
     * 缓存欧乐号
     */
    public static void setOlNo(String olNo) {
        put("ol_no", olNo);
    }

    /**
     * 获取欧乐号
     */
    public static String getOlNo() {
        return get("ol_no", String.class);
    }

    /**
     * 缓存数据
     *
     * @param key    key
     * @param object 数据对象
     * @param <T>    数据对象类型
     */
    public static <T> void put(String key, T object) {
        if (TextUtils.isEmpty(key) || object == null) return;
        sp.edit().putString(key, EncryptUtils.encodeBase64(GsonUtils.toJson(object))).apply();
    }

    /**
     * 获取缓存数据
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> tClass) {
        if (TextUtils.isEmpty(key) || tClass == null) return null;
        String str = sp.getString(key, null);

        if (str != null) {
            return GsonUtils.fromJson(EncryptUtils.decodeBase64(str), tClass);
        }
        return null;
    }

    public static void saveSp(Context mContext, String Key, String value) {
        mContext.getSharedPreferences(AppConfig.SP_DIR, 0).edit().putString(Key, value).apply();
    }

    public static String getSp(Activity mContext, String key) {
        String result = "";
        result = mContext.getSharedPreferences(AppConfig.SP_DIR, 0).getString(key, "");
        return result;
    }

    public static void saveSpBoolean(Context mContext, String Key, Boolean value) {
        mContext.getSharedPreferences(AppConfig.SP_DIR, 0).edit().putBoolean(Key, value).apply();
    }

    public static Boolean getSpBoolean(Activity mContext, String key,Boolean defValue) {
        return mContext.getSharedPreferences(AppConfig.SP_DIR, 0).getBoolean(key, defValue);
    }

}
