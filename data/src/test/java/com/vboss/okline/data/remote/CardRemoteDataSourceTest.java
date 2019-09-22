package com.vboss.okline.data.remote;

import com.vboss.okline.data.LogUtils;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

public class CardRemoteDataSourceTest {
    private CardRemoteDataSource remoteService;

    @Before
    public void setUp() throws Exception {
        if (remoteService == null) {
            remoteService = CardRemoteDataSource.getInstance();
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void cardDetail() throws Exception {
        remoteService.cardDetail(100, 807).subscribe(new DefaultSubscriber<CardEntity>());
    }

    @Test
    public void cardList() throws Exception {
        List<CardEntity> list = remoteService.cardList(CardType.BANK_CARD).toBlocking().single();
        System.out.println(list);
    }

    @Test
    public void searchCard() throws Exception {
        String keyToSearch = "光";
        remoteService.searchCard(keyToSearch).subscribe(new DefaultSubscriber<List<CardEntity>>() {
            @Override
            public void onNext(List<CardEntity> cardEntities) {
                super.onNext(cardEntities);
                System.out.println("Card info : " + cardEntities.get(0));
            }
        });
    }

    @Test
    public void topCard() throws Exception {
        remoteService.topCard(CardType.BANK_CARD, 840).subscribe(new DefaultSubscriber<Boolean>());
    }

    @Test
    public void cardLogList() throws Exception {
        remoteService.cardDetail(CardType.BANK_CARD, 840).flatMap(new Func1<CardEntity, Observable<List<CardLog>>>() {
            @Override
            public Observable<List<CardLog>> call(CardEntity entity) {
                return remoteService.getOrSearchCardLog(entity.cardMainType(), entity.cardNo(), null, null, 1, 20);
            }
        }).subscribe(new DefaultSubscriber<List<CardLog>>() {
            @Override
            public void onNext(List<CardLog> cardLogs) {
                super.onNext(cardLogs);
            }
        });

    }

    @Test
    public void cardTypeList() throws Exception {
        remoteService.cardTypeList().subscribe(new DefaultSubscriber<List<CardType>>());
    }

    @Test
    public void getCardLogByOrderNo() {
        CardLog log = remoteService.queryCardLogByOrderNo(100, 843, "POS20170401161036962270", "2017-04-04", null).toBlocking().single();
        LogUtils.println(log);
    }

}