package com.vboss.okline.data;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/10 <br/>
 * Summary : 在这里描述Class的主要功能
 */
@RunWith(MockitoJUnitRunner.class)
public class AppRepositoryTest extends AndroidTestCase{
    private AppRepository repository ;
    @Mock
    Context mMockContext;
    @Before
    public void setup(){
        if (repository == null){
            repository = AppRepository.getInstance(mMockContext);
        }
    }

    @Test
    public void getAppDetail() throws Exception {

    }

    @Test
    public void getOrSearchMyApp() throws Exception {

    }

    @Test
    public void getLatestPoolApp() throws Exception {

    }

    @Test
    public void getAllApp(){
    }

    @Test
    public void getOrSearchPoolApp() throws Exception {

    }

    @Test
    public void getAppAdsList() throws Exception {

    }

    @Test
    public void deleteApp() throws Exception {

    }

    @Test
    public void starApp() throws Exception {

    }

    @Test
    public void setAppComponent() throws Exception {

    }

    @Test
    public void getOrSearchPoolCard() throws Exception {

    }

}