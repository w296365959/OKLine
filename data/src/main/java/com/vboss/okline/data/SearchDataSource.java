package com.vboss.okline.data;

import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.ContactEntity;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/9 <br/>
 * Summary : 数据搜索操作接口
 */
public interface SearchDataSource {

    /**
     * 搜索卡片， {@code cardMainType}表示搜索的类别。
     *
     * @param cardMainType
     * @param cardName     搜索的关键字
     * @param pageIndex    搜索的页码索引
     * @param pageSize     每页的条目数
     * @return
     */
    Observable<List<CardEntity>> searchCard(int cardMainType, String cardName, int pageIndex, int pageSize);

    /**
     * 搜索交易记录。 {@code cardMainType}表示搜索的类别。
     *
     * @param cardMainType
     * @param cardName     搜索关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<List<CardLog>> searchCardLog(int cardMainType, String cardName, int pageIndex, int pageSize);

    /**
     * 搜索联系人
     *
     * @param remarkName 搜索关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<ContactEntity> searchContact(String remarkName, int pageIndex, int pageSize);

    /**
     * 搜索我的App。{@code cardMainType}表示搜索的类别。
     *
     * @param appName   搜索关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<AppEntity> searchMyApp(String appName, int pageIndex, int pageSize);

    /**
     * 搜索应用池App。{@code cardMainType}表示搜索的类别。
     *
     * @param cardMainType
     * @param appName      搜索关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Observable<AppEntity> searchPoolApp(int cardMainType, String appName, int pageIndex, int pageSize);
}
