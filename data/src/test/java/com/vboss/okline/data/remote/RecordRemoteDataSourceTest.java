package com.vboss.okline.data.remote;

import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/10 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RecordRemoteDataSourceTest {
    private RecordRemoteDataSource remote;

    @Before
    public void setup(){
        if (remote == null){
            remote = RecordRemoteDataSource.getInstance();
        }
    }

    @Test
    public void getTransDateList() throws Exception {
        remote.getTransDateList(1,20).subscribe(new DefaultSubscriber<List<String>>());
    }

    @Test
    public void getTransLogList() throws Exception {
        remote.getTransLogList("").subscribe(new DefaultSubscriber<List<CardLog>>());
    }

    @Test
    public void getOrSearchTransLogs() throws Exception {

    }

    @Test
    public void saveTransLog() throws Exception {

    }

    @Test
    public void orgzCardList() throws Exception {
        List<OrgzCard> list = remote.orgzCardList(/*"中国光大银行"*/ null, 1, 10).toBlocking().single();
        for (OrgzCard en :
                list) {

            System.out.println(en);
        }
    }

    @Test
    public void orgzCardLogList() throws Exception {

    }

}