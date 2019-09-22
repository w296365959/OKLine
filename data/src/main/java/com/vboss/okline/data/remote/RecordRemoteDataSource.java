package com.vboss.okline.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.data.CardFlavor;
import com.vboss.okline.data.RecordDataSource;
import com.vboss.okline.data.RepoUtils;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.data.entities.TransDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/9 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RecordRemoteDataSource implements RecordDataSource {
    /**
     * 获取交易日期列表
     */
    private static final String GET_TRANS_DATE_LIST = "qryCardTransdayRequest";
    /**
     * 获取某交易日期的交易记录
     */
    private static final String GET_TRANS_DATE_LOG_LIST = "qryCardTransListRequest";

    /**
     * 获取个人卡片列表
     */
    private static final String ORGZ_CARD_LIST = "qryCardOrgRequest";
    /**
     * 获取个人卡片交易记录
     */
    private static final String ORGZ_CARD_LOG_LIST = "qryCardTransLogRequest";

    private final OKLineClient client;
    public static RecordRemoteDataSource instance;

    public static RecordRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new RecordRemoteDataSource();
        }
        return instance;
    }

    private RecordRemoteDataSource() {
        client = OKLineClient.getInstance();
    }

    @Override
    public Observable<List<String>> getTransDateList(int pageNum, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageNum, pageSize);
        return client.<List<TransDate>>requestAsyncForData(GET_TRANS_DATE_LIST, RequestData.newBuilder()
                .pageIndex(pageNum)
                .pageSize(pageSize).build(), new TypeToken<List<TransDate>>() {
        }.getType()).map(new Func1<List<TransDate>, List<String>>() {
            @Override
            public List<String> call(List<TransDate> list) {
                List<String> dateList = new ArrayList<String>();
                for (TransDate date : list) {
                    dateList.add(date.getTransDate());
                }
                return dateList;
            }
        });
    }

    @Override
    public Observable<List<CardLog>> getTransLogList(String transDate) {
        return client.requestAsyncForData(GET_TRANS_DATE_LOG_LIST, RequestData.newBuilder()
                .transDate(transDate).build(), new TypeToken<List<CardLog>>() {
        }.getType());
    }

    @Override
    public Observable<Boolean> saveTransLog(CardLog log) {
        return Observable.empty();
    }

    /**
     * 批量上传log
     *
     * @param logs
     * @return 返回上传成功的记录的交易时间
     */
    public List<String> saveTransLogInBulk(List<CardLog> logs) {
        // TODO: 2017/4/27  同步上传交易记录

        return Collections.emptyList();
    }

    @Override
    public Observable<List<OrgzCard>> orgzCardList(@Nullable String cardName, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        return client.requestAsyncForData(ORGZ_CARD_LIST, RequestData.newBuilder()
                .cardName(cardName)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build(), new TypeToken<List<OrgzCard>>() {
        }.getType());
    }

    @Override
    public Observable<List<CardLog>> orgzCardLogList(@CardFlavor int cardMainType, @NonNull String cardNo, @Nullable String keyToSearch, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        RequestData data = RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardNo(cardNo)
                .remark(keyToSearch)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();

        return client.requestAsyncForData(ORGZ_CARD_LOG_LIST, data, new TypeToken<List<CardLog>>() {
        }.getType());
    }
}
