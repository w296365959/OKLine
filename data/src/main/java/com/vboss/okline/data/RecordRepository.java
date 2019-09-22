package com.vboss.okline.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.data.local.RecordLocalDataSource;
import com.vboss.okline.data.remote.RecordRemoteDataSource;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/1 <br/>
 * Summary : 记录数据操作入口
 */
public class RecordRepository implements RecordDataSource {
    private static RecordRepository instance;
    private final RecordRemoteDataSource remote;
    private final RecordLocalDataSource local;

    public static RecordRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecordRepository(context);
        }
        return instance;
    }

    private RecordRepository(Context context) {
        remote = RecordRemoteDataSource.getInstance();
        local = new RecordLocalDataSource(context);
    }

    /**
     * 获取交易日期列表
     *
     * @param pageNum  页码
     * @param pageSize 页数据条目
     * @return
     */
    @Override
    public Observable<List<String>> getTransDateList(int pageNum, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageNum, pageSize);
        return remote.getTransDateList(pageNum, pageSize);
    }

    /**
     * 获取某交易日期下的所有记录
     *
     * @param transDate 日期字串，格式：yyyyMMdd
     * @return
     */
    @Override
    public Observable<List<CardLog>> getTransLogList(String transDate) {
        return remote.getTransLogList(transDate);
    }

    /**
     * 获取缓存下来的所有交易记录
     */
    public List<CardLog> getCachedLog() {
        return local.getCachedLog();
    }

    /**
     * 保存交易记录到本地。
     *
     * @param log 记录对象
     * @return 返回缓存的记录条数
     */
    @Override
    public Observable<Boolean> saveTransLog(CardLog log) {
        return local.saveTransLog(log);
    }

    /**
     * 获取或搜索机构卡列表
     *
     * @param cardName  卡片名称
     * @param pageIndex 页码
     * @param pageSize  页条目数
     * @return
     */
    @Override
    public Observable<List<OrgzCard>> orgzCardList(@Nullable String cardName, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        return remote.orgzCardList(cardName, pageIndex, pageSize);
    }

    /**
     * 获取某机构卡下交易记录
     *
     * @param cardMainType 卡片类型
     * @param cardNo       卡号
     * @param keyToSearch  keyToSearch为空时获取某卡的交易记录，不为空时则搜索。
     * @param pageIndex    页码
     * @param pageSize     页数据条目
     * @return
     */
    @Override
    public Observable<List<CardLog>> orgzCardLogList(@CardFlavor int cardMainType, @NonNull String cardNo, @Nullable String keyToSearch, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        return remote.orgzCardLogList(cardMainType, cardNo, keyToSearch, pageIndex, pageSize);
    }
}
