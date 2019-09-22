package com.vboss.okline.data.entities;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : App广告实体类
 */

@AutoValue
public abstract class AppAds {
    /**
     * app广告Id
     *
     * @return
     */
    public abstract int adId();

    /**
     * 排列顺序。最大的排最前。
     *
     * @return
     */
    public abstract int shortShow();

    /**
     * 广告图片地址。
     *
     * @return
     */
    public abstract String imgUrl();

    /**
     * 是否已开卡。大于0 已开， 0 未开或卡不是正常使用状态。
     *
     * @return
     */
    @SerializedName("openUrl")
    public abstract String isOpen();

    public static TypeAdapter<AppAds> typeAdapter(Gson gson) {
        return new AutoValue_AppAds.GsonTypeAdapter(gson);
    }
}
