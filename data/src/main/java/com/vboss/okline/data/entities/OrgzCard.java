package com.vboss.okline.data.entities;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/18 <br/>
 * Summary : 机构卡片实体类
 */

@AutoValue
public abstract class OrgzCard {

    /**
     * 卡号
     */
    public abstract String cardNo();

    /**
     * 交易时间
     */
    public abstract String transTime();

    /**
     * 交易备注
     */
    public abstract String remark();

    /**
     * 卡片类型
     */
    public abstract int cardMainType();

    /**
     * 卡片图片地址
     */
    public abstract String imgUrl();

    /**
     * 卡片名称
     */
    public abstract String cardName();

    public abstract int cardId();

    /**
     * Gson转换器
     */
    public static TypeAdapter<OrgzCard> typeAdapter(Gson gson) {
        return new AutoValue_OrgzCard.GsonTypeAdapter(gson);
    }


}
