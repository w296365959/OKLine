package com.vboss.okline.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vboss.okline.data.CardDataSource;
import com.vboss.okline.data.IDataSource;
import com.vboss.okline.data.RepoUtils;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 卡片本地数据操作入口
 */
public class CardLocalDataSource implements CardDataSource, IDataSource<CardEntity> {
    private static final String TAG = CardLocalDataSource.class.getSimpleName();
    private DBService dbService;
    private static CardLocalDataSource instance;
    private final Func1<Cursor, CardEntity> CARD_MAPPER;

    public static CardLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new CardLocalDataSource(context);
        }
        return instance;
    }

    private CardLocalDataSource(Context context) {
        dbService = DBService.getInstance(context);
        CARD_MAPPER = new Func1<Cursor, CardEntity>() {
            @Override
            public CardEntity call(Cursor cursor) {
                return CardEntity.MAPPER.map(cursor);
            }
        };
    }

    public boolean isDataExisting(String whereClause, String... whereArgs) {
        return dbService.isDataExisting(CardEntity.TABLE_NAME, whereClause, whereArgs);
    }

    @Override
    public Observable<CardEntity> cardDetail(int cardMainType, int cardId) {
        throw new UnsupportedOperationException("User remote resource");

        /*String whereClause = CardEntity.CARDMAINTYPE + "=? and " + CardEntity.CARDID + "=?";

        return dbService.queryOneWithWhere(CardEntity.TABLE_NAME, CARD_MAPPER, whereClause,
                String.valueOf(cardMainType), String.valueOf(cardId));*/
    }

    @Override
    public Observable<List<CardEntity>> cardList(int cardMainType) {
        return dbService.queryListWithWhere(CardEntity.TABLE_NAME, CARD_MAPPER,
                CardEntity.CARDMAINTYPE + "=?" + " ORDER BY " + CardEntity.ISDEFAULT + " DESC ",
                String.valueOf(cardMainType));

    }

    @Override
    public Observable<List<CardEntity>> searchCard(@Nullable String cardName) {
        if (TextUtils.isEmpty(cardName)) {
            return Observable.error(new IllegalArgumentException("The key to search is empty"));
        } else {
            return dbService.queryListQuietly(CardEntity.TABLE_NAME, CARD_MAPPER,
                    RepoUtils.buildLikeStatement(CardEntity.CARDNAME, cardName));
        }
    }

    @Override
    public Observable<Boolean> topCard(final int cardMainType, final int cardId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    //清除某类型的卡的top标志
                    int size1 = dbService.update(CardEntity.TABLE_NAME, CardEntity.buildMarshal(null).isDefault(0).asContentValues(),
                            CardEntity.CARDMAINTYPE + " = ? AND " + CardEntity.ISDEFAULT + "=?",
                            String.valueOf(cardMainType), String.valueOf(1));
                    Timber.tag(TAG).i("Clear top flag, size = %d", size1);
                    //设置top标志
                    int size = dbService.update(CardEntity.TABLE_NAME, CardEntity.buildMarshal(null).isDefault(1).asContentValues(),
                            CardEntity.CARDMAINTYPE + "= ? AND " + CardEntity.CARDID + " = ?",
                            String.valueOf(cardMainType), String.valueOf(cardId));

                    subscriber.onNext(size != 0);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public void lossSingleCard(int mainType, int cardId) {

    }

    @Override
    public Observable<Boolean> updateCard(Map<String, ?> fieldMap) {
        return null;
    }

    @Override
    public Observable<List<CardLog>> getOrSearchCardLog(int cardMainType, @NonNull String cardNo, String remark, String transDate, int pageIndex, int pageSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<List<CardType>> cardTypeList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<CardLog> queryCardLogByOrderNo(int cardMainType, int cardId, @NonNull String orderNO, String transDate, String tag) {
        return null;
    }

    @Override
    public Boolean save(CardEntity cardEntity) {
        return dbService.insert(CardEntity.TABLE_NAME, CardEntity.buildMarshal(cardEntity).isDefault(cardEntity.isDefault() == 1 ? 1 : 0).asContentValues()) != -1;
    }

    @Override
    public boolean saveAll(List<CardEntity> list) {
        List<ContentValues> tmpList = new ArrayList<>();
        for (CardEntity en : list) {
            tmpList.add(CardEntity.buildMarshal(en).isDefault(en.isDefault() == 1 ? 1 : 0).asContentValues());
        }
        return dbService.bulkInsert(CardEntity.TABLE_NAME, tmpList) != 0;
    }

    /**
     * 获取默认卡
     *
     * @param cardMainType
     * @return
     */
    public Observable<CardEntity> getTopCard(int cardMainType) {
        return dbService.queryListWithWhere(CardEntity.TABLE_NAME, CARD_MAPPER, CardEntity.CARDMAINTYPE + "=? AND "
                + CardEntity.ISDEFAULT + "=?", String.valueOf(cardMainType), String.valueOf(1)).map(new Func1<List<CardEntity>, CardEntity>() {
            @Override
            public CardEntity call(List<CardEntity> cardEntities) {
                if (cardEntities != null && cardEntities.size() != 1) {
                    throw new IllegalStateException("More one default card found");
                }
                return cardEntities != null ? cardEntities.get(0) : null;
            }
        });
    }
}
