package com.vboss.okline.data.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.IDataSource;
import com.vboss.okline.data.RecordDataSource;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/27 <br/>
 * Summary : 记录本地数据操作
 */
public class RecordLocalDataSource implements RecordDataSource, IDataSource<CardLog> {
    private final DBService dbService;
    private final Func1<Cursor, CardLog> CARD_LOG_MAPPER;

    public RecordLocalDataSource(Context context) {
        dbService = DBService.getInstance(context);
        CARD_LOG_MAPPER = new Func1<Cursor, CardLog>() {
            @Override
            public CardLog call(Cursor cursor) {
                return CardLog.MAPPER.map(cursor);
            }
        };
    }

    /**
     * 获取交易日期列表。日期格式：yyyyMMdd
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Observable<List<String>> getTransDateList(int pageNum, int pageSize) {
        return null;
    }

    /**
     * 获取本地缓存的交易记录
     *
     * @param transDate
     * @return
     */
    @Override
    public Observable<List<CardLog>> getTransLogList(String transDate) {
        return null;
    }

    /**
     * 获取缓存的交易记录
     */
    public List<CardLog> getCachedLog() {
        return dbService.select(CardLog.TABLE_NAME, CardLog.MAPPER, null);
    }

    /**
     * 删除缓存的log
     */
    public void removeCachedLog() {
        dbService.delete(CardLog.TABLE_NAME, null);
    }

    @Override
    public Observable<Boolean> saveTransLog(final CardLog log) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return dbService.insert(CardLog.TABLE_NAME, CardLog.buildMarshal(log).asContentValues()) != -1;
            }
        });
    }

    /**
     * 获取或搜索机构下卡证列表
     *
     * @param cardName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Observable<List<OrgzCard>> orgzCardList(@Nullable String cardName, int pageIndex, int pageSize) {
        return null;
    }

    /**
     * 获取或搜索机构下某卡的交易记录
     *
     * @param cardMainType
     * @param cardNo
     * @param keyToSearch  keyToSearch为空时获取某卡的交易记录，不为空时则搜索。
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Observable<List<CardLog>> orgzCardLogList(@CardFlavor int cardMainType, @NonNull String cardNo, @Nullable String keyToSearch, int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public Boolean save(CardLog log) {
        return dbService.insert(CardLog.TABLE_NAME, CardLog.buildMarshal(log).asContentValues()) != -1;
    }

    @Override
    public boolean saveAll(List<CardLog> list) {
        return false;
    }
}
