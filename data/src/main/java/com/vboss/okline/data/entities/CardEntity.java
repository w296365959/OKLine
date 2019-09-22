package com.vboss.okline.data.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.vboss.okline.data.GsonUtils;

import java.io.Serializable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 卡证实体类
 */

@AutoValue
public abstract class CardEntity implements CardModel, Serializable {
    /**
     * 普通卡
     */
    //卡片属性定义
    public static final int CATEGORY_NORMAL = 0;
    /**
     * 银行卡
     */
    public static final int CATEGORY_BANK_CARD = 1;
    /**
     * 上付卡
     */
    public static final int CATEGORY_QUICK_PASS = 2;
    /**
     * 银行卡绑定为闪付卡
     */
    public static final int CATEGORY_BANK_CARD_TO_QUICK_PASS = 3;

    private static final Factory<CardEntity> FACTORY = new Factory<>(new Creator<CardEntity>() {
        @Override
        public CardEntity create(int cardId, @Nullable String cardNo, int appId, @Nullable String imgUrl, @Nullable String iconUrl, @Nullable String bindId, @Nullable String aid, int isQuickPass, int isDefault, int balance, int integral, @Nullable String cardName, @Nullable String merName, int cardMainType) {
            return new AutoValue_CardEntity(cardId, cardNo, appId, imgUrl, iconUrl, bindId, aid, isQuickPass, isDefault, balance, integral, cardName, merName, cardMainType, 0);
        }
    });

    public static TypeAdapter<CardEntity> typeAdapter(Gson gson) {
        return new AutoValue_CardEntity.GsonTypeAdapter(gson);
    }

    /**
     * Map cursor row to POJO
     */
    public static final Mapper<CardEntity> MAPPER = new Mapper<>(FACTORY);

//    public static final Marshal MARSHAL = new Marshal(null);

    /**
     * POJO to ContentValues
     *
     * @return
     */
    public static Marshal buildMarshal(CardEntity entity) {
        return new Marshal(entity);
    }


    @Override
    public String toString() {
        return GsonUtils.toJson(CardEntity.this);
    }

    private AppEntity appEntity;

    public AppEntity getAppEntity() {
        return appEntity;
    }

    public void setAppEntity(AppEntity appEntity) {
        this.appEntity = appEntity;
    }

    /**
     * 查看卡片是否已开开
     * <p>
     * > 0 : 已开卡；<br/>
     * = 0 ：未开卡
     */
    @SerializedName("isopen")
    public abstract int isOpen();
}
