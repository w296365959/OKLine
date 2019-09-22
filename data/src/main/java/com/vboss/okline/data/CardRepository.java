package com.vboss.okline.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.local.AppLocalDataSource;
import com.vboss.okline.data.local.CardLocalDataSource;
import com.vboss.okline.data.remote.CardRemoteDataSource;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 卡证数据仓库。当本地数据不存在时，从服务器加载数据。
 */
public class CardRepository implements CardDataSource {
    private static final String TAG = CardRepository.class.getSimpleName();

    private final CardLocalDataSource local;
    private final CardRemoteDataSource remote;
    private static CardRepository instance;
    private final AppLocalDataSource appLocal;

    public static CardRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CardRepository(context);
        }
        return instance;
    }

    private CardRepository(Context context) {
        local = CardLocalDataSource.getInstance(context);
        appLocal = AppLocalDataSource.getInstance(context);
        remote = CardRemoteDataSource.getInstance();
    }

    /**
     * 预备用户卡片数据
     */
    public void prepareCardData() {
        WorkerThreadScheduler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    remote.cardList(CardType.ALL).map(new Func1<List<CardEntity>, Void>() {
                        @Override
                        public Void call(List<CardEntity> cardEntities) {
                            if (cardEntities != null && !cardEntities.isEmpty()) {
                                local.saveAll(cardEntities);
                            }
                            return null;
                        }
                    }).toBlocking().single();
                } catch (Exception e) {
                    Timber.tag(TAG).e(e);
                }
            }
        });
    }

    @Override
    public Observable<CardEntity> cardDetail(@CardFlavor int cardMainType, int cardId) {
        return remote.cardDetail(cardMainType, cardId).flatMap(new Func1<CardEntity, Observable<CardEntity>>() {
            @Override
            public Observable<CardEntity> call(final CardEntity entity) {
                if (entity != null && entity.appId() != 0) {
                    //Attach AppEntity to CardEntity
                    return Observable.just(entity).zipWith(appLocal.getAppDetail(entity.appId()),
                            new Func2<CardEntity, AppEntity, CardEntity>() {
                                @Override
                                public CardEntity call(CardEntity entity, AppEntity appEntity) {
                                    entity.setAppEntity(appEntity);
                                    return entity;
                                }
                            });
                } else {
                    return Observable.just(entity);
                }
            }
        });
    }

    /**
     * 获取卡片列表。当对数据库进行增删改时会出发卡片查询动作。
     *
     * @param cardMainType
     * @return
     */
    @Override
    public Observable<List<CardEntity>> cardList(@CardFlavor final int cardMainType) {
        return local.cardList(cardMainType);
    }

    @Override
    public Observable<List<CardEntity>> searchCard(@Nullable String cardName) {
        return local.searchCard(cardName);
    }

    @Override
    public Observable<Boolean> topCard(@CardFlavor int cardMainType, int cardId) {
        return local.topCard(cardMainType, cardId).zipWith(remote.topCard(cardMainType, cardId),
                new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean remote, Boolean local) {
                        return remote && local;
                    }
                });
    }

    @Override
    public void lossSingleCard(@CardFlavor int mainType, int cardId) {
        throw new UnsupportedOperationException("Not implement");
    }

    @Override
    public Observable<Boolean> updateCard(Map<String, ?> fieldMap) {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取或搜索卡片交易记录
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       卡号
     * @param remark      按商户号搜索
     * @param transDate    按日期搜索
     * @param pageIndex    页码
     * @param pageSize     每页的数据条目
     * @return
     */
    @Override
    public Observable<List<CardLog>> getOrSearchCardLog(@CardFlavor int cardMainType, @NonNull String cardNo, @Nullable String remark, @Nullable String transDate, int pageIndex, int pageSize) {
        return remote.getOrSearchCardLog(cardMainType, cardNo, remark, transDate, pageIndex, pageSize);
    }

    @Override
    public Observable<List<CardType>> cardTypeList() {
        return remote.cardTypeList();
    }

    /**
     * 根据订单号查询交易记录
     *
     * @param cardMainType
     * @param cardId
     * @param orderNo   订单号
     * @param transDate 交易日期,格式：yyyy-MM-dd
     * @param tag
     */
    @Override
    public Observable<CardLog> queryCardLogByOrderNo(int cardMainType, int cardId, @NonNull String orderNo, String transDate, String tag) {
        return remote.queryCardLogByOrderNo(cardMainType, cardId, orderNo, transDate, tag);
    }

    /**
     * 获取默认卡
     *
     * @param cardMainType 卡片类型
     * @return
     */
    public Observable<CardEntity> getTopCard(int cardMainType) {
        return local.getTopCard(cardMainType);
    }

}
