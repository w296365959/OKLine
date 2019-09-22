package com.vboss.okline.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;

import java.util.List;

import rx.Observable;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 记录数据访问入口
 */
public interface RecordDataSource {

    /**
     * 获取交易日期列表。日期格式：yyyyMMdd
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    Observable<List<String>> getTransDateList(int pageNum, int pageSize);

    /**
     * 根据交易日期获取交易记录
     *
     * @param transDate 日期字串，格式：yyyyMMdd
     * @return
     */
    Observable<List<CardLog>> getTransLogList(String transDate);

    /**
     * 保存交易记录
     *
     * @param log 记录对象
     * @return 保存成功返回true，否则返回false。
     */
    Observable<Boolean> saveTransLog(CardLog log);

    /**
     * 获取或搜索机构下卡证列表
     *
     * @param cardName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<List<OrgzCard>> orgzCardList(@Nullable String cardName, int pageIndex, int pageSize);

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
    Observable<List<CardLog>> orgzCardLogList(@CardFlavor int cardMainType, @NonNull String cardNo,
                                              @Nullable String keyToSearch, int pageIndex, int pageSize);
}
