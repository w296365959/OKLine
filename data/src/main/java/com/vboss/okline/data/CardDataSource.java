package com.vboss.okline.data;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
 * Summary : 卡片操作接口类
 */
public interface CardDataSource {

    /**
     * 获取卡片详情
     *
     * @param cardMainType
     * @param cardId
     * @return
     */
    Observable<CardEntity> cardDetail(@CardFlavor int cardMainType, int cardId);

    /**
     * 获取卡片列表
     *
     * @param cardMainType
     * @return
     */
    Observable<List<CardEntity>> cardList(@CardFlavor int cardMainType);

    /**
     * 根据卡片名称搜索卡片
     *
     * @param cardName 搜索关键字
     * @return
     */
    Observable<List<CardEntity>> searchCard(@Nullable String cardName);

    /**
     * 设置默认卡
     *
     * @param cardMainType
     * @param cardId
     */
    Observable<Boolean> topCard(@CardFlavor int cardMainType, int cardId);

    /**
     * 挂失单张卡片
     *
     * @param mainType
     * @param cardId
     */
    void lossSingleCard(@CardFlavor int mainType, int cardId);

    /**
     * 更新卡片
     *
     * @param fieldMap
     */
    Observable<Boolean> updateCard(Map<String, ?> fieldMap);

    /**
     * 获取或搜索卡片交易记录
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       卡号
     * @param remark      按商户名搜索
     * @param transDate        按日期搜索
     * @param pageIndex    页码
     * @param pageSize     每页的条目
     * @return
     */
    Observable<List<CardLog>> getOrSearchCardLog(@CardFlavor int cardMainType, @NonNull String cardNo, String remark, String transDate, @IntRange(from = 1) int pageIndex, @IntRange(from = 1) int pageSize);


    /**
     * 获取卡片类型列表
     *
     * @return
     */
    Observable<List<CardType>> cardTypeList();

    /**
     * 刷卡通知
     * @param cardMainType
     * @param cardId
     * @param orderNO 订单号
     * @param transDate
     * @param tag @return
     */
    Observable<CardLog> queryCardLogByOrderNo(int cardMainType, int cardId, @NonNull String orderNO, String transDate, String tag);


}
