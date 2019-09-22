package com.vboss.okline.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.data.CardDataSource;
import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;

import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 卡证服务器数据加载入口
 */
public class CardRemoteDataSource implements CardDataSource {

    /*********************卡片业务请求常量定义************************/

    /**
     * 卡片详情
     */
    private static final String CARD_DETAIL = "qryCardInfoRequest";

    /**
     * 卡片详情
     */
    private static final String CARD_DETAIL_BY_NUM = "qryCardDtlRequest";
    /**
     * 卡片类标
     */
    private static final String CARD_LIST = "qryCardListByCardTypeRequest";
    /**
     * 卡片置顶
     */
    private static final String TOP_CARD = "setCardDefultRequest";
    /**
     * 单张卡片挂失
     */
    private static final String LOST_CARD = "";
    /**
     * 卡片交易记录
     */
    private static final String CARD_LOG = "qryCardTransLogRequest";
    /**
     * 卡片类型列表
     */
    private static final String CARD_TYPE_LIST = "qryCardMainTypeRequest";

    private static final String PULL_CARD_LOG_BY_ORDERNO = "qryOrderInfoRequest";


    private static CardRemoteDataSource instance;
    private final OKLineClient retrofitClient;

    public static CardRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new CardRemoteDataSource();
        }
        return instance;
    }

    private CardRemoteDataSource() {
        retrofitClient = OKLineClient.getInstance();
    }

    @Override
    public Observable<CardEntity> cardDetail(int cardMainType, int cardId) {
        return retrofitClient.requestAsyncForData(CARD_DETAIL, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardId(cardId)
                .build(), CardEntity.class);
    }

    public Observable<CardEntity> cardDetailByNum(int cardMainType, String cardNo) {
        return retrofitClient.requestAsyncForData(CARD_DETAIL_BY_NUM, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardNo(cardNo)
                .build(), CardEntity.class);
    }

    @Override
    public Observable<List<CardEntity>> cardList(@CardFlavor int cardMainType) {
        return retrofitClient.requestAsyncForData(CARD_LIST,
                RequestData.newBuilder()
                        .cardMainType(cardMainType).build(),
                new TypeToken<List<CardEntity>>() {
                }.getType());
    }

    public Observable<List<CardEntity>> cardList(@NonNull String olNo, int cardMainType) {
        return retrofitClient.requestAsyncForData(CARD_LIST,
                RequestData.newBuilder()
                        .olNo(olNo)
                        .cardMainType(cardMainType).build(),
                new TypeToken<List<CardEntity>>() {
                }.getType());
    }

    @Override
    public Observable<List<CardEntity>> searchCard(@Nullable String cardName) {
        if (cardName == null) {
            return Observable.empty();
        }
        return retrofitClient.requestAsyncForData(CARD_LIST, RequestData.newBuilder()
                .cardMainType(CardType.ALL)
                .cardName(cardName).build(), new TypeToken<List<CardEntity>>() {
        }.getType());
    }

    @Override
    public Observable<Boolean> topCard(@CardFlavor int cardMainType, int cardId) {
        return retrofitClient.requestAsyncForBoolean(TOP_CARD, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardId(cardId).build());
    }

    @Override
    public void lossSingleCard(@CardFlavor int mainType, int cardId) {

    }

    @Override
    public Observable<Boolean> updateCard(Map<String, ?> fieldMap) {
        return Observable.error(new UnsupportedOperationException("Not supported by now"));
    }

    @Override
    public Observable<List<CardLog>> getOrSearchCardLog(@CardFlavor int cardMainType, @NonNull String cardNo, String remark, String transDate, int pageIndex, int pageSize) {
        return retrofitClient.requestAsyncForData(CARD_LOG, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardNo(cardNo)
                .remark(remark)
                .transDate(transDate)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build(), new TypeToken<List<CardLog>>() {
        }.getType());
    }

    @Override
    public Observable<List<CardType>> cardTypeList() {
        return retrofitClient.requestAsyncForData(CARD_TYPE_LIST, RequestData.newBuilder().build()
                , new TypeToken<List<CardType>>() {
                }.getType());
    }

    @Override
    public Observable<CardLog> queryCardLogByOrderNo(int cardMainType, int cardId, @NonNull String orderNO, String payDate, String tag) {
        return retrofitClient.requestAsyncForData(PULL_CARD_LOG_BY_ORDERNO,
                RequestData.newBuilder()
                        .cardMainType(cardMainType)
                        .cardId(cardId)
                        .orderNo(orderNO)
                        .payDate(payDate)
                        .tags(tag).build(), CardLog.class);
    }


}
