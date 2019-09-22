package com.vboss.okline.data.entities;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 卡证交易记录
 */
@AutoValue
public abstract class CardLog implements CardLogModel {

    private static final CardLogModel.Factory<CardLog> FACTORY = new Factory<>(new Creator<CardLog>() {

        @Override
        public CardLog create(int _id, int cardMainType, int cardId, @Nullable String cardNo, @Nullable String cardName, @Nullable String transTime, int amount, @Nullable String imgUrl, @Nullable String remark, @Nullable String orderNo, @Nullable String slipUrl, @Nullable String receiptUrl, @Nullable String invoiceUrl, @Nullable String merName, int integral) {
            return new AutoValue_CardLog(_id, cardMainType, cardId, cardNo, cardName, transTime, amount, imgUrl, remark, orderNo, slipUrl, receiptUrl, invoiceUrl, merName, integral);
        }
    });

    public static TypeAdapter<CardLog> typeAdapter(Gson gson) {
        return new AutoValue_CardLog.GsonTypeAdapter(gson);
    }

    public final static CardLogModel.Mapper<CardLog> MAPPER = new Mapper<>(FACTORY);

    /**
     * JavaBean to ContentValues
     *
     * @param log
     * @return
     */
    public final static Marshal buildMarshal(CardLog log) {
        return new Marshal(log);
    }

    public static Builder newBuilder() {
        return new AutoValue_CardLog.Builder()
                ._id(-1)
                .integral(0)
                .amount(0);
    }

    @AutoValue.Builder
    public interface Builder {
        Builder _id(int id);

        Builder cardId(int cardId);

        Builder cardNo(String cardNo);

        Builder cardName(String cardName);

        Builder cardMainType(int type);

        Builder transTime(String transDate);

        Builder amount(int amount);

        Builder remark(String remark);

        Builder imgUrl(String url);

        Builder orderNo(String orderNo);

        Builder slipUrl(String url);

        Builder receiptUrl(String url);

        Builder invoiceUrl(String url);

        Builder merName(String name);

        Builder integral(int integral);

        CardLog build();
    }

}
