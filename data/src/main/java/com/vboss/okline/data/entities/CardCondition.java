package com.vboss.okline.data.entities;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.vboss.okline.data.GsonUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/11 <br/>
 * Summary : 开卡条件实体类
 */
@AutoValue
public abstract class CardCondition implements Serializable {

    /**
     * @return 是否需要认证
     */
    @SerializedName("isCert")
    public abstract int needToAuth();

    /**
     * @return 是否需要开卡费
     */
    @SerializedName("isOpenFee")
    public abstract int needFeeToOpen();

    /**
     * 是否需要开卡预存
     */
    @SerializedName("isOpenPre")
    public abstract int needPrestoreToOpen();

    /**
     * 充值金额列表
     */
    public abstract List<Integer> amountList();

    /**
     * 开卡金额列表
     */
    public abstract List<CardFee> cardFeeList();

    public static Builder newBuilder() {
        return new AutoValue_CardCondition.Builder()
                .needFeeToOpen(0)
                .needPrestoreToOpen(0)
                .cardFeeList(Collections.<CardFee>emptyList())
                .needToAuth(0)
                .amountList(Collections.<Integer>emptyList());
    }

    @AutoValue.Builder
    public interface Builder {
        Builder needToAuth(int needAuth);

        Builder needFeeToOpen(int needFee);

        Builder needPrestoreToOpen(int needPrestore);

        Builder amountList(List<Integer> list);

        Builder cardFeeList(List<CardFee> list);

        CardCondition build();


    }


    public class CardFee implements Serializable {
        /**
         * 开卡金额
         */
        private int amount;
        /**
         * 金额备注
         */
        private String memo;

        public int getAmount() {
            return amount;
        }

        public String getMemo() {
            return memo;
        }
    }

    public static TypeAdapter<CardCondition> typeAdapter(Gson gson) {
        return new AutoValue_CardCondition.GsonTypeAdapter(gson);
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
