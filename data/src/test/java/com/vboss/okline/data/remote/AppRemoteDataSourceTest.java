package com.vboss.okline.data.remote;

import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/9 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class AppRemoteDataSourceTest {
    AppRemoteDataSource remote;

    @Before
    public void setup() {
        remote = AppRemoteDataSource.getInstance();
    }

    @Test
    public void getLatestPoolApp() throws Exception {
        remote.getLatestPoolApp().subscribe(new DefaultSubscriber<List<AppEntity>>());
    }

    @Test
    public void getOrSearchPoolApp() throws Exception {
        List<AppEntity> list = remote.getOrSearchPoolApp(CardType.ALL, null, 1, 20).toBlocking().single();
        System.out.println(list);
    }

    @Test
    public void getAppAdsList() throws Exception {
        remote.getAppAdsList().subscribe(new DefaultSubscriber<List<AppAds>>());
    }

    @Test
    public void getOrSearchPoolCard() throws Exception {
        remote.getOrSearchPoolCard(CardType.COMMON_CARD, null, 1, 20).subscribe(new DefaultSubscriber<List<CardEntity>>());
    }

}